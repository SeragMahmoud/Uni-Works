"""
test_moves.py  --  Chess move detector (frame-diff, no piece recognition).

Controls:
  SPACE  lock reference frame  (do with pieces in starting position, board still)
  R      reset board + reference
  Q      quit
"""

import time
import threading
import queue
import cv2
import numpy as np
import chess
import chess.engine
from engine import EngineController
from robot  import ArmController

# ── Config — edit these yourself ──────────────────────────────────────────────
CAMERA_INDEX   = None
BOARD_ROI      = (346, 97, 934, 651)
#                  ^    ^   ^    ^
#               TL-X  TL-Y BR-X BR-Y   (top-left X/Y, bottom-right X/Y)

BOARD_FLIP_H   = False   # True if A-file appears on the RIGHT of the screen
BOARD_FLIP_V   = False   # True if rank-1 appears at the TOP of the screen

DIFF_THRESHOLD = 18
# How sensitive the square diff is (lower = more sensitive, higher = less noise).
# Start at 18. If white pieces are missed → lower to 12.
# If empty squares light up on their own → raise to 25.

STILL_DIFF     = 6.0
# How still the board must be (frame-to-frame) before starting the 3-sec countdown.
# Lower = stricter. Raise if the countdown never starts.

SETTLE_SEC     = 5.0    # seconds of stillness — increase if black needs more time
# Seconds the board must stay still before the move is confirmed.

COOLDOWN_SEC   = 3.0
# Seconds to keep refreshing the reference after white's ARM move finishes,
# before accepting any black input. Prevents arm/hand motion from being
# misread as a black move.

MAX_CHANGED    = 6
# Ignore events where more than this many squares changed (hand sweep / noise).

SQ_MARGIN      = 0.08
# Fraction of each square trimmed at edges before diffing (avoids grid-line noise).
# ──────────────────────────────────────────────────────────────────────────────


# ── Optional filters — uncomment to enable ────────────────────────────────────

# _clahe = cv2.createCLAHE(clipLimit=5.0, tileGridSize=(4, 4))
#
# CLAHE (Contrast Limited Adaptive Histogram Equalization):
#   Boosts local contrast square-by-square so pieces stand out from the board.
#   clipLimit: how aggressive (try 3–8). tileGridSize: how local (try (2,2)–(8,8)).
#   Helps when: white pieces on white squares are not detected.

# def preprocess(frame):
#     lab = cv2.cvtColor(frame, cv2.COLOR_BGR2LAB)
#     l, a, b = cv2.split(lab)
#     l = _clahe.apply(l)                          # apply CLAHE to brightness only
#     enhanced = cv2.cvtColor(cv2.merge([l, a, b]), cv2.COLOR_LAB2BGR)
#     blur = cv2.GaussianBlur(enhanced, (0, 0), 2)
#     return cv2.addWeighted(enhanced, 1.8, blur, -0.8, 0)
#     # addWeighted(enhanced, 1.8, blur, -0.8): unsharp mask = sharpening.
#     # 1.8 / -0.8 ratio controls strength. Try 1.5/-0.5 (mild) or 2.2/-1.2 (strong).

# If you enable preprocess(), replace these two lines in main():
#   proc = preprocess(raw)        <-- add this after cap.read()
#   changed = get_changed_squares(proc, ref_proc)   <-- use proc not raw
#   ref_proc = proc.copy()        <-- store proc not raw when pressing SPACE
# ──────────────────────────────────────────────────────────────────────────────


def _pixel_to_sq(col: int, row: int) -> str:
    file_i = (7 - col) if BOARD_FLIP_H else col
    rank_i = row        if BOARD_FLIP_V else (7 - row)
    return chr(ord("a") + file_i) + str(rank_i + 1)


