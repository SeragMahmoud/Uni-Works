# Chess ARM Python Modules

| File | Purpose |
| --- | --- |
| `test_moves.py` | Main physical game workflow using camera frame differences. |
| `main.py` | Dashboard runtime that connects vision, engine, and arm modules. |
| `engine.py` | Stockfish and `python-chess` integration. |
| `robot.py` | Serial command interface for the robotic arm. |
| `vision.py` | YOLO/OpenCV board and piece detection support. |
| `gui.py` | Tkinter dashboard UI. |
| `config.py` | Engine path, serial settings, board ROI, and servo calibration. |
| `calibrate_board.py` | Interactive camera ROI calibration. |
| `test_config.py` | Servo lookup-table validation. |
| `chess_arm_controller.py` | Direct inverse-kinematics and movement control helper. |

