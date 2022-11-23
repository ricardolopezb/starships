package starships.physics.collision;

import starships.GameEngine;
import starships.entities.ship.ShipController;
import starships.physics.collision.calculators.CollisionCalculator;
import starships.physics.collision.calculators.ShipBulletCollisionCalculator;
import starships.physics.collision.calculators.ShipShipCollisionCalculator;

import java.util.List;
import java.util.Optional;

public class CollisionHandler {

    public GameEngine handleCollision(GameEngine currentState, String element1Id, String element2Id){
        if(element1Id.startsWith("Ship") && element2Id.startsWith("Ship")) {
            CollisionCalculator calculator = new ShipShipCollisionCalculator(10, 100);
            return calculator.handleCollision(currentState, element1Id, element2Id);
        }
        if(element1Id.startsWith("Ship") && element2Id.startsWith("Bullet")
        || element1Id.startsWith("Bullet") && element2Id.startsWith("Ship")) {
            System.out.println("pingo");
            CollisionCalculator calculator = new ShipBulletCollisionCalculator(200);
            return calculator.handleCollision(currentState, element1Id, element2Id);
        }
//        if() {
//            gameEngine = gameEngine.shipShipCollision(event.element1Id, event.element2Id, 10)
//        }
//        if(element1Id.startsWith("Bullet") && element2Id.startsWith("Asteroid")) {
//            gameEngine = gameEngine.shipShipCollision(event.element1Id, event.element2Id, 10)
//        }
//        if(element1Id.startsWith("Asteroid") && element2Id.startsWith("Bullet")) {
//            gameEngine = gameEngine.shipShipCollision(event.element1Id, event.element2Id, 10)
//        }
//        if(element1Id.startsWith("Ship") && element2Id.startsWith("Asteroid")) {
//            gameEngine = gameEngine.shipShipCollision(event.element1Id, event.element2Id, 10)
//        }
//        if(element1Id.startsWith("Asteroid") && element2Id.startsWith("Ship")) {
//            gameEngine = gameEngine.shipShipCollision(event.element1Id, event.element2Id, 10)
//        }
        return null;
    }

}
