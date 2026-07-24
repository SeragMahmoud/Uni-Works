import queue
import threading
import tkinter as tk
from tkinter import scrolledtext

import cv2
import numpy as np
from PIL import Image, ImageTk

_CAM_W, _CAM_H = 640, 480
_LOG_W, _LOG_H = 340, 480


class Dashboard:
    def __init__(self, title: str = "Chess Arm Control"):
        self.root = tk.Tk()
        self.root.title(title)
        self.root.resizable(False, False)
        self.root.configure(bg="#1a1a2e")

        self._frame_q: queue.Queue = queue.Queue(maxsize=2)
        self._log_q:   queue.Queue = queue.Queue()
        self._running  = True

        self._build_ui()

    # ── UI construction ──────────────────────────────────────────────────────

    def _build_ui(self):
        top = tk.Frame(self.root, bg="#1a1a2e")
        top.pack(padx=10, pady=(10, 0))

        # Camera feed panel
        cam_frame = tk.LabelFrame(top, text=" CV Feed ",
                                  bg="#1a1a2e", fg="#e0e0e0",
                                  font=("Courier", 10, "bold"),
                                  bd=2, relief="groove")
        cam_frame.grid(row=0, column=0, padx=(0, 8))

        placeholder = np.zeros((_CAM_H, _CAM_W, 3), dtype=np.uint8)
        cv2.putText(placeholder, "Waiting for camera...", (140, 240),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.9, (80, 80, 80), 2)
        self._cam_label = tk.Label(cam_frame, bg="#000000")
        self._cam_label.pack()
        self._show_numpy(placeholder)

        # Command log panel
        log_frame = tk.LabelFrame(top, text=" Command Log ",
                                  bg="#1a1a2e", fg="#e0e0e0",
                                  font=("Courier", 10, "bold"),
                                  bd=2, relief="groove")
        log_frame.grid(row=0, column=1, sticky="ns")

        self._log_box = scrolledtext.ScrolledText(
            log_frame,
            width=38, height=28,
            bg="#0d0d0d", fg="#00ff88",
            font=("Courier", 10),
            state="disabled",
            wrap="word",
        )
        self._log_box.pack(padx=4, pady=4)

        # Tag colours
        self._log_box.tag_config("cmd",    foreground="#00aaff")
        self._log_box.tag_config("engine", foreground="#ffcc00")
        self._log_box.tag_config("warn",   foreground="#ff6600")
        self._log_box.tag_config("error",  foreground="#ff3333")
        self._log_box.tag_config("ok",     foreground="#00ff88")

        # Status bar
        self._status_var = tk.StringVar(value="BOOTING …")
        status_bar = tk.Label(
            self.root,
            textvariable=self._status_var,
            bg="#16213e", fg="#ffffff",
            font=("Courier", 10),
            anchor="w", padx=8,
        )
        status_bar.pack(fill="x", padx=10, pady=(4, 10))

    # ── Public API (thread-safe) ─────────────────────────────────────────────

    def update_frame(self, frame: np.ndarray):
        """Push a BGR numpy frame to the camera panel."""
        if not self._running:
            return
        resized = cv2.resize(frame, (_CAM_W, _CAM_H))
        try:
            self._frame_q.put_nowait(resized)
        except queue.Full:
            pass

    def log(self, message: str, tag: str = "ok"):
        """Append a line to the command log. Tags: cmd, engine, warn, error, ok."""
        if self._running:
            self._log_q.put_nowait((message, tag))

    def set_status(self, text: str):
        """Update the bottom status bar."""
        self.log(f"[STATUS] {text}", "ok")
        if self._running:
            self._log_q.put_nowait(("__status__:" + text, "ok"))

    # ── Internal helpers ─────────────────────────────────────────────────────

    def _show_numpy(self, bgr: np.ndarray):
        rgb   = cv2.cvtColor(bgr, cv2.COLOR_BGR2RGB)
        photo = ImageTk.PhotoImage(Image.fromarray(rgb))
        self._cam_label.configure(image=photo)
        self._cam_label.image = photo

    def _poll(self):
        # Drain frame queue
        try:
            frame = self._frame_q.get_nowait()
            self._show_numpy(frame)
        except queue.Empty:
            pass

        # Drain log queue
        while True:
            try:
                item, tag = self._log_q.get_nowait()
                if item.startswith("__status__:"):
                    self._status_var.set(item[len("__status__:"):])
                else:
                    self._log_box.configure(state="normal")
                    self._log_box.insert("end", item + "\n", tag)
                    self._log_box.see("end")
                    self._log_box.configure(state="disabled")
            except queue.Empty:
                break

        if self._running:
            self.root.after(33, self._poll)   # ~30 fps refresh

    def _on_close(self):
        self._running = False
        self.root.destroy()

    # ── Entry point ──────────────────────────────────────────────────────────

    def run(self, target_func, *args, **kwargs):
        """
        Start target_func(*args, **kwargs) in a daemon thread,
        then hand control to the tkinter event loop.
        """
        t = threading.Thread(
            target=target_func, args=args, kwargs=kwargs, daemon=True
        )
        t.start()
        self.root.after(100, self._poll)
        self.root.protocol("WM_DELETE_WINDOW", self._on_close)
        self.root.mainloop()

    @property
    def running(self) -> bool:
        return self._running
