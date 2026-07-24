import cv2
import numpy as np
from ultralytics import YOLO
from config import BOARD_ROI

# Map YOLO class names -> short display label.
# Extend this dict to match the class names in your chess model.
_PIECE_DISPLAY: dict[str, str] = {
    # Roboflow "chess-pieces" dataset style
    "white-king":   "wK", "white-queen":  "wQ", "white-rook":   "wR",
    "white-bishop": "wB", "white-knight": "wN", "white-pawn":   "wP",
    "black-king":   "bK", "black-queen":  "bQ", "black-rook":   "bR",
    "black-bishop": "bB", "black-knight": "bN", "black-pawn":   "bP",
    # Short-code style (wK, bR, …)
    "wk": "wK", "wq": "wQ", "wr": "wR", "wb": "wB", "wn": "wN", "wp": "wP",
    "bk": "bK", "bq": "bQ", "br": "bR", "bb": "bB", "bn": "bN", "bp": "bP",
    # Generic single-word fallback
    "king": "K?", "queen": "Q?", "rook": "R?",
    "bishop": "B?", "knight": "N?", "pawn": "P?",
}


def _norm(cls: str) -> str:
    """Normalise a class name for dict lookup."""
    return cls.lower().replace("_", "-").replace(" ", "-")


class ChessVision:
    def __init__(self, model_path: str = "best.pt", camera_index: int = None):
        self.cap = self._open_camera(camera_index)
        if not self.cap.isOpened():
            raise RuntimeError("No camera found. Check connection or set CAMERA_INDEX in main.py.")
        try:
            self.model = YOLO(model_path)
        except Exception as e:
            self.cap.release()
            raise RuntimeError(f"YOLO model not found: '{model_path}'. Download a chess model first.") from e
        self._board_roi = BOARD_ROI   # (x1, y1, x2, y2) or None

    # ── Camera helpers ────────────────────────────────────────────────────────

    @staticmethod
    def _open_camera(index=None) -> cv2.VideoCapture:
        """Try a specific index, or scan 0-4 and return the first working camera."""
        candidates = [index] if index is not None else range(5)
        for i in candidates:
            cap = cv2.VideoCapture(i, cv2.CAP_DSHOW)
            if cap.isOpened():
                ret, _ = cap.read()
                if ret:
                    print(f"[CV] Camera found at index {i}")
                    return cap
            cap.release()
        return cv2.VideoCapture(-1)

    def _capture_frame(self) -> np.ndarray:
        ret, frame = self.cap.read()
        if not ret:
            raise RuntimeError("Failed to capture frame from camera")
        return frame

    # ── Board region helpers ──────────────────────────────────────────────────

    def _get_board_region(self, frame: np.ndarray):
        """Return (board_sub_image, (ox, oy)) where (ox, oy) is top-left offset."""
        if self._board_roi:
            x1, y1, x2, y2 = self._board_roi
            return frame[y1:y2, x1:x2], (x1, y1)
        return frame, (0, 0)

    def _pixel_to_square(self, x: float, y: float, bw: int, bh: int) -> str | None:
        """Map pixel (x, y) inside the board region to a UCI square ('e4')."""
        if x < 0 or y < 0 or x >= bw or y >= bh:
            return None
        col = int(x / bw * 8)
        row = int(y / bh * 8)
        if not (0 <= col < 8 and 0 <= row < 8):
            return None
        file_char = chr(ord("a") + col)   # col 0 -> 'a', col 7 -> 'h'
        rank_char = str(8 - row)          # row 0 -> rank 8, row 7 -> rank 1
        return file_char + rank_char

    # ── YOLO inference ────────────────────────────────────────────────────────

    def _run_yolo(self, frame: np.ndarray):
        """
        Run YOLO on the board region.
        Returns (detections, annotated_board_img, (ox, oy, bw, bh)).
        detections: {square_lc: class_name}  e.g. {'e4': 'white-pawn'}
        """
        board_img, (ox, oy) = self._get_board_region(frame)
        bh, bw = board_img.shape[:2]
        results = self.model(board_img, verbose=False)

        detections: dict[str, str] = {}
        for box in results[0].boxes:
            cls_name = self.model.names[int(box.cls)]
            x_c = float(box.xywh[0][0])
            y_c = float(box.xywh[0][1])
            sq = self._pixel_to_square(x_c, y_c, bw, bh)
            if sq:
                detections[sq] = cls_name

        annotated_board = results[0].plot()
        return detections, annotated_board, (ox, oy, bw, bh)

    # ── Overlay drawing ───────────────────────────────────────────────────────

    def _draw_board_overlay(self, frame: np.ndarray,
                             detections: dict,
                             ox: int, oy: int,
                             bw: int, bh: int) -> np.ndarray:
        """
        Draw 8x8 grid, square labels (A1-H8), and detected piece names
        over the board region of frame (in-place).

        Layout per square:
            top-left  : square name  e.g. "E4"   (green)
            bottom    : piece label  e.g. "wN"   (yellow), if detected
        """
        sq_w = bw / 8
        sq_h = bh / 8

        # ── Subtle alternating-square tint ────────────────────────────────────
        tint = frame.copy()
        for col in range(8):
            for row in range(8):
                px = int(ox + col * sq_w);       py = int(oy + row * sq_h)
                ex = int(ox + (col + 1) * sq_w); ey = int(oy + (row + 1) * sq_h)
                color = (30, 50, 30) if (col + row) % 2 == 0 else (30, 30, 55)
                cv2.rectangle(tint, (px, py), (ex, ey), color, -1)
        cv2.addWeighted(tint, 0.25, frame, 0.75, 0, frame)

        # ── Grid lines ────────────────────────────────────────────────────────
        for i in range(9):
            lx = int(ox + i * sq_w)
            ly = int(oy + i * sq_h)
            cv2.line(frame, (lx, oy),      (lx, oy + bh), (0, 200, 0), 1)
            cv2.line(frame, (ox, ly), (ox + bw, ly),      (0, 200, 0), 1)

        # ── Labels per square ─────────────────────────────────────────────────
        for col in range(8):
            for row in range(8):
                px   = int(ox + col * sq_w)
                py   = int(oy + row * sq_h)
                sq_h_px = int(sq_h)

                file_char  = chr(ord("a") + col)
                rank_num   = 8 - row
                square_lc  = f"{file_char}{rank_num}"           # 'e4'
                label      = f"{file_char.upper()}{rank_num}"   # 'E4'

                # Square name — top-left corner, small
                cv2.putText(frame, label, (px + 3, py + 13),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.33,
                            (0, 230, 80), 1, cv2.LINE_AA)

                # Piece label — bottom of square
                if square_lc in detections:
                    cls     = detections[square_lc]
                    display = _PIECE_DISPLAY.get(_norm(cls), cls[:5])
                    cv2.putText(frame, display, (px + 2, py + sq_h_px - 4),
                                cv2.FONT_HERSHEY_SIMPLEX, 0.38,
                                (255, 220, 50), 1, cv2.LINE_AA)

        return frame

    # ── Public API ────────────────────────────────────────────────────────────

    def get_annotated_frame(self) -> np.ndarray:
        """
        Capture a frame, run YOLO on the board region, paste YOLO bounding
        boxes back, then draw the 8x8 grid with square labels and piece names.
        """
        frame = self._capture_frame()
        detections, board_ann, (ox, oy, bw, bh) = self._run_yolo(frame)

        # Paste YOLO-annotated board area back into the full frame
        if self._board_roi:
            x1, y1, x2, y2 = self._board_roi
            frame[y1:y2, x1:x2] = board_ann
        else:
            frame = board_ann

        return self._draw_board_overlay(frame, detections, ox, oy, bw, bh)

    def get_board_state(self) -> dict:
        """Capture a frame and return current {square: class_name} detections."""
        frame = self._capture_frame()
        detections, _, _ = self._run_yolo(frame)
        return detections

    def get_opponent_move(self) -> str | None:
        """
        Compare two consecutive board states to infer the opponent's move.
        Returns a UCI string ('e7e5') or None if no clear move detected.
        """
        before = self.get_board_state()
        after  = self.get_board_state()

        disappeared = [sq for sq in before if sq not in after]
        appeared    = [sq for sq in after  if sq not in before]

        if len(disappeared) == 1 and len(appeared) == 1:
            return disappeared[0] + appeared[0]

        if len(disappeared) == 2 and len(appeared) == 0:
            return disappeared[0] + disappeared[1]

        return None

    def release(self):
        self.cap.release()

    def __enter__(self):
        return self

    def __exit__(self, *_):
        self.release()
