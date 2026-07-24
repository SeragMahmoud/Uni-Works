# Chess ARM Architecture

Chess ARM is organized as a modular robotics pipeline:

```text
Camera -> Vision/Move Detection -> Chess Board State -> Stockfish -> Arm Controller -> Firmware -> Servos
```

## Main Components

| Component | Location | Role |
| --- | --- | --- |
| Move detector | `src/chess_arm/test_moves.py` | Detects changed board squares and identifies legal UCI moves. |
| Vision detector | `src/chess_arm/vision.py` | Runs YOLO detection and maps piece detections to chess squares. |
| Engine controller | `src/chess_arm/engine.py` | Manages Stockfish and `python-chess` board state. |
| Arm controller | `src/chess_arm/robot.py` | Sends serial commands for pick-and-place moves. |
| Dashboard | `src/chess_arm/main.py`, `src/chess_arm/gui.py` | Displays live camera feed, logs, and status. |
| Firmware | `firmware/arm_controller/arm_controller.ino` | Converts commands into smooth servo motion. |

## Runtime Data Flow

1. The camera captures the board.
2. Vision logic detects movement or pieces.
3. The software converts square changes into chess notation.
4. `python-chess` validates the move.
5. Stockfish chooses the strongest reply.
6. The arm controller converts UCI notation into source and destination squares.
7. The servo lookup table provides hover and ground positions.
8. Firmware executes the physical movement sequence.

