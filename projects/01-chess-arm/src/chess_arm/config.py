import os

STOCKFISH_PATH = os.getenv("CHESS_ARM_STOCKFISH_PATH", "stockfish")

SERIAL_PORT   = os.getenv("CHESS_ARM_SERIAL_PORT", "")
BAUD_RATE     = int(os.getenv("CHESS_ARM_BAUD_RATE", "115200"))
SERIAL_TIMEOUT = 10

# Board region of interest in the camera frame.
# Set to (x1, y1, x2, y2) pixel coordinates of the board's top-left and
# bottom-right corners.  None = use the full camera frame (less accurate).
# Run  python calibrate_board.py  to find the right values interactively.
BOARD_ROI = (346, 97, 934, 651)   # (top-left X, top-left Y, bottom-right X, bottom-right Y)

HOME_ANGLES = {"B": 90, "S": 90, "E": 90}

# ---------------------------------------------------------------------------
# LUT generation
# ---------------------------------------------------------------------------
# Geometry model (adjust these 6 constants after physical calibration):
#
#   BASE_MIN / BASE_MAX  : base rotation for file 'a' and file 'h'  [degrees]
#   S_NEAR / S_FAR       : shoulder angle at rank-1 (near) and rank-8 (far)
#   E_NEAR / E_FAR       : elbow   angle at rank-1 (near) and rank-8 (far)
#   HOVER_S_OFFSET       : how many degrees to subtract from shoulder for hover
#   HOVER_E_OFFSET       : how many degrees to add to elbow for hover (raises arm)
#
# All values are in degrees (0-180, servo range).

BASE_MIN      = 40    # base angle pointing at file 'a'
BASE_MAX      = 140   # base angle pointing at file 'h'

S_NEAR        = 60    # shoulder at rank 1 (closest row to arm)
S_FAR         = 85    # shoulder at rank 8 (farthest row from arm)

E_NEAR        = 110   # elbow at rank 1
E_FAR         = 70    # elbow at rank 8

HOVER_S_OFFSET = 12   # raise arm by pulling shoulder back
HOVER_E_OFFSET = 15   # raise arm by extending elbow upward


def _generate_lut() -> dict:
    lut = {}
    files = "abcdefgh"
    for fi, f in enumerate(files):
        base = round(BASE_MIN + fi * (BASE_MAX - BASE_MIN) / 7)
        for rank in range(1, 9):
            ri = rank - 1
            t  = ri / 7.0                          # 0.0 (rank1) → 1.0 (rank8)
            s_gnd = round(S_NEAR + t * (S_FAR  - S_NEAR))
            e_gnd = round(E_NEAR + t * (E_FAR  - E_NEAR))
            s_hov = s_gnd - HOVER_S_OFFSET
            e_hov = e_gnd + HOVER_E_OFFSET
            lut[f"{f}{rank}"] = {
                "hover":  (base, s_hov, e_hov),
                "ground": (base, s_gnd, e_gnd),
            }
    return lut


SQUARE_LUT = _generate_lut()
