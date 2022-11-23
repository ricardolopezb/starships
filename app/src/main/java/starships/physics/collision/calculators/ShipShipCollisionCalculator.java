package starships.physics.collision.calculators;

import starships.GameEngine;
import starships.entities.ship.Ship;
import starships.entities.ship.ShipController;

import java.util.*;

public class ShipShipCollisionCalculator implements CollisionCalculator {
    private final Integer collisionDamage;
    private final Integer score;

    public ShipShipCollisionCalculator(Integer collisionDamage, Integer score) {
        this.collisionDamage = collisionDamage;
        this.score = score;
    }


    @Override
    public GameEngine handleCollision(GameEngine currentState, String ship1Id, String ship2Id) {
        Optional<ShipController> ship1 = currentState.findShip(ship1Id);
        Optional<ShipController> ship2 = currentState.findShip(ship2Id);

        List<ShipController> newShipList = new ArrayList<>(currentState.getShips());
        List<String> newRemovedIds = new ArrayList<>(currentState.getRemovedIds());
        Map<ShipController, Integer> newScores = new HashMap<>(currentState.getScores());

        if (ship1.isPresent() && ship2.isPresent()) {
            Optional<ShipController> damagedShip1 = ship1.get().takeDamage(collisionDamage);
            Optional<ShipController> damagedShip2 = ship2.get().takeDamage(collisionDamage);

            if (damagedShip1.isPresent())
                newShipList = currentState.replaceShip(newShipList, ship1.get(), damagedShip1.get());
            else {
                newShipList = currentState.removeShip(newShipList, ship1.get());
                newRemovedIds = currentState.pureAddString(newRemovedIds, ship1.get().getId());
                newScores = increaseScore(ship2, newScores, score);
            }

            if (damagedShip2.isPresent())
                newShipList = currentState.replaceShip(newShipList, ship2.get(), damagedShip2.get());
            else {
                newShipList = currentState.removeShip(newShipList, ship2.get());
                newRemovedIds = currentState.pureAddString(newRemovedIds, ship2.get().getId());
                newScores = increaseScore(ship1, newScores, score);
            }
        }
        return new GameEngine(currentState.getMovingEntities(), newShipList, newRemovedIds, newScores);
    }


}
