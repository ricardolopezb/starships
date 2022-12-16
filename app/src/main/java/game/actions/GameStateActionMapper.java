package game.actions;

import game.actions.game.PauseGameAction;
import game.actions.game.SaveGameStateAction;
import game.actions.ship.*;

public class GameStateActionMapper {
    public static GameStateAction getShipActionForDescription(String shipId, String description){
        return switch (description){
            case "accelerate" -> new AccelerateShipAction(shipId);
            case "brake" -> new StopShipAction(shipId);
            case "rotate_clockwise" -> new RotateShipClockwiseAction(shipId);
            case "rotate_counterclockwise" -> new RotateShipCounterclockwiseAction(shipId);
            case "shoot" -> new ShootAction(shipId);
            case "change_weapon" -> new ChangeWeaponAction(shipId);
            case "pause" -> new PauseGameAction();
            case "save" -> new SaveGameStateAction();
            default -> new AccelerateShipAction(shipId);
        };
    }
}
