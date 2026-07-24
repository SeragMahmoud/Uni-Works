"""
Chess Robot Arm Controller
==========================
Usage:
  python chess_arm_controller.py           # interactive input mode
  python chess_arm_controller.py e2e4      # single move from CLI arg

Sends joint angles over Serial to ESP32.
Edit CALIBRATION section before first run.
"""

import serial
import time
import math
import sys

# ─────────────────────────────────────────────
# SERIAL CONFIG
# ─────────────────────────────────────────────
SERIAL_PORT = "COM3"        # Windows: "COM3" | Linux/Mac: "/dev/ttyUSB0"
BAUD_RATE   = 115200

# ─────────────────────────────────────────────
# ARM GEOMETRY (cm)
# ─────────────────────────────────────────────
BASE_H = 3.0    # height of base cylinder (ground → shoulder pivot)
L2     = 14.0   # shoulder → elbow
L3     = 10.0   # elbow → wrist
L4     = 13.0   # wrist → gripper tip (includes wrist servos + gripper)

# ─────────────────────────────────────────────
# CALIBRATION  ← tune these with trial & error
# ─────────────────────────────────────────────
BOARD_OFFSET_Y  = 8.5   # cm from shoulder center to rank-1 edge
BOARD_CENTER_X  = 0.0   # lateral offset correction (+ = shift right, - = left)
SQUARE_SIZE     = 2.5   # cm per square (20cm / 8)

Z_HIGH  = BASE_H + 12.0  # safe travel height (above all pieces)
Z_GRIP  = BASE_H + 1.5   # height to grab/place a piece
Z_PLACE = BASE_H + 1.5   # same as grip unless pieces vary

# Servo angle offsets per joint (tune if arm isn't straight at 0°)
# Format: degrees to ADD to calculated angle before sending
SERVO_OFFSET = {
    "J1": 90,  # base rotation — 90° = facing board, 180° = full left (A-file), 0° = full right (H-file)
    "J2": 0,   # shoulder
    "J3": 0,   # elbow
    "J4": 0,   # wrist pitch (keep gripper pointing down)
    "J5": 0,   # wrist roll
    "J6": 0,   # gripper (0=open, 1=close handled separately)
}

GRIPPER_OPEN  = 30   # degrees
GRIPPER_CLOSE = 90   # degrees

# Movement speed — delay between serial commands (seconds)
MOVE_DELAY = 0.8

# ─────────────────────────────────────────────
# COORDINATE MATH
# ─────────────────────────────────────────────
def square_to_xy(square: str):
    """
    Convert chess notation to physical XY coords (cm).
    Origin = shoulder center projected on ground.
    X = lateral (left/right), Y = forward (toward board)
    """
    square = square.lower().strip()
    col = ord(square[0]) - ord('a')   # a=0 .. h=7
    row = int(square[1]) - 1          # 1=0 .. 8=7

    x = (col * SQUARE_SIZE + SQUARE_SIZE / 2) - (4 * SQUARE_SIZE) + BOARD_CENTER_X
    y = BOARD_OFFSET_Y + (row * SQUARE_SIZE + SQUARE_SIZE / 2)

    return round(x, 3), round(y, 3)


