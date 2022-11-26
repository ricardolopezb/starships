package starships;

import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import starships.entities.Asteroid;
import starships.entities.ship.Ship;
import starships.entities.bullet.Bullet;
import starships.entities.ship.ShipController;
import starships.movement.Mover;
import starships.movement.spawners.AsteroidSpawner;
import starships.persistence.ShipsInitializer;
import starships.physics.Position;
import starships.physics.collision.CollisionHandler;
import starships.utils.RandomNumberGenerator;

import java.io.IOException;
import java.util.*;

public class GameEngine {

    private final List<Mover> movingEntities;
    private final List<ShipController> ships;
    private final List<String> removedIds;
    private final Map<ShipController, Integer> scores;
    private final CollisionHandler collisionHandler;
    private final AsteroidSpawner asteroidSpawner;


    public GameEngine(List<Mover> movingEntities, List<ShipController> ships, List<String> removedIds,Map<ShipController, Integer> scores) {
        this.movingEntities = movingEntities;
        this.ships = ships;
        this.scores = scores;
        this.removedIds = removedIds;
        this.collisionHandler = new CollisionHandler();
        this.asteroidSpawner = new AsteroidSpawner();
    }

    public GameEngine(){
        this.movingEntities = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.scores = new HashMap<>();
        this.removedIds = new ArrayList<>();
        this.collisionHandler = new CollisionHandler();
        this.asteroidSpawner = new AsteroidSpawner();
    }

    public GameEngine accelerateShip(String shipId, Double coef) {
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            ShipController acceleratedShip = foundShip.get().accelerate(coef);
            List<ShipController> newList = replaceShip(this.ships, foundShip.get(), acceleratedShip);
            return new GameEngine(this.movingEntities, newList, this.removedIds, scores);
        } else {
            return this;
        }

    }

    public GameEngine rotateShip(String shipId, Integer degrees) {
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            ShipController acceleratedShip = foundShip.get().rotate(degrees);
            List<ShipController> newList = replaceShip(this.ships, foundShip.get(), acceleratedShip);
            return new GameEngine(this.movingEntities, newList, this.removedIds, scores);
        } else {
            return this;
        }
    }

    public GameEngine handleCollision(String element1Id, String element2Id){
        return collisionHandler.handleCollision(this, element1Id, element2Id);
    }


    public Mover<Asteroid> spawnAsteroid(){
        Integer targetShipIndex = RandomNumberGenerator.getRandomNumber(0, ships.size());
        Position targetShipPosition = ships.get(targetShipIndex).getShipMover().getPosition();
        return asteroidSpawner.spawnAsteroid(targetShipPosition);
//        List<Mover> newMoverList = new ArrayList<>(this.movingEntities);
//        newMoverList.add(asteroidMover);
//        return new GameEngine(newMoverList, this.ships, this.removedIds, this.scores);
    }

    public List<String> pureAddString(List<String> list, String elem){
        List<String> newList = new ArrayList<>(list);
        list.add(elem);
        return list;
    }

    public List<ShipController> removeShip(List<ShipController> list, ShipController shipController){
        List<ShipController> listCopy = new ArrayList<>(list);
        listCopy.remove(shipController);
        return listCopy;
    }

    public GameEngine shoot(String shipId){
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            List<Mover<Bullet>> shotBullets = foundShip.get().shoot();
            List<Mover> newMovers = new ArrayList<>(this.movingEntities);
            newMovers.addAll(shotBullets);
            return new GameEngine(newMovers, this.ships, this.removedIds,this.scores);
        } else {
            return this;
        }
    }

    public Optional<ShipController> findShip(String shipId){
        for (ShipController ship : ships) {
            if(ship.getId().equals(shipId)) return Optional.of(ship);
        }
        return Optional.empty();
    }

    @NotNull
    public List<ShipController> replaceShip(List<ShipController> list, ShipController ship, ShipController updatedShip) {
        List<ShipController> newList = new ArrayList<>(list);
        newList.remove(ship);
        newList.add(updatedShip);
        return newList;
    }


    public GameEngine initialize() throws IOException, ParseException {
        ShipsInitializer shipsInitializer = new ShipsInitializer();
        List<Mover> movers = new ArrayList<>();
        Map<ShipController, Integer> shipScores = new HashMap<>();
        List<ShipController> shipControllers = shipsInitializer.createShipControllers();
        return new GameEngine(movers, shipControllers, this.removedIds, shipScores);
    }

    public List<Mover> getMovingEntities() {
        return movingEntities;
    }

    public List<ShipController> getShips() {
        return ships;
    }

    public Map<ShipController, Integer> getScores() {
        return scores;
    }

    public List<String> getRemovedIds() {
        return removedIds;
    }

    public Optional<Mover> findMover(String bulletId) {
        for (Mover mover : movingEntities) {
            if(mover.getId().equals(bulletId)) return Optional.of(mover);
        }
        return Optional.empty();
    }

    public List<Mover> removeMovingEntity(List<Mover> list, Mover entity) {
        List<Mover> listCopy = new ArrayList<>(list);
        listCopy.remove(entity);
        return listCopy;
    }
}
