# Demo Review Guide

## Full Hardware Demo

1. Configure Stockfish and serial environment variables.
2. Calibrate the board ROI with `python src/chess_arm/calibrate_board.py`.
3. Validate servo ranges with `python src/chess_arm/test_config.py`.
4. Upload `firmware/arm_controller/arm_controller.ino` to the controller.
5. Run `python src/chess_arm/test_moves.py`.
6. Press `SPACE`, allow Stockfish to move white, then make black moves physically.

## No-Hardware Demo

Without the arm or camera, review:

- `src/chess_arm/test_moves.py` for detection and state flow.
- `src/chess_arm/engine.py` for Stockfish integration.
- `src/chess_arm/robot.py` for motion sequencing.
- `docs/TECHNICAL_ARCHITECTURE.md` for diagrams.

No live hardware video is included in this public portfolio copy.