# ─────────────────────────────────────────────
# INVERSE KINEMATICS
# ─────────────────────────────────────────────
def ik_solve(x: float, y_fwd: float, z: float):
    """
    Solve IK for target position (x, y_fwd, z).
    x      = lateral offset from shoulder (cm)
    y_fwd  = forward distance from shoulder (cm)
    z      = height (cm)

    Returns dict of joint angles in degrees, or None if unreachable.

    Arm layout:
        J1 (base rotation) → rotates whole arm left/right
        J2 (shoulder)      → lifts arm up
        J3 (elbow)         → bends arm
        J4 (wrist pitch)   → keeps gripper pointing DOWN (compensates J2+J3)
    """

    # J1: base rotation from lateral offset
    j1 = math.degrees(math.atan2(x, y_fwd))

    # Reach in the arm's vertical plane
    reach  = math.sqrt(x**2 + y_fwd**2)   # horizontal distance
    height = z - BASE_H                    # height above shoulder

    # Distance from shoulder to target
    D = math.sqrt(reach**2 + height**2)

    # Check reachability
    max_reach = L2 + L3 + L4
    if D > max_reach:
        print(f"  [!] Target unreachable — D={D:.1f}cm > max={max_reach:.1f}cm")
        return None

    # Angle to target from horizontal
    alpha = math.atan2(height, reach)

    # Law of cosines for elbow
    # We treat L2 and (L3+L4) as the two arm segments for simplicity
    # L4 is treated as rigid extension of L3 in this 2-segment IK
    # For full 3-segment IK, see note below
    reach2 = L3 + L4   # treat forearm+wrist as one segment
    cos_j3 = (D**2 - L2**2 - reach2**2) / (2 * L2 * reach2)
    cos_j3 = max(-1, min(1, cos_j3))   # clamp for float errors
    j3 = -math.degrees(math.acos(cos_j3))  # negative = elbow bends down

    beta = math.atan2(reach2 * math.sin(math.radians(-j3)),
                      L2 + reach2 * math.cos(math.radians(-j3)))
    j2 = math.degrees(alpha - beta)

    # J4: wrist pitch — compensate to keep gripper pointing straight down
    j4 = -(j2 + j3)

    # Apply servo offsets
    angles = {
        "J1": round(j1  + SERVO_OFFSET["J1"], 1),
        "J2": round(j2  + SERVO_OFFSET["J2"], 1),
        "J3": round(j3  + SERVO_OFFSET["J3"], 1),
        "J4": round(j4  + SERVO_OFFSET["J4"], 1),
        "J5": round(0   + SERVO_OFFSET["J5"], 1),   # wrist roll = neutral
        "J6": GRIPPER_OPEN,
    }
    return angles


# ─────────────────────────────────────────────
# SERIAL COMMUNICATION
# ─────────────────────────────────────────────
def send_angles(ser, angles: dict, label: str = ""):
    """
    Format: J1:45.0,J2:30.0,J3:-50.0,J4:20.0,J5:0.0,J6:30\n
    """
    cmd = ",".join(f"{k}:{v}" for k, v in angles.items()) + "\n"
    print(f"  → [{label}] {cmd.strip()}")
    if ser:
        ser.write(cmd.encode())
        time.sleep(MOVE_DELAY)

def send_gripper(ser, state: str):
    """state = 'open' or 'close'"""
    val = GRIPPER_OPEN if state == "open" else GRIPPER_CLOSE
    cmd = f"J1:_,J2:_,J3:_,J4:_,J5:_,J6:{val}\n"
    # Simpler: just send gripper command
    cmd = f"GRIP:{val}\n"
    print(f"  → [gripper {state}] {cmd.strip()}")
    if ser:
        ser.write(cmd.encode())
        time.sleep(0.4)