def _open_camera():
    for backend, bname in [(cv2.CAP_MSMF, "MSMF"), (cv2.CAP_DSHOW, "DSHOW")]:
        candidates = [CAMERA_INDEX] if CAMERA_INDEX is not None else range(5)
        for i in candidates:
            cap = cv2.VideoCapture(i, backend)
            if not cap.isOpened():
                cap.release()
                continue
            cap.set(cv2.CAP_PROP_FOURCC, cv2.VideoWriter_fourcc(*"MJPG"))
            cap.set(cv2.CAP_PROP_FRAME_WIDTH,  1280)
            cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)
            cap.set(cv2.CAP_PROP_FPS, 30)
            cap.set(cv2.CAP_PROP_AUTO_EXPOSURE, 1)   # 1 = manual exposure (no auto-adjust)
            cap.set(cv2.CAP_PROP_EXPOSURE, -5)        # fixed exposure value
            ret, frame = cap.read()
            if ret:
                print(f"[CV] Camera {i} ({bname}): {frame.shape[1]}x{frame.shape[0]}")
                return cap
            cap.release()
    return None


def board_still_diff(f1: np.ndarray, f2: np.ndarray) -> float:
    """Mean pixel diff between two consecutive frames inside the board ROI."""
    x1, y1, x2, y2 = BOARD_ROI
    return float(np.mean(cv2.absdiff(f1[y1:y2, x1:x2], f2[y1:y2, x1:x2])))


def get_changed_squares(current: np.ndarray, reference: np.ndarray) -> list[str]:
    """Return squares whose mean pixel diff vs reference exceeds DIFF_THRESHOLD."""
    x1, y1, x2, y2 = BOARD_ROI
    sq_w = (x2 - x1) / 8
    sq_h = (y2 - y1) / 8

    diff = cv2.cvtColor(
        cv2.absdiff(current[y1:y2, x1:x2], reference[y1:y2, x1:x2]),
        cv2.COLOR_BGR2GRAY
    )

    changed = []
    for col in range(8):
        for row in range(8):
            px = int(col * sq_w);        py = int(row * sq_h)
            ex = int((col + 1) * sq_w);  ey = int((row + 1) * sq_h)
            mx = max(1, int(sq_w * SQ_MARGIN))
            my = max(1, int(sq_h * SQ_MARGIN))
            region = diff[py+my : ey-my, px+mx : ex-mx]
            if region.size > 0 and float(np.mean(region)) > DIFF_THRESHOLD:
                changed.append(_pixel_to_sq(col, row))
    return changed


def identify_move(changed: list[str], board: chess.Board) -> str | None:
    """Find a legal UCI move that matches the changed squares. Tries both colors."""
    s = set(changed)

    def _search(b):
        for m in b.legal_moves:
            u = m.uci()
            if u[:2] in s and u[2:4] in s:
                return u
        return None

    r = _search(board)
    if r:
        return r
    board.turn = not board.turn
    r = _search(board)
    board.turn = not board.turn
    return r


