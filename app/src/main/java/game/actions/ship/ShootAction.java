package game.actions.ship;

import game.GameState;
import game.actions.Action;

public class ShootAction implements Action {
    private final String shipId;

    public ShootAction(String shipId) {
        this.shipId = shipId;
    }
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.shoot(shipId);
    }
}
