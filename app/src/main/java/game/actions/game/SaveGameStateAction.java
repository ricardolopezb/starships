package game.actions.game;

import game.GameState;
import game.actions.GameStateAction;
import persistence.gamestate.GameStateSaver;

public class SaveGameStateAction implements GameStateAction {
    @Override
    public GameState applyAction(GameState gameState) {
        GameStateSaver gameStateSaver = new GameStateSaver();
        gameStateSaver.saveGameState(gameState);
        return gameState;
    }
}