def draw_overlay(display: np.ndarray, changed_set: set,
                 state: str, still_for: float, last_move: str) -> np.ndarray:
    x1, y1, x2, y2 = BOARD_ROI
    sq_w = (x2 - x1) / 8
    sq_h = (y2 - y1) / 8
    font = cv2.FONT_HERSHEY_SIMPLEX

    # faint checkerboard tint so grid is readable
    tint = np.zeros_like(display)
    for col in range(8):
        for row in range(8):
            px = int(x1 + col * sq_w);     py = int(y1 + row * sq_h)
            ex = int(x1 + (col+1) * sq_w); ey = int(y1 + (row+1) * sq_h)
            c = (20, 40, 20) if (col + row) % 2 == 0 else (20, 20, 45)
            cv2.rectangle(tint, (px, py), (ex, ey), c, -1)
    cv2.addWeighted(tint, 0.20, display, 1.0, 0, display)

    # yellow highlight on changed squares
    if changed_set:
        hi = display.copy()
        for sq in changed_set:
            fi  = ord(sq[0]) - ord('a')
            ri  = int(sq[1]) - 1
            col = (7 - fi) if BOARD_FLIP_H else fi
            row = ri if BOARD_FLIP_V else (7 - ri)
            px = int(x1 + col * sq_w);     py = int(y1 + row * sq_h)
            ex = int(x1 + (col+1) * sq_w); ey = int(y1 + (row+1) * sq_h)
            cv2.rectangle(hi, (px+1, py+1), (ex-1, ey-1), (0, 220, 220), -1)
        cv2.addWeighted(hi, 0.40, display, 0.60, 0, display)

    # grid lines + border
    cv2.rectangle(display, (x1, y1), (x2, y2), (0, 200, 80), 2)
    for i in range(1, 8):
        cv2.line(display, (int(x1+i*sq_w), y1), (int(x1+i*sq_w), y2), (0, 120, 60), 1)
        cv2.line(display, (x1, int(y1+i*sq_h)), (x2, int(y1+i*sq_h)), (0, 120, 60), 1)

    # square name labels
    fscale = max(0.28, min(0.48, sq_h / 80))
    for col in range(8):
        for row in range(8):
            sq  = _pixel_to_sq(col, row).upper()
            px  = int(x1 + col * sq_w)
            py  = int(y1 + row * sq_h)
            clr = (0, 0, 0) if sq.lower() in changed_set else (0, 200, 80)
            cv2.putText(display, sq, (px+3, py+int(sq_h*0.28)),
                        font, fscale, clr, 1, cv2.LINE_AA)

    # status line
    if state == "NO_REF":
        s_txt = "NO REF — press SPACE with pieces in starting position"
        s_col = (0, 200, 255)
    elif state == "WAIT_WHITE":
        s_txt = "WAITING — type white UCI in terminal (e.g. e2e4)"
        s_col = (0, 160, 255)
    elif state == "WATCHING":
        s_txt = "WATCHING — make your BLACK move"
        s_col = (100, 255, 100)
    elif state == "SETTLING":
        pct   = min(still_for / SETTLE_SEC, 1.0)
        bar   = int(pct * 20)
        s_txt = f"SETTLING [{'#'*bar}{'-'*(20-bar)}] {still_for:.1f}/{SETTLE_SEC:.0f}s"
        s_col = (0, 180, 255)
    elif state == "WHITE_MOVING":
        s_txt = "ARM MOVING — Stockfish is playing white..."
        s_col = (0, 220, 255)
    elif state == "COOLDOWN":
        s_txt = f"COOLDOWN — don't touch pieces ({still_for:.0f}/{COOLDOWN_SEC:.0f}s)"
        s_col = (180, 80, 255)
    elif state == "GAME_OVER":
        s_txt = "GAME OVER — press R to reset"
        s_col = (0, 0, 255)
    else:
        s_txt = state
        s_col = (200, 200, 200)

    cv2.putText(display, s_txt, (8, 22), font, 0.52, s_col, 1)
    if last_move:
        cv2.putText(display, f"Last: {last_move}", (8, 46), font, 0.60, (180, 255, 180), 1)
    cv2.putText(display, "SPACE=lock   R=reset   Q=quit   (type UCI in terminal for manual white)",
                (8, display.shape[0]-10), font, 0.42, (140, 140, 140), 1)
    return display


def _piece_name(symbol: str) -> str:
    return {"P": "Pawn", "N": "Knight", "B": "Bishop",
            "R": "Rook",  "Q": "Queen",  "K": "King"}.get(symbol.upper(), symbol)


def record_capture(board: chess.Board, move: chess.Move,
                   white_captured: list, black_captured: list):
    """Call BEFORE board.push(move). Records the captured piece."""
    if not board.is_capture(move):
        return
    if board.is_en_passant(move):
        piece = chess.Piece(chess.PAWN, not board.turn)
    else:
        piece = board.piece_at(move.to_square)
    if piece is None:
        return
    if board.turn == chess.WHITE:
        white_captured.append(piece.symbol().upper())   # white took a black piece
    else:
        black_captured.append(piece.symbol().upper())   # black took a white piece


