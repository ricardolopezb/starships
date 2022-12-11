package game;

import persistence.gamestate.visitor.Visitor;
import starships.entities.Asteroid;
import starships.entities.ship.ShipController;
import starships.movement.Mover;

import java.util.List;
import java.util.Map;

public class OutOfBoundsIgnoringGame implements GameState{
    private final GameState liveGame;
    private final Integer savedEntitiesQuantity;
    private Integer handledEntitySpawnings;

    public OutOfBoundsIgnoringGame(GameState liveGame, Integer savedEntitiesQuantity) {
        this.liveGame = liveGame;
        this.savedEntitiesQuantity = savedEntitiesQuantity;
        this.handledEntitySpawnings = 0;
    }
    public OutOfBoundsIgnoringGame(GameState liveGame, Integer savedEntitiesQuantity, Integer handledEntitySpawnings) {
        this.liveGame = liveGame;
        this.savedEntitiesQuantity = savedEntitiesQuantity;
        this.handledEntitySpawnings = handledEntitySpawnings;
    }

    @Override
    public GameState accelerateShip(String shipId, Double coef) {
        return new OutOfBoundsIgnoringGame(liveGame.accelerateShip(shipId,coef), savedEntitiesQuantity, handledEntitySpawnings);
    }

    @Override
    public GameState rotateShip(String shipId, Integer degrees) {
        return new OutOfBoundsIgnoringGame(liveGame.rotateShip(shipId,degrees), savedEntitiesQuantity, handledEntitySpawnings);

    }

    @Override
    public GameState handleCollision(String element1Id, String element2Id) {
        return new OutOfBoundsIgnoringGame(liveGame.handleCollision(element1Id, element2Id), savedEntitiesQuantity, handledEntitySpawnings);

    }

    @Override
    public GameState handleReachBounds(String elementId) {
        return new OutOfBoundsIgnoringGame(liveGame.handleReachBounds(elementId), savedEntitiesQuantity, handledEntitySpawnings);
    }

    @Override
    public GameState handleOutOfBounds(String elementId) {
        if(handledEntitySpawnings == savedEntitiesQuantity){
            System.out.println("STOPPED BEING OUT OF BOUNDS IGNORER");
            return liveGame;
        } else {
            return new OutOfBoundsIgnoringGame(liveGame, savedEntitiesQuantity, handledEntitySpawnings+1);
        }
    }

    @Override
    public GameState changeWeapon(String shipId) {
        return new OutOfBoundsIgnoringGame(liveGame.changeWeapon(shipId), savedEntitiesQuantity, handledEntitySpawnings);
    }

    @Override
    public Mover<Asteroid> spawnAsteroid() {
        return liveGame.spawnAsteroid();
    }

    @Override
    public GameState shoot(String shipId) {
        return new OutOfBoundsIgnoringGame(liveGame.shoot(shipId), savedEntitiesQuantity, handledEntitySpawnings);

    }

    @Override
    public List<Mover> getMovingEntities() {
        return liveGame.getMovingEntities();
    }

    @Override
    public List<ShipController> getShips() {
        return liveGame.getShips();
    }

    @Override
    public Map<String, Integer> getScores() {
        return liveGame.getScores();
    }

    @Override
    public List<String> getRemovedIds() {
        return liveGame.getRemovedIds();
    }

    @Override
    public GameState stopShip(String shipId) {
        return new OutOfBoundsIgnoringGame(liveGame.stopShip(shipId), savedEntitiesQuantity, handledEntitySpawnings);

    }

    @Override
    public GameState pause() {
        return liveGame.pause();
    }

    @Override
    public GameState unpause() {
        return liveGame.unpause();
    }

    @Override
    public GameState getCopyWith(List<Mover> movers, List<ShipController> ships, List<String> removedIds, Map<String, Integer> scores) {
        return new OutOfBoundsIgnoringGame(liveGame.getCopyWith(movers,ships,removedIds,scores), this.savedEntitiesQuantity, this.handledEntitySpawnings);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return liveGame.accept(visitor);
    }
}
