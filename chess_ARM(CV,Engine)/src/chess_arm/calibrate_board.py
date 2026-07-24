"""
calibrate_board.py  -  Identify the chess board region in the camera frame.

Usage:
    python calibrate_board.py

Controls:
    Left-click   : place a corner point (need exactly 2: top-left, bottom-right)
    r            : reset points and start over
    q            : quit

After clicking both corners the BOARD_ROI value is printed.
Copy it into config.py:
    BOARD_ROI = (x1, y1, x2, y2)
"""

import cv2

CAMERA_INDEX = None   # None = auto-scan, or set to 0, 1, 2 …

_pts: list[tuple[int, int]] = []


def _on_mouse(event, x, y, flags, param):
    if event != cv2.EVENT_LBUTTONDOWN:
        return
    if len(_pts) >= 2:
        return
    _pts.append((x, y))
    print(f"  Point {len(_pts)}: ({x}, {y})")
    if len(_pts) == 2:
        roi = (_pts[0][0], _pts[0][1], _pts[1][0], _pts[1][1])
        print(f"\n  Paste this into config.py:")
        print(f"  BOARD_ROI = {roi}\n")


def _open_camera(index):
    """Open at the same resolution as test_vision.py so ROI coords match exactly."""
    for backend, bname in [(cv2.CAP_MSMF, "MSMF"), (cv2.CAP_DSHOW, "DSHOW")]:
        candidates = [index] if index is not None else range(5)
        for i in candidates:
            cap = cv2.VideoCapture(i, backend)
            if not cap.isOpened():
                cap.release()
                continue
            cap.set(cv2.CAP_PROP_FOURCC, cv2.VideoWriter_fourcc(*"MJPG"))
            cap.set(cv2.CAP_PROP_FRAME_WIDTH,  1280)
            cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)
            cap.set(cv2.CAP_PROP_FPS, 60)
            ret, frame = cap.read()
            if ret:
                h, w = frame.shape[:2]
                print(f"[CV] Camera {i} ({bname}): {w}x{h}")
                return cap
            cap.release()
    return None


def main():
    cap = _open_camera(CAMERA_INDEX)
    if cap is None:
        print("[ERROR] No camera found. Set CAMERA_INDEX at the top of this file.")
        return

    WIN = "Board Calibration"
    cv2.namedWindow(WIN)
    cv2.setMouseCallback(WIN, _on_mouse)

    print("Click the TOP-LEFT corner of the chess board.")
    print("Then click the BOTTOM-RIGHT corner.")
    print("Press 'r' to reset, 'q' to quit.\n")

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # Draw clicked points
        for p in _pts:
            cv2.circle(frame, p, 6, (0, 80, 255), -1)

        # Draw the selected rectangle
        if len(_pts) == 2:
            cv2.rectangle(frame, _pts[0], _pts[1], (0, 255, 0), 2)
            # Also draw an 8x8 preview grid inside the rect
            x1, y1 = _pts[0]
            x2, y2 = _pts[1]
            bw, bh  = x2 - x1, y2 - y1
            for i in range(1, 8):
                lx = x1 + int(i * bw / 8)
                ly = y1 + int(i * bh / 8)
                cv2.line(frame, (lx, y1), (lx, y2), (0, 200, 0), 1)
                cv2.line(frame, (x1, ly), (x2, ly), (0, 200, 0), 1)

        # Instructions overlay
        hint = "Click TL corner" if len(_pts) == 0 else (
               "Click BR corner" if len(_pts) == 1 else
               "r=reset  q=quit")
        cv2.putText(frame, hint, (10, 25),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)

        cv2.imshow(WIN, frame)
        key = cv2.waitKey(1) & 0xFF
        if key == ord("q"):
            break
        if key == ord("r"):
            _pts.clear()
            print("  Points reset.\n")

    cap.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    main()
