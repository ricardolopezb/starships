package game.actions.ship;

import game.GameState;
import game.actions.Action;

public class StopShipAction implements Action {
    private final String shipId;

    public StopShipAction(String shipId) {
        this.shipId = shipId;
    }
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.stopShip(shipId);
    }
}