# ─────────────────────────────────────────────
# MOVE SEQUENCE
# ─────────────────────────────────────────────
def move_piece(ser, src: str, dst: str):
    """
    Full move sequence for one chess move.
    src, dst = square strings like 'e2', 'e4'
    """
    print(f"\n{'='*40}")
    print(f"  MOVE: {src.upper()} → {dst.upper()}")
    print(f"{'='*40}")

    src_x, src_y = square_to_xy(src)
    dst_x, dst_y = square_to_xy(dst)

    print(f"  SRC physical: x={src_x}cm  y={src_y}cm")
    print(f"  DST physical: x={dst_x}cm  y={dst_y}cm")

    # 1. Move to above source square (safe height)
    a = ik_solve(src_x, src_y, Z_HIGH)
    if not a: return
    send_angles(ser, a, f"hover {src.upper()}")

    # 2. Descend to grip height
    a = ik_solve(src_x, src_y, Z_GRIP)
    if not a: return
    send_angles(ser, a, f"descend {src.upper()}")

    # 3. Close gripper
    send_gripper(ser, "close")

    # 4. Lift back to safe height
    a = ik_solve(src_x, src_y, Z_HIGH)
    if not a: return
    send_angles(ser, a, f"lift from {src.upper()}")

    # 5. Move to above destination (still at safe height)
    a = ik_solve(dst_x, dst_y, Z_HIGH)
    if not a: return
    send_angles(ser, a, f"hover {dst.upper()}")

    # 6. Descend to place height
    a = ik_solve(dst_x, dst_y, Z_PLACE)
    if not a: return
    send_angles(ser, a, f"descend {dst.upper()}")

    # 7. Open gripper — release piece
    send_gripper(ser, "open")

    # 8. Lift back to safe height
    a = ik_solve(dst_x, dst_y, Z_HIGH)
    if not a: return
    send_angles(ser, a, f"retract from {dst.upper()}")

    print(f"\n  ✓ Move complete\n")


# ─────────────────────────────────────────────
# CALIBRATION HELPER
# ─────────────────────────────────────────────
def calibrate_mode(ser):
    """
    Type any square (e.g. 'a1') to move arm there at Z_HIGH.
    Type 'down' to descend, 'up' to rise.
    Type 'open'/'close' to test gripper.
    Type 'q' to quit.
    """
    print("\n[CALIBRATION MODE]")
    print("  Enter square (e.g. a1, h8) to hover over it")
    print("  'down' → descend  |  'up' → rise  |  'open'/'close' → gripper")
    print("  'q' → quit\n")

    last_x, last_y = 0, BOARD_OFFSET_Y

    while True:
        cmd = input("  > ").strip().lower()
        if cmd == 'q':
            break
        elif cmd == 'down':
            a = ik_solve(last_x, last_y, Z_GRIP)
            if a: send_angles(ser, a, "manual down")
        elif cmd == 'up':
            a = ik_solve(last_x, last_y, Z_HIGH)
            if a: send_angles(ser, a, "manual up")
        elif cmd in ('open', 'close'):
            send_gripper(ser, cmd)
        elif len(cmd) == 2 and cmd[0].isalpha() and cmd[1].isdigit():
            last_x, last_y = square_to_xy(cmd)
            a = ik_solve(last_x, last_y, Z_HIGH)
            if a: send_angles(ser, a, f"hover {cmd.upper()}")
        else:
            print("  Unknown command")


# ─────────────────────────────────────────────
# MAIN
# ─────────────────────────────────────────────
def main():
    # Try to open serial — run in dry mode if port not found
    ser = None
    try:
        ser = serial.Serial(SERIAL_PORT, BAUD_RATE, timeout=2)
        time.sleep(2)   # ESP32 reboot delay
        print(f"[OK] Connected to {SERIAL_PORT}")
    except Exception as e:
        print(f"[WARN] Serial not connected: {e}")
        print("[INFO] Running in DRY mode — angles printed but not sent\n")

    # CLI arg: single move
    if len(sys.argv) == 2:
        move = sys.argv[1].strip().lower()
        if len(move) == 4:
            move_piece(ser, move[:2], move[2:])
        else:
            print("Usage: python chess_arm_controller.py e2e4")
        return

    # Interactive mode
    print("\nChess Robot Controller")
    print("  Enter move (e.g. e2e4) to execute")
    print("  Enter 'cal' for calibration mode")
    print("  Enter 'q' to quit\n")

    while True:
        cmd = input("Move > ").strip().lower()
        if cmd == 'q':
            break
        elif cmd == 'cal':
            calibrate_mode(ser)
        elif len(cmd) == 4 and cmd[:2].isalpha() and cmd[2:4][0].isalpha():
            move_piece(ser, cmd[:2], cmd[2:])
        else:
            print("  Invalid — enter move like e2e4 or 'cal'")

    if ser:
        ser.close()
    print("Done.")


if __name__ == "__main__":
    main()