def check_game_over(board: chess.Board) -> str | None:
    """Return a result string if the game is over, else None."""
    if board.is_checkmate():
        winner = "BLACK" if board.turn == chess.WHITE else "WHITE"
        return f"CHECKMATE — {winner} WINS!"
    if board.is_stalemate():
        return "STALEMATE — Draw!"
    if board.is_insufficient_material():
        return "DRAW — Insufficient material!"
    if board.is_seventyfive_moves():
        return "DRAW — 75-move rule!"
    return None


def print_board(board: chess.Board, last_move: str = "",
                white_captured: list = None, black_captured: list = None,
                result: str = ""):
    """Print full ASCII board + captured pieces + game status."""
    file_labels = "  a b c d e f g h"
    print("\n" + "=" * 36)
    print(file_labels)
    print("  +" + "-+" * 8)
    for rank in range(7, -1, -1):
        row = f"{rank+1} |"
        for file in range(8):
            piece = board.piece_at(chess.square(file, rank))
            row  += f"{piece.symbol() if piece else '.'}|"
        print(row)
        print("  +" + "-+" * 8)
    print(file_labels)

    # captured pieces
    if white_captured:
        fmt = " ".join(_piece_name(p) for p in white_captured)
        print(f"\n  White took : {fmt}")
    if black_captured:
        fmt = " ".join(_piece_name(p) for p in black_captured)
        print(f"  Black took : {fmt}")

    turn = "WHITE" if board.turn == chess.WHITE else "BLACK"
    print(f"\n  Turn   : {turn}  (move {board.fullmove_number})")
    if last_move:
        print(f"  Last   : {last_move}")
    if result:
        print(f"\n  *** {result} ***")
    print(f"  FEN    : {board.fen()}")
    print("=" * 36 + "\n")


