# Security And Privacy

## Sanitization Applied

- The original Stockfish absolute path was replaced with `CHESS_ARM_STOCKFISH_PATH`.
- The original serial port was replaced with `CHESS_ARM_SERIAL_PORT`.
- `.env` files are excluded and represented only by `.env.example`.
- YOLO weights, local generated caches, and private hardware files are not included.

## Hardware Safety

Robotic-arm testing should be performed with:

- Stable power supply.
- Clear workspace around the board.
- Low-speed calibration moves before full pick/place sequences.
- Emergency power-off access.

## Data Handling

The project does not require user accounts, credentials, or persistent personal data. Camera frames are processed locally in memory.
