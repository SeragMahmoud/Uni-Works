# Project Overview

Chess ARM connects software intelligence with a physical chess board. The original project combines OpenCV board observation, `python-chess` move legality, Stockfish engine play, serial command coordination, and servo firmware.

## Portfolio Value

The project is valuable because it crosses several engineering boundaries at once:

| Area | Evidence in this repository |
| --- | --- |
| Computer vision | Frame-difference move detection and optional YOLO square mapping. |
| AI/game logic | Stockfish integration through `python-chess`. |
| Embedded systems | Arduino/ESP32 firmware for servo control. |
| Robotics | Calibrated square-to-servo lookup table and pick/place command sequence. |
| Human interaction | Camera window controls, cooldown handling, and dashboard mode. |

## Evidence Sources

- Original source modules: `test_moves.py`, `engine.py`, `robot.py`, `vision.py`, `config.py`, `gui.py`, `main.py`, `calibrate_board.py`, `test_config.py`.
- Original firmware: `arm_controller.ino`.
- Workspace technical description: Chess ARM section.

## Publication Boundary

This public version includes source excerpts that demonstrate the architecture. It excludes machine-specific paths, local `.env` files, model weights, generated caches, and hardware-specific private calibration files.
