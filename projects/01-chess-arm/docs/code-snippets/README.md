# Code Snippets

This folder documents the selected public implementation evidence already included under `src/` and `firmware/`.

| Public file | Original relative source | Purpose | Sanitization |
| --- | --- | --- | --- |
| `src/chess_arm/engine.py` | `engine.py` | Stockfish wrapper and legal move state management. | No secret values present. |
| `src/chess_arm/robot.py` | `robot.py` | Serial command protocol and pick/place sequencing. | No secret values present. |
| `src/chess_arm/test_moves.py` | `test_moves.py` | Frame-difference move detector and runtime state machine. | No secret values present. |
| `src/chess_arm/config.py` | `config.py` | Runtime config and generated servo lookup table. | Local Stockfish path and serial port replaced with environment variables. |
| `firmware/arm_controller/arm_controller.ino` | `arm_controller.ino` | Embedded servo-control firmware. | No credential material copied. |

These are source excerpts from the real project rather than fabricated sample code.
