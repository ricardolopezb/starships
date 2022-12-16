package game.actions.ship;

import game.GameState;
import game.actions.GameStateAction;
import persistence.Constants;

public class AccelerateShipAction implements GameStateAction {
    private final String shipId;

    public AccelerateShipAction(String shipId) {
        this.shipId = shipId;
    }

    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.accelerateShip(shipId, Constants.SHIP_ACCELERATION_COEFFICIENT);
    }
}
