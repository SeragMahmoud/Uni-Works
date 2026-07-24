# Chess ARM - Robotic Chess Player

## Overview

Chess ARM is a physical AI robotics project that combines camera-based chess-board detection, Stockfish chess decisions, and servo-arm movement. A human can play on a real board while the software detects moves, validates chess state, asks Stockfish for a reply, and sends movement commands to an Arduino/ESP32-controlled arm.

## Purpose

The project demonstrates end-to-end integration between computer vision, game AI, serial communication, embedded firmware, and calibrated robotic motion.

## Main Features

- Camera-based board monitoring with OpenCV.
- Frame-difference move detection for physical chess moves.
- Optional YOLO-based piece detection module.
- Legal move validation through `python-chess`.
- Stockfish engine integration.
- Servo lookup table for 64 chess squares.
- Arduino/ESP32 firmware for arm movement and calibration.
- Tkinter dashboard with camera feed, logs, and status.

## Technology Stack

Python, OpenCV, NumPy, Ultralytics YOLO, `python-chess`, Stockfish, PySerial, Tkinter, Pillow, Arduino/ESP32 C++, and Adafruit PCA9685 servo control.

## Architecture

```text
Camera -> Vision/Move Detection -> python-chess -> Stockfish -> Serial Arm Controller -> Firmware -> Servos
```

| Folder | Purpose |
| --- | --- |
| `src/chess_arm` | Python source for vision, engine integration, GUI, calibration, and arm commands. |
| `firmware/arm_controller` | Arduino/ESP32 firmware for servo and calibration control. |
| `docs` | Architecture, setup, calibration, and operation notes. |
| `models` | Placeholder for local YOLO model files, which are not committed. |

## Prerequisites

- Python 3.10 or later.
- Stockfish installed locally.
- Webcam.
- Arduino/ESP32 connected by serial for physical arm control.
- Servo arm hardware and PCA9685 driver for the embedded workflow.

## Installation

```bash
pip install -r requirements.txt
```

## Environment Variables

Copy `.env.example` as a local reference and set these values in your shell or local environment:

| Variable | Purpose |
| --- | --- |
| `CHESS_ARM_STOCKFISH_PATH` | Path or executable name for Stockfish. |
| `CHESS_ARM_SERIAL_PORT` | Serial port for the Arduino/ESP32 arm controller. |
| `CHESS_ARM_BAUD_RATE` | Serial baud rate, usually `115200`. |

## Run Command

Main physical chess workflow:

```bash
python src/chess_arm/test_moves.py
```

Dashboard workflow:

```bash
python src/chess_arm/main.py
```

## Build Command

No Python build step is required.

## Test Command

Validate the servo lookup table:

```bash
python src/chess_arm/test_config.py
```

## API Endpoints

Not applicable. This is a local robotics and desktop-control project.

## Screenshots

No public screenshots are committed in this sanitized source publish.

## Known Limitations

- Full operation requires physical camera and robotic-arm hardware.
- YOLO model weights are intentionally excluded and should be supplied locally.
- Stockfish must be installed locally or available on the system path.

## Possible Future Improvements

- Add a portable hardware setup checklist with photos.
- Add automated simulation tests for move-detection logic.
- Add sample calibration profiles for different board sizes.

## Security And Configuration Notes

No real local paths, `.env` files, model weights, credentials, or private hardware configuration files are committed. Use environment variables for machine-specific setup.
