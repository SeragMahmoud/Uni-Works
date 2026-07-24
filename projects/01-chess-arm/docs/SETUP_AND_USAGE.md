# Setup And Usage

## Prerequisites

- Python 3.10 or later.
- Stockfish installed locally.
- Webcam mounted with a stable view of the board.
- Arduino or ESP32-compatible controller connected over USB serial.
- Servo arm hardware with calibrated base, shoulder, elbow, wrist, wrist rotation, and gripper channels.

## Install Python Dependencies

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

Windows PowerShell:

```powershell
py -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

## Configure Local Environment

Use `.env.example` as a placeholder guide.

```bash
CHESS_ARM_STOCKFISH_PATH=stockfish
CHESS_ARM_SERIAL_PORT=your_serial_port_here
CHESS_ARM_BAUD_RATE=115200
```

The source reads these values from environment variables. Do not commit local paths or device names.

## Board Calibration

1. Run `python src/chess_arm/calibrate_board.py`.
2. Click the top-left and bottom-right board corners.
3. Transfer the printed ROI tuple into `src/chess_arm/config.py` or the active local config.
4. Keep camera placement fixed after calibration.

## Servo Calibration

Tune these values in `src/chess_arm/config.py` for the physical arm:

| Constant | Meaning |
| --- | --- |
| `BASE_MIN` / `BASE_MAX` | Base angle range from file `a` to file `h`. |
| `S_NEAR` / `S_FAR` | Shoulder range from rank 1 to rank 8. |
| `E_NEAR` / `E_FAR` | Elbow range from rank 1 to rank 8. |
| `HOVER_S_OFFSET` / `HOVER_E_OFFSET` | Safe hover adjustment above a square. |

Run the validation script before moving hardware:

```bash
python src/chess_arm/test_config.py
```

## Run The Game

```bash
python src/chess_arm/test_moves.py
```

Expected review flow:

1. Place pieces in the starting position.
2. Press `SPACE` to lock the reference frame.
3. Let Stockfish choose the first white move, or type a manual UCI move in the terminal when Stockfish is unavailable.
4. Make the black physical move.
5. Hold the board still until detection settles.
6. Allow the arm move and cooldown to complete before touching the board again.

## No-Hardware Review

Without hardware, reviewers can still inspect:

- `engine.py` for Stockfish integration.
- `test_moves.py` for board-diff detection and game-state flow.
- `robot.py` for serial command sequencing.
- `config.py` for square-to-servo lookup-table generation.
- `firmware/arm_controller/arm_controller.ino` for embedded control.
