package game.actions.ship;

import game.GameState;
import game.actions.Action;

public class ChangeWeaponAction implements Action {
    private final String shipId;

    public ChangeWeaponAction(String shipId) {
        this.shipId = shipId;
    }
    @Override
    public GameState applyAction(GameState gameState) {
        return gameState.changeWeapon(shipId);
    }
}
