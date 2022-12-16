package game.actions.ship;

import game.GameState;
import game.actions.GameStateAction;

public class ChangeWeaponAction implements GameStateAction {
    private final String shipId;

    public ChangeWeaponAction(String shipId) {
        this.shipId = shipId;
    }
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.changeWeapon(shipId);
    }
}
