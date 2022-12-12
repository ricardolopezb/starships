package game.actions.game;

import game.GameState;
import game.actions.Action;

public class PauseGameAction implements Action {
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.togglePause();
    }
}
