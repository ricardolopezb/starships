package game;

import persistence.gamestate.visitor.Visitable;
import starships.entities.Asteroid;
import starships.entities.ship.ShipController;
import starships.movement.Mover;

import java.util.List;
import java.util.Map;

public interface GameState extends Visitable {
    GameState accelerateShip(String shipId, Double coef);
    GameState rotateShip(String shipId, Integer degrees);
    GameState handleCollision(String element1Id, String element2Id);
    GameState handleReachBounds(String elementId);
    GameState handleOutOfBounds(String elementId);
    GameState changeWeapon(String shipId);
    Mover<Asteroid> spawnAsteroid();
    GameState shoot(String shipId);
    List<Mover> getMovingEntities();
    List<ShipController> getShips();
    Map<String, Integer> getScores();
    List<String> getRemovedIds();
    GameState stopShip(String shipId);
    GameState togglePause();

    GameState getCopyWith(List<Mover> movers, List<ShipController> ships, List<String> removedIds, Map<String, Integer> scores);


}
