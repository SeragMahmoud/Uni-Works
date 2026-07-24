# Technical Architecture

## System Context

```mermaid
flowchart LR
    Player[Human player] --> Board[Physical board]
    Board --> Camera[Camera]
    Camera --> PythonApp[Python chess-arm app]
    PythonApp --> Stockfish[Stockfish process]
    PythonApp --> Serial[USB serial]
    Serial --> MCU[Arduino or ESP32 firmware]
    MCU --> ServoDriver[PCA9685 servo driver]
    ServoDriver --> Arm[Robotic arm]
    Arm --> Board
```

## Runtime Components

| Component | Source file | Responsibility |
| --- | --- | --- |
| Move detector | `src/chess_arm/test_moves.py` | Locks a reference frame, compares squares, identifies legal UCI moves, and manages game state. |
| Chess engine wrapper | `src/chess_arm/engine.py` | Starts Stockfish, validates/pushes moves, requests the engine reply, and exposes board status. |
| Robot controller | `src/chess_arm/robot.py` | Sends serial commands and waits for firmware `ACK` responses. |
| Calibration config | `src/chess_arm/config.py` | Stores local runtime config and generates a 64-square servo lookup table. |
| Vision experiment | `src/chess_arm/vision.py` | Uses YOLO/OpenCV detection and maps bounding-box centers to board squares. |
| Dashboard | `src/chess_arm/main.py`, `src/chess_arm/gui.py` | Provides GUI runtime and command-log visibility. |
| Firmware | `firmware/arm_controller/arm_controller.ino` | Runs servo movement and calibration commands on the microcontroller. |

## Move Detection Flow

```mermaid
flowchart TD
    A[Press SPACE with board set] --> B[Store reference frame]
    B --> C[Watch live camera frames]
    C --> D[Compute per-square absolute difference]
    D --> E{2 to MAX_CHANGED squares?}
    E -- No --> C
    E -- Yes --> F[Wait until board is still]
    F --> G[Recompute changed squares]
    G --> H[Search legal UCI moves]
    H --> I{Legal move found?}
    I -- No --> C
    I -- Yes --> J[Push black move]
    J --> K[Ask Stockfish for white move]
    K --> L[Execute arm move]
    L --> M[Cooldown and refresh reference]
    M --> C
```

## Arm Command Flow

The Python `ArmController.execute_move()` method decomposes a UCI move into source and destination squares. Each square has two calibrated poses:

- `hover`: safe travel above the square.
- `ground`: pick/place height at the square.

The movement sequence is source hover, gripper open, source ground, gripper close/hold, source hover, destination hover, destination ground, gripper open, destination hover, home.

## Supported Runtime Modes

| Command | Purpose |
| --- | --- |
| `python src/chess_arm/test_moves.py` | Main physical game workflow. |
| `python src/chess_arm/main.py` | Dashboard mode. |
| `python src/chess_arm/calibrate_board.py` | Camera board ROI calibration. |
| `python src/chess_arm/test_config.py` | Servo lookup-table validation. |
| `python src/chess_arm/chess_arm_controller.py e2e4` | Direct arm-move test. |
