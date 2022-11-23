package starships.physics.collision.calculators;

import starships.GameEngine;
import starships.entities.ship.ShipController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface CollisionCalculator {

    GameEngine handleCollision(GameEngine currentState, String element1, String element2);

    default Map<ShipController, Integer> increaseScore(Optional<ShipController> shipIncreasingScore, Map<ShipController, Integer> currentScores, Integer score) {
        Map<ShipController, Integer> newScores = new HashMap<>(currentScores);
        if(shipIncreasingScore.isPresent()) {
            ShipController keyShip = shipIncreasingScore.get();
            if(newScores.containsKey(keyShip))
                newScores.put(keyShip, currentScores.get(keyShip) + score);
            else newScores.put(keyShip, score);
        }
        return newScores;
    }
}
