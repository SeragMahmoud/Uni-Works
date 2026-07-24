import chess
import chess.engine
from config import STOCKFISH_PATH


class EngineController:
    def __init__(self, stockfish_path: str = STOCKFISH_PATH, skill_level: int = 10):
        self.board = chess.Board()
        self._engine = chess.engine.SimpleEngine.popen_uci(stockfish_path)
        self._engine.configure({"Skill Level": skill_level})

    def apply_opponent_move(self, uci: str) -> bool:
        """Push the opponent's move onto the board. Returns False if illegal."""
        move = chess.Move.from_uci(uci)
        if move not in self.board.legal_moves:
            return False
        self.board.push(move)
        return True

    def get_best_move(self, time_limit: float = 1.0) -> str | None:
        """Ask Stockfish for the best reply. Returns UCI string or None on game over."""
        if self.board.is_game_over():
            return None
        result = self._engine.play(
            self.board,
            chess.engine.Limit(time=time_limit),
        )
        if result.move is None:
            return None
        self.board.push(result.move)
        return result.move.uci()

    def get_fen(self) -> str:
        return self.board.fen()

    def is_game_over(self) -> bool:
        return self.board.is_game_over()

    def outcome(self) -> str:
        result = self.board.outcome()
        return str(result) if result else "ongoing"

    def reset(self):
        self.board.reset()

    def close(self):
        self._engine.quit()

    def __enter__(self):
        return self

    def __exit__(self, *_):
        self.close()
