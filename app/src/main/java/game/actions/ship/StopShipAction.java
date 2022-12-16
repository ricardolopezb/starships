package game.actions.ship;

import game.GameState;
import game.actions.GameStateAction;

public class StopShipAction implements GameStateAction {
    private final String shipId;

    public StopShipAction(String shipId) {
        this.shipId = shipId;
    }
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.stopShip(shipId);
    }
}
