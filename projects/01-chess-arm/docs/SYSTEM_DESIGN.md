# System Design

## Design Goals

- Keep chess legality separate from vision heuristics.
- Keep hardware movement behind a small serial controller API.
- Make board and servo calibration explicit.
- Support no-hardware review through source-level inspection.

## State Model

`test_moves.py` uses runtime states such as `NO_REF`, `WAIT_WHITE`, `WATCHING`, `SETTLING`, `WHITE_MOVING`, `COOLDOWN`, and `GAME_OVER`.

## Data Contracts

| Data | Format | Producer | Consumer |
| --- | --- | --- | --- |
| Board frame | OpenCV image array | Camera | Move detector |
| Changed squares | List of algebraic squares | Move detector | Move identifier |
| Move | UCI string such as `e2e4` | Move identifier/Stockfish | Board and robot controller |
| Servo pose | Tuple of angles | Config lookup table | Robot controller |
| Serial command | Text command | Robot controller | Firmware |
