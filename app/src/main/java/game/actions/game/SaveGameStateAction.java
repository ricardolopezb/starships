package game.actions.game;

import game.GameState;
import game.actions.Action;
import persistence.gamestate.GameStateSaver;

public class SaveGameStateAction implements Action {
    @Override
    public GameState applyAction(GameState gameState) {
        GameStateSaver gameStateSaver = new GameStateSaver();
        gameStateSaver.saveGameState(gameState);
        return gameState;
    }
}
