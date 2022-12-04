package game.actions;

import game.GameState;

public interface Action {
    GameState applyAction(GameState gameState);
}
