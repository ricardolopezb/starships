package game.actions.ship;

import game.GameState;
import game.actions.GameStateAction;
import persistence.Constants;

public class RotateShipClockwiseAction implements GameStateAction {
    private final String shipId;

    public RotateShipClockwiseAction(String shipId) {
        this.shipId = shipId;
    }

    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.rotateShip(shipId, Constants.SHIP_ROTATION_DEGREES);
    }
}
