# Setup

## Python

Install Python 3.10 or later, then install the required packages:

```bash
pip install -r requirements.txt
```

## Stockfish

Download Stockfish and set the executable path in:

```text
src/chess_arm/config.py
```

## Arduino or ESP32

Open this sketch in the Arduino IDE:

```text
firmware/arm_controller/arm_controller.ino
```

Upload it to the board, then set the serial port in:

```text
src/chess_arm/config.py
```

