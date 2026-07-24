# Chess ARM - Robotic Chess Player

Chess ARM is a complete AI robotics project where a camera observes a physical chess board, Stockfish selects intelligent moves, and a servo-controlled robotic arm executes those moves on the board.

The project combines computer vision, chess engine integration, serial communication, embedded firmware, servo calibration, and a live control dashboard into one polished physical AI system.

## Repository Structure

| Folder | Purpose |
| --- | --- |
| [src/](src/README.md) | Python source code for computer vision, chess engine control, GUI, calibration, and robotic-arm orchestration. |
| [firmware/](firmware/README.md) | Arduino/ESP32 firmware for servo control, calibration commands, WiFi access point, and web-based arm tuning. |
| [docs/](docs/README.md) | Technical documentation for setup, architecture, calibration, and operating workflow. |
| [diagrams/](diagrams/README.md) | Architecture and process diagrams for the system. |
| [demo/](demo/README.md) | Demo videos, screenshots, and presentation-ready media. |
| [assets/](assets/README.md) | Images and visual assets used in documentation or demos. |
| [models/](models/README.md) | Computer-vision model assets used by the YOLO-based detection mode. |
| [reports/](reports/README.md) | Technical reports, experiment summaries, and evaluation material. |
| [templates/](templates/README.md) | Reusable calibration and documentation templates. |
| [archive/](archive/README.md) | Preserved historical or supporting material. |

## Technology Stack

| Area | Technologies |
| --- | --- |
| Programming | Python, Arduino C/C++ |
| Computer vision | OpenCV, NumPy, YOLO through Ultralytics |
| Chess intelligence | Stockfish, `python-chess` |
| Robotics interface | PySerial, USB serial |
| GUI | Tkinter, Pillow, OpenCV display windows |
| Embedded control | ESP32/Arduino, Adafruit PCA9685 PWM servo driver |

## Core Capabilities

- Detects real chess-board movement using camera input.
- Converts board-square changes into UCI chess moves.
- Validates moves through `python-chess`.
- Generates strong engine responses through Stockfish.
- Sends physical movement commands to the robotic arm.
- Controls base, shoulder, elbow, wrist, wrist rotation, and gripper servos.
- Provides calibration tools for board ROI and servo positions.
- Includes a dashboard mode with camera feed, command log, and system status.
- Includes embedded WiFi calibration controls through the firmware.

## Quick Start

Install Python dependencies:

```bash
pip install -r requirements.txt
```

Configure hardware and engine paths in:

```text
src/chess_arm/config.py
```

Run the main physical chess workflow:

```bash
python src/chess_arm/test_moves.py
```

Run the dashboard workflow:

```bash
python src/chess_arm/main.py
```

Validate all generated square angles:

```bash
python src/chess_arm/test_config.py
```

Calibrate the board camera region:

```bash
python src/chess_arm/calibrate_board.py
```

## Main Runtime Flow

1. The camera locks a reference view of the starting board.
2. Stockfish plays the robot's move.
3. The robotic arm performs the physical move.
4. The board reference is refreshed.
5. The human player makes a move.
6. The vision module detects changed squares.
7. The move is validated as a legal chess move.
8. Stockfish calculates the next response.
9. The robotic arm executes the reply.

## Firmware

The firmware is located at:

```text
firmware/arm_controller/arm_controller.ino
```

It supports serial commands, smooth servo movement, safe home positioning, gripper control, mini-board movement, inverse-kinematics helpers, and a browser-based calibration page served from the ESP32 access point.

## Portfolio Value

Chess ARM is a standout AI robotics project. It demonstrates real-world integration across vision, embedded systems, chess AI, mechanical calibration, and physical automation.
