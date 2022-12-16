package game.actions.game;

import game.GameState;
import game.actions.GameStateAction;

public class PauseGameAction implements GameStateAction {
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.togglePause();
    }
}
