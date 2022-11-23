package starships.physics.collision.calculators;

import starships.GameEngine;
import starships.entities.bullet.Bullet;
import starships.entities.ship.ShipController;
import starships.movement.Mover;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShipBulletCollisionCalculator implements CollisionCalculator{
    private final Integer score;

    public ShipBulletCollisionCalculator(Integer score) {
        this.score = score;
    }

    @Override
    public GameEngine handleCollision(GameEngine currentState, String shipId, String bulletId) {
        System.out.println("Ship id = " + shipId + " bullet-id = " + bulletId);


        Optional<ShipController> ship = currentState.findShip(shipId);
        Optional<Mover> bulletMover = currentState.findMover(bulletId);
        List<String> newRemovedIds = new ArrayList<>(currentState.getRemovedIds());
        List<ShipController> newShips = currentState.getShips();
        List<Mover> newMovingEntities = currentState.getMovingEntities();
        Map<ShipController, Integer> newScores = currentState.getScores();

        if(bulletMover.isPresent() && ship.isPresent()){
            Bullet bullet = (Bullet) bulletMover.get().getEntity();
            if(shipId.equals(bullet.getOwnerId())) return currentState;
            Integer bulletDamage = bullet.getDamage();
            Optional<ShipController> damagedShip = ship.get().takeDamage(bulletDamage);
            if(damagedShip.isEmpty()){
                newRemovedIds.add(ship.get().getId());
                newShips = currentState.removeShip(currentState.getShips(), ship.get());
                Optional<ShipController> ownerShip = getOwnerShip(currentState, bullet);
                newScores = increaseScore(ownerShip, currentState.getScores(), score);
            } else {
                newShips = currentState.replaceShip(currentState.getShips(), ship.get(), damagedShip.get());
            }
            newMovingEntities = currentState.removeMovingEntity(currentState.getMovingEntities(), bulletMover.get());
            newRemovedIds.add(bullet.getId());

        }
        return new GameEngine(newMovingEntities, newShips, newRemovedIds, newScores);

    }

    private Optional<ShipController> getOwnerShip(GameEngine currentState, Bullet bullet) {
        return currentState.findShip(bullet.getOwnerId());
    }
}
