package game.actions.ship;

import game.GameState;
import game.actions.GameStateAction;
import persistence.Constants;

public class RotateShipCounterclockwiseAction implements GameStateAction {
    private final String shipId;

    public RotateShipCounterclockwiseAction(String shipId) {
        this.shipId = shipId;
    }
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.rotateShip(shipId, -Constants.SHIP_ROTATION_DEGREES);
    }
}
