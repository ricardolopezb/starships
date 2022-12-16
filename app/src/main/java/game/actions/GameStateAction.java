package game.actions;

import game.GameState;

public interface GameStateAction {
    GameState applyAction(GameState gameState);
}
