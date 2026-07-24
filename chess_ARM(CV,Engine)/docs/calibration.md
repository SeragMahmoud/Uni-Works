# Calibration

Chess ARM uses calibration for both camera geometry and servo motion.

## Board ROI

Run:

```bash
python src/chess_arm/calibrate_board.py
```

Select the top-left and bottom-right corners of the chess board, then copy the printed ROI into `src/chess_arm/config.py`.

## Servo Lookup Table

The square lookup table is generated from calibration constants in `config.py`.

Run:

```bash
python src/chess_arm/test_config.py
```

This validates all 64 squares and confirms the hover and ground positions for each square.

