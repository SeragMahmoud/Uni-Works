# Testing And Validation

## Included Validation

| Validation | Command | Purpose |
| --- | --- | --- |
| Servo lookup table | `python src/chess_arm/test_config.py` | Confirms generated square angles are inside servo range. |
| Board ROI calibration | `python src/chess_arm/calibrate_board.py` | Confirms board coordinates from camera view. |
| Manual move execution | `python src/chess_arm/chess_arm_controller.py e2e4` | Exercises a single physical move when hardware is connected. |

## Manual Test Checklist

- Camera opens at the configured index.
- Board ROI lines align with all 64 squares.
- Changed-square highlights match actual piece movement.
- Illegal moves are rejected by `python-chess`.
- Stockfish returns a UCI move.
- Serial commands receive firmware acknowledgements.
- Arm returns to home after movement.

## Test Gaps

No automated hardware-in-the-loop tests are included. A future test harness could replay recorded camera frames and mock the serial controller.
