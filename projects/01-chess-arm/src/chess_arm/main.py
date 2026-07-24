import time
import sys
import threading

from gui import Dashboard
from vision import ChessVision
from engine import EngineController
from robot import ArmController

VISION_MODEL  = "Dynamic-Chess-Board-Piece-Extraction/chess-model-yolov8m.pt"
CAMERA_INDEX  = None       # None = auto-scan 0-4
MOVE_WAIT_SEC = 3.0


def get_move_manually(dashboard: Dashboard) -> str:
    dashboard.log("[MANUAL] Type opponent move in terminal (e.g. e7e5)", "warn")
    while True:
        uci = input("[MANUAL] Enter opponent move: ").strip().lower()
        if len(uci) >= 4 and uci[0] in "abcdefgh" and uci[2] in "abcdefgh":
            return uci
        print("         Invalid format — use UCI: <file><rank><file><rank>")


def game_loop(dashboard: Dashboard):
    dashboard.set_status("Initializing modules …")

    # ── Vision ───────────────────────────────────────────────────────────────
    vision = None
    try:
        vision = ChessVision(model_path=VISION_MODEL, camera_index=CAMERA_INDEX)
        dashboard.log("[BOOT] Vision module ready", "ok")
    except RuntimeError as e:
        dashboard.log(f"[WARN] {e}", "warn")
        dashboard.log("[BOOT] Manual move input mode active", "warn")

    # ── Engine ───────────────────────────────────────────────────────────────
    try:
        engine = EngineController()
        dashboard.log("[BOOT] Stockfish engine ready", "ok")
    except Exception as e:
        dashboard.log(f"[ERROR] Engine init failed: {e}", "error")
        if vision:
            vision.release()
        return

    # ── Arm ──────────────────────────────────────────────────────────────────
    try:
        def _arm_log(cmd):
            dashboard.log(f"  >> {cmd}", "cmd")

        arm = ArmController(on_command=_arm_log)
        dashboard.log("[BOOT] Arm serial ready", "ok")
    except Exception as e:
        dashboard.log(f"[ERROR] Arm/Serial init failed: {e}", "error")
        engine.close()
        if vision:
            vision.release()
        return

    dashboard.set_status("Game running — waiting for opponent")
    dashboard.log("[INFO] Robot plays WHITE. Waiting for human BLACK move …", "ok")

    # ── Camera feed thread ───────────────────────────────────────────────────
    def _feed_loop():
        while dashboard.running:
            if vision:
                try:
                    frame = vision.get_annotated_frame()
                    dashboard.update_frame(frame)
                except Exception:
                    pass
            time.sleep(0.033)   # ~30 fps

    threading.Thread(target=_feed_loop, daemon=True).start()

    # ── Game loop ─────────────────────────────────────────────────────────────
    try:
        while dashboard.running and not engine.is_game_over():

            # PHASE 1 — get opponent move
            if vision is None:
                opponent_uci = get_move_manually(dashboard)
            else:
                dashboard.set_status("Waiting for opponent move …")
                opponent_uci = None
                while opponent_uci is None and dashboard.running:
                    time.sleep(MOVE_WAIT_SEC)
                    try:
                        opponent_uci = vision.get_opponent_move()
                    except RuntimeError as e:
                        dashboard.log(f"[WARN] Vision: {e}", "warn")
                    if opponent_uci is None:
                        dashboard.log("[WAIT] No move detected, retrying …", "warn")

            if not dashboard.running:
                break

            dashboard.log(f"[CV]  Opponent: {opponent_uci}", "ok")

            # PHASE 2 — validate
            if not engine.apply_opponent_move(opponent_uci):
                dashboard.log(f"[WARN] Illegal move: {opponent_uci} — ignored", "warn")
                continue

            dashboard.log(f"[FEN] {engine.get_fen()}", "ok")

            if engine.is_game_over():
                dashboard.log(f"[GAME OVER] {engine.outcome()}", "engine")
                dashboard.set_status(f"Game over: {engine.outcome()}")
                break

            # PHASE 3 — engine reply
            dashboard.set_status("Stockfish thinking …")
            robot_uci = engine.get_best_move()
            if robot_uci is None:
                dashboard.log("[GAME OVER] No move for robot", "engine")
                break

            dashboard.log(f"[ENGINE] Robot plays: {robot_uci}", "engine")
            dashboard.set_status(f"Robot executing: {robot_uci}")

            # PHASE 4 — physical move
            try:
                arm.execute_move(robot_uci)
                dashboard.log(f"[ARM]  {robot_uci} done", "ok")
            except KeyError as e:
                dashboard.log(f"[ERROR] LUT missing: {e}", "error")
            except TimeoutError as e:
                dashboard.log(f"[ERROR] Serial timeout: {e}", "error")
            except Exception as e:
                dashboard.log(f"[ERROR] Arm: {e}", "error")

            if engine.is_game_over():
                dashboard.log(f"[GAME OVER] {engine.outcome()}", "engine")
                dashboard.set_status(f"Game over: {engine.outcome()}")
                break

            dashboard.set_status("Waiting for opponent move …")

    except Exception as e:
        dashboard.log(f"[FATAL] {e}", "error")

    finally:
        dashboard.log("[SHUTDOWN] Releasing resources …", "warn")
        try:
            arm.go_home()
        except Exception:
            pass
        arm.close()
        engine.close()
        if vision:
            vision.release()
        dashboard.log("[SHUTDOWN] Done.", "warn")
        dashboard.set_status("Stopped")


def main():
    dashboard = Dashboard(title="Chess Arm Control")
    dashboard.run(game_loop, dashboard)


if __name__ == "__main__":
    main()
