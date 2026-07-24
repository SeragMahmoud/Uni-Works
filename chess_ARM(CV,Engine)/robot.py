import time
import serial
from config import SERIAL_PORT, BAUD_RATE, SERIAL_TIMEOUT, HOME_ANGLES, SQUARE_LUT


class ArmController:
    ACK = "ACK"
    GRIPPER_OPEN  = 180
    GRIPPER_CLOSE = 0

    def __init__(self, port: str = SERIAL_PORT, baud: int = BAUD_RATE,
                 on_command=None):
        self.ser = serial.Serial(port, baud, timeout=SERIAL_TIMEOUT)
        self._on_command = on_command   # optional callback(cmd: str)
        time.sleep(2)  # wait for Arduino reset after serial connect
        self.ser.reset_input_buffer()

    def send_command(self, cmd: str) -> str:
        """Send a single command and block until Arduino replies ACK."""
        if self._on_command:
            self._on_command(cmd)
        payload = (cmd.strip() + "\n").encode()
        self.ser.write(payload)
        deadline = time.time() + SERIAL_TIMEOUT
        while time.time() < deadline:
            line = self.ser.readline().decode(errors="ignore").strip()
            if line == self.ACK:
                return self.ACK
            if line:
                print(f"[Arduino] {line}")
        raise TimeoutError(f"No ACK received for command: {cmd!r}")

    def _move_angles(self, b: int, s: int, e: int):
        self.send_command(f"B{b}")
        self.send_command(f"S{s}")
        self.send_command(f"E{e}")

    def _gripper(self, angle: int, hold: bool = False):
        self.send_command(f"G{angle}")
        if hold:
            self.send_command("GH")

    def go_home(self):
        self._move_angles(HOME_ANGLES["B"], HOME_ANGLES["S"], HOME_ANGLES["E"])

    def _get_square_angles(self, square: str) -> tuple[tuple, tuple]:
        if square not in SQUARE_LUT:
            raise KeyError(f"Square '{square}' not found in LUT. Calibrate it first.")
        entry = SQUARE_LUT[square]
        return entry["hover"], entry["ground"]

    def execute_move(self, uci: str):
        """
        Execute a full pick-and-place sequence for a UCI move string.
        Example: uci='e2e4'  ->  pick from e2, place at e4.
        Handles promotion notation (e.g. 'e7e8q') by ignoring the suffix.
        """
        src = uci[:2]
        dst = uci[2:4]

        src_hover, src_ground = self._get_square_angles(src)
        dst_hover, dst_ground = self._get_square_angles(dst)

        # 1. Move to source hover position
        self._move_angles(*src_hover)

        # 2. Open gripper before descending
        self._gripper(self.GRIPPER_OPEN)

        # 3. Descend to source ground position
        self._move_angles(*src_ground)

        # 4. Close gripper and engage hold torque
        self._gripper(self.GRIPPER_CLOSE, hold=True)

        # 5. Rise back to source hover
        self._move_angles(*src_hover)

        # 6. Move to destination hover
        self._move_angles(*dst_hover)

        # 7. Descend to destination ground
        self._move_angles(*dst_ground)

        # 8. Release piece
        self._gripper(self.GRIPPER_OPEN)

        # 9. Rise back to destination hover
        self._move_angles(*dst_hover)

        # 10. Return arm to safe home position
        self.go_home()

    def request_status(self) -> str:
        self.send_command("?")
        return self.ACK

    def close(self):
        if self.ser.is_open:
            self.ser.close()

    def __enter__(self):
        return self

    def __exit__(self, *_):
        self.close()
