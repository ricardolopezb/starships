package game.actions.ship;

import game.GameState;
import game.actions.GameStateAction;

public class ShootAction implements GameStateAction {
    private final String shipId;

    public ShootAction(String shipId) {
        this.shipId = shipId;
    }
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.shoot(shipId);
    }
}