def main():
    cap = _open_camera()
    if cap is None:
        print("[ERROR] No camera found.")
        return

    # ── Stockfish ─────────────────────────────────────────────────────────────
    engine = None
    try:
        engine = EngineController()
        print("[ENGINE] Stockfish ready.")
    except Exception as e:
        print(f"[ENGINE] Stockfish not available ({e}). Manual W-key mode active.")

    # ── ARM ───────────────────────────────────────────────────────────────────
    arm = None
    try:
        arm = ArmController()
        print("[ARM] Connected.")
    except Exception as e:
        print(f"[ARM] Not connected ({e}). Moves will be printed only.")

    print("Warming up camera (2s)...")
    t = time.perf_counter()
    while time.perf_counter() - t < 2.0:
        cap.read()

    board          = chess.Board()
    ref_frame      = None
    prev_raw       = None
    still_for      = 0.0
    cooldown_left  = 0.0
    last_move      = ""
    white_captured = []
    black_captured = []
    game_result    = ""

    # States:
    #   NO_REF       → waiting for SPACE to lock
    #   WAIT_WHITE   → Stockfish/manual white move pending
    #   WHITE_MOVING → ARM executing (blocks briefly)
    #   COOLDOWN     → ARM done, refreshing reference for COOLDOWN_SEC before watching
    #   WATCHING     → waiting for black's physical move
    #   SETTLING     → move detected, counting down stillness
    #   GAME_OVER    → checkmate/stalemate
    state = "NO_REF"

    # Background stdin reader (non-blocking, for manual white override)
    cmd_queue: queue.Queue = queue.Queue()

    def _stdin_reader():
        while True:
            try:
                line = input()
                if line.strip():
                    cmd_queue.put(line.strip())
            except EOFError:
                break

    threading.Thread(target=_stdin_reader, daemon=True).start()

    def do_white_move(raw_frame):
        """Ask Stockfish for white's move, execute on ARM, push to board.
        Returns the UCI string, or None on failure."""
        nonlocal last_move, game_result, ref_frame, prev_raw, state

        if engine is None:
            return None   # fall through to manual W key

        # Sync engine board with our board (engine tracks its own state)
        white_uci = engine.get_best_move(time_limit=1.0)
        if white_uci is None:
            return None

        w_move = chess.Move.from_uci(white_uci)
        record_capture(board, w_move, white_captured, black_captured)
        board.push(w_move)
        last_move   = f"W:{white_uci.upper()}"
        game_result = check_game_over(board) or ""

        print(f"\n[STOCKFISH] White plays: {white_uci.upper()}")

        if arm:
            try:
                arm.execute_move(white_uci)
                arm.go_home()
            except Exception as e:
                print(f"  [ARM] Move failed: {e}")
        else:
            print("  (ARM not connected — move physically, then wait for cooldown)")

        print_board(board, last_move, white_captured, black_captured, game_result)
        # Caller must enter COOLDOWN state — do NOT re-lock here.
        return white_uci

    WIN = "Move Detection"
    cv2.namedWindow(WIN, cv2.WINDOW_NORMAL)

    print("\n" + "=" * 36)
    print("  CHESS VISION — FLOW")
    print("=" * 36)
    print("  1. Set up starting position")
    print("     → SPACE in camera window to lock")
    print("  2. Stockfish auto-plays white + ARM moves")
    print("     (or type UCI manually if no Stockfish)")
    print("  3. Make your BLACK move, hold still 5 sec")
    print("  4. Repeat — game runs automatically")
    print("  Camera: SPACE=lock  R=reset  Q=quit")
    print("=" * 36 + "\n")
    print_board(board, white_captured=white_captured, black_captured=black_captured)

    prev_time = time.perf_counter()

    while True:
        ret, raw = cap.read()
        if not ret:
            continue

        now       = time.perf_counter()
        dt        = now - prev_time
        prev_time = now

        # ── manual white UCI from terminal (fallback when no Stockfish) ───────
        try:
            uci_in = cmd_queue.get_nowait().lower()
            if state == "GAME_OVER":
                print(f"  Game over ({game_result}). Press R to reset.\n")
            elif state in ("WAIT_WHITE", "NO_REF"):
                try:
                    board.turn = chess.WHITE
                    move = chess.Move.from_uci(uci_in)
                    if move in board.legal_moves:
                        record_capture(board, move, white_captured, black_captured)
                        board.push(move)
                        if engine:
                            engine.apply_opponent_move(uci_in)
                        last_move   = f"W:{uci_in.upper()}"
                        game_result = check_game_over(board) or ""
                        ref_frame   = raw.copy()
                        prev_raw    = raw.copy()
                        if game_result:
                            state = "GAME_OVER"
                        else:
                            state = "WATCHING"
                        print(f"\n[WHITE manual] {uci_in.upper()}")
                        print_board(board, last_move, white_captured, black_captured, game_result)
                    else:
                        print(f"  '{uci_in}' not a legal white move.\n")
                except Exception:
                    print(f"  Invalid UCI '{uci_in}'. Use format: e2e4\n")
            else:
                print(f"  [ignored] state={state}, not waiting for white.\n")
        except queue.Empty:
            pass

        # ── COOLDOWN: keep refreshing reference after ARM move ────────────────
        if state == "COOLDOWN":
            ref_frame    = raw.copy()
            prev_raw     = raw.copy()
            cooldown_left -= dt
            # reuse still_for as the display counter (counts up from 0)
            still_for    = COOLDOWN_SEC - cooldown_left
            if cooldown_left <= 0:
                state     = "WATCHING"
                still_for = 0.0
                print("[READY] Board locked — make your BLACK move.\n")

        # ── detection (WATCHING / SETTLING only) ─────────────────────────────
        changed     = []
        changed_set = set()
        if state in ("WATCHING", "SETTLING") and ref_frame is not None:
            changed     = get_changed_squares(raw, ref_frame)
            changed_set = set(changed)

        if state == "WATCHING":
            if 2 <= len(changed) <= MAX_CHANGED:
                state     = "SETTLING"
                still_for = 0.0
                print(f"[MOVE STARTED] {changed}")

        elif state == "SETTLING":
            fd = board_still_diff(raw, prev_raw) if prev_raw is not None else 999.0
            still_for = still_for + dt if fd < STILL_DIFF else 0.0

            if still_for >= SETTLE_SEC:
                final = get_changed_squares(raw, ref_frame)

                if 2 <= len(final) <= MAX_CHANGED:
                    board.turn = chess.BLACK
                    uci = identify_move(final, board)
                    if uci:
                        move = chess.Move.from_uci(uci)
                        record_capture(board, move, white_captured, black_captured)
                        board.push(move)
                        if engine:
                            engine.apply_opponent_move(uci)
                        last_move   = f"B:{uci.upper()}"
                        game_result = check_game_over(board) or ""
                        print(f"\n[BLACK] {uci.upper()}")
                        print_board(board, last_move, white_captured, black_captured, game_result)

                        if game_result:
                            state = "GAME_OVER"
                        else:
                            # Auto-play white via Stockfish + ARM
                            state = "WHITE_MOVING"
                            ref_frame = raw.copy()   # temp lock while ARM moves
                            white_result = do_white_move(raw)
                            if white_result is None:
                                # Stockfish unavailable — wait for manual input
                                state = "WAIT_WHITE"
                                print("  → Type white's move in terminal.\n")
                            elif game_result:
                                state = "GAME_OVER"
                            else:
                                state         = "COOLDOWN"
                                cooldown_left = COOLDOWN_SEC
                                still_for     = 0.0
                    else:
                        # Unknown move — piece moved but couldn't identify legally
                        print(f"[UNKNOWN] {final} — going back to WATCHING.\n")
                        state = "WATCHING"   # let black try again
                else:
                    # Noise or piece lifted and returned — go back to WATCHING
                    if len(final) == 0:
                        print("[RETURNED] Piece put back — still your turn.\n")
                    else:
                        print(f"[NOISE] {len(final)} sq — ignored, still your turn.\n")
                    state = "WATCHING"   # black keeps their turn

                ref_frame = raw.copy()
                still_for = 0.0

        prev_raw = raw.copy()

        # ── display ───────────────────────────────────────────────────────────
        display = draw_overlay(raw.copy(), changed_set, state, still_for, last_move)
        cv2.imshow(WIN, display)

        key = cv2.waitKey(1) & 0xFF
        if key == ord("q"):
            break
        if key == ord(" "):
            ref_frame = raw.copy()
            prev_raw  = raw.copy()
            still_for = 0.0
            if engine and state == "NO_REF":
                # First lock — Stockfish plays white's first move automatically
                state = "WHITE_MOVING"
                print("[LOCKED] Starting game — Stockfish plays white first.\n")
                w = do_white_move(raw)
                if game_result:
                    state = "GAME_OVER"
                elif w:
                    state         = "COOLDOWN"
                    cooldown_left = COOLDOWN_SEC
                    still_for     = 0.0
                else:
                    state = "WAIT_WHITE"
            else:
                state = "WAIT_WHITE"
                print("[LOCKED] Reference saved. Type white's UCI or wait for Stockfish.\n")
        if key == ord("r"):
            board          = chess.Board()
            ref_frame      = None
            prev_raw       = None
            state          = "NO_REF"
            still_for      = 0.0
            cooldown_left  = 0.0
            last_move      = ""
            white_captured = []
            black_captured = []
            game_result    = ""
            if engine:
                engine.reset()
            print("[RESET] New game. Press SPACE to lock.\n")
            print_board(board, white_captured=white_captured, black_captured=black_captured)

    if engine:
        engine.close()
    cap.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    main()
