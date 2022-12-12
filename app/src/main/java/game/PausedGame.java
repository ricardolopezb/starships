package game;

import adapters.AsteroidUIAdapter;
import persistence.gamestate.visitor.Visitor;
import starships.entities.Asteroid;
import starships.entities.ship.ShipController;
import starships.movement.Mover;
import starships.physics.Position;
import starships.physics.Vector;
import starships.utils.IdGenerator;

import java.util.List;
import java.util.Map;

public class PausedGame implements GameState{
    private final GameState liveGame;

    public PausedGame(GameState liveGame) {
        this.liveGame = liveGame;
    }

    @Override
    public GameState accelerateShip(String shipId, Double coef) {
        return this;
    }

    @Override
    public GameState rotateShip(String shipId, Integer degrees) {
        return this;
    }

    @Override
    public GameState handleCollision(String element1Id, String element2Id) {
        return this;
    }

    @Override
    public GameState handleReachBounds(String elementId) {
        return this;
    }

    @Override
    public GameState handleOutOfBounds(String elementId) {
        return this;
    }

    @Override
    public GameState changeWeapon(String shipId) {
        return this;
    }

    @Override
    public Mover<Asteroid> spawnAsteroid() {
        return new Mover<>(
                new Asteroid("Asteroid"+ IdGenerator.generateId(), 50),
                new Position(30000, 30000),
                new Vector(30.0),
                new Vector(30.0),
                new AsteroidUIAdapter()
                );
    }

    @Override
    public GameState shoot(String shipId) {
        return this;
    }

    @Override
    public List<Mover> getMovingEntities() {
        return this.liveGame.getMovingEntities();
    }

    @Override
    public List<ShipController> getShips() {
        return this.liveGame.getShips();
    }

    @Override
    public Map<String, Integer> getScores() {
        return this.liveGame.getScores();
    }

    @Override
    public List<String> getRemovedIds() {
        return this.liveGame.getRemovedIds();
    }

    @Override
    public GameState stopShip(String shipId) {
        return this;
    }

    @Override
    public GameState togglePause() {
        return this.liveGame;
    }


    @Override
    public GameState getCopyWith(List<Mover> movers, List<ShipController> ships, List<String> removedIds, Map<String, Integer> scores) {
        return this;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return this.liveGame.accept(visitor);
    }
}
