package starships;

import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import starships.entities.Asteroid;
import starships.entities.BaseEntity;
import starships.entities.bullet.Bullet;
import starships.entities.ship.ShipController;
import starships.movement.Mover;
import starships.movement.ShipMover;
import starships.movement.spawners.AsteroidSpawner;
import persistence.ShipsInitializer;
import starships.physics.Position;
import starships.utils.RandomNumberGenerator;

import java.io.IOException;
import java.util.*;

public class GameState {

    private final List<Mover> movingEntities;
    private final List<ShipController> ships;
    private final List<String> removedIds;
    private final Map<ShipController, Integer> scores;
    private final AsteroidSpawner asteroidSpawner;


    public GameState(List<Mover> movingEntities, List<ShipController> ships, List<String> removedIds, Map<ShipController, Integer> scores) {
        this.movingEntities = movingEntities;
        this.ships = ships;
        this.scores = scores;
        this.removedIds = removedIds;
        this.asteroidSpawner = new AsteroidSpawner();
    }

    public GameState(){
        this.movingEntities = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.scores = new HashMap<>();
        this.removedIds = new ArrayList<>();
        this.asteroidSpawner = new AsteroidSpawner();
    }

    public GameState accelerateShip(String shipId, Double coef) {
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            ShipController acceleratedShip = foundShip.get().accelerate(coef);
            List<ShipController> newList = replaceShip(this.ships, foundShip.get(), acceleratedShip);
            return new GameState(this.movingEntities, newList, this.removedIds, scores);
        } else {
            return this;
        }

    }

    public GameState rotateShip(String shipId, Integer degrees) {
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            ShipController acceleratedShip = foundShip.get().rotate(degrees);
            List<ShipController> newList = replaceShip(this.ships, foundShip.get(), acceleratedShip);
            return new GameState(this.movingEntities, newList, this.removedIds, scores);
        } else {
            return this;
        }
    }

    public GameState handleCollision(String element1Id, String element2Id){
        List<ShipController> shipsCopy = getShipsCopyWithShipRemoved(element1Id, element2Id);
        List<Mover> moversCopy = getMoversCopyWithMoverRemoved(element1Id, element2Id);
        List<String> removedIdsCopy = new ArrayList<>(this.removedIds);
        Optional<Mover> element1 = findMoverInAllEntities(element1Id);
        Optional<Mover> element2 = findMoverInAllEntities(element2Id);
        if(element1.isEmpty() || element2.isEmpty()) return this;
        Optional<BaseEntity> collidedElement1 = collideElements(element1, element2);
        Optional<BaseEntity> collidedElement2 = collideElements(element2, element1);
        insertCollidedElementsInLists(shipsCopy, moversCopy, removedIdsCopy, element1, element2, collidedElement1, collidedElement2);
        return new GameState(moversCopy, shipsCopy, removedIdsCopy, this.scores);

    }

    private static Optional collideElements(Optional<Mover> element1, Optional<Mover> element2) {
        return element1.get().getEntity().collide(element2.get().getEntity());
    }

    private void insertCollidedElementsInLists(List<ShipController> shipsCopy, List<Mover> moversCopy, List<String> removedIdsCopy, Optional<Mover> element1, Optional<Mover> element2, Optional<BaseEntity> collidedElement1, Optional<BaseEntity> collidedElement2) {
        insertIntoCorrespondingList(collidedElement1, element1, shipsCopy, moversCopy, removedIdsCopy);
        insertIntoCorrespondingList(collidedElement2, element2, shipsCopy, moversCopy, removedIdsCopy);
    }

    @NotNull
    private ArrayList<Mover> getMoversCopyWithMoverRemoved(String element1Id, String element2Id) {
        return new ArrayList<>(this.movingEntities.stream().filter(mover -> !mover.getId().equals(element1Id) && !mover.getId().equals(element2Id)).toList());
    }

    @NotNull
    private ArrayList<ShipController> getShipsCopyWithShipRemoved(String element1Id, String element2Id) {
        return new ArrayList<>(this.ships.stream().filter(shipController -> !shipController.getId().equals(element1Id) && !shipController.getId().equals(element2Id)).toList());
    }



    private void insertIntoCorrespondingList(Optional<BaseEntity> collisionResult, Optional<Mover> element1, List<ShipController> shipsCopy, List<Mover> moversCopy, List<String> removedIdsCopy) {
        if(collisionResult.isEmpty()){
            removedIdsCopy.add(element1.get().getId());
            return;
        }

        if(collisionResult.get().getId().startsWith("Ship")){
            //removePreExistingShipController(collisionResult, shipsCopy);
            ShipController collidedShipController = getCollidedShipController(collisionResult);
            shipsCopy.add(collidedShipController);
        } else {
            //aaremovePreExistingMover(collisionResult, moversCopy);
            Mover newCollidedMover = getCollidedEntityMover(collisionResult, shipsCopy, moversCopy);
            moversCopy.add(newCollidedMover);
        }
    }

    private static void removePreExistingMover(Optional<BaseEntity> collisionResult, List<Mover> moversCopy) {
        for (Mover mover : moversCopy) {
            if(mover.getId().equals(collisionResult.get().getId())){
                moversCopy.remove(mover);
            }
        }
    }


    private Mover getCollidedEntityMover(Optional<BaseEntity> collisionResult, List<ShipController> shipsCopy, List<Mover> moversCopy) {
        Mover originalMover = findMover(collisionResult.get().getId()).get();
        return new Mover(collisionResult.get(),
                originalMover.getPosition(),
                originalMover.getMovementVector(),
                originalMover.getFacingDirection(),
                originalMover.getAdapter()
        );
    }

    @NotNull
    private ShipController getCollidedShipController(Optional<BaseEntity> collisionResult) {
        ShipController originalShipController = findShip(collisionResult.get().getId()).get();
        ShipController collidedShipController = new ShipController(
                new ShipMover(
                        new Mover<>(collisionResult.get(),
                                originalShipController.getShipMover().getPosition(),
                                originalShipController.getShipMover().getMovementVector(),
                                originalShipController.getShipMover().getFacingDirection(),
                                originalShipController.getShipMover().getAdapter())
                )
                , originalShipController.getWeapon());
        return collidedShipController;
    }

    private Optional<Mover> findMoverInAllEntities(String entityId){
        for (Mover movingEntity : movingEntities) {
            if(movingEntity.getId().equals(entityId)) return Optional.of(movingEntity);
        }
        for (ShipController ship : ships) {
            if(ship.getId().equals(entityId)) return Optional.of(ship.getShipMover().getMover());
        }
        return Optional.empty();
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

    public GameState shoot(String shipId){
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            List<Mover<Bullet>> shotBullets = foundShip.get().shoot();
            List<Mover> newMovers = new ArrayList<>(this.movingEntities);
            newMovers.addAll(shotBullets);
            return new GameState(newMovers, this.ships, this.removedIds,this.scores);
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


    public GameState initialize() throws IOException, ParseException {
        ShipsInitializer shipsInitializer = new ShipsInitializer();
        List<Mover> movers = new ArrayList<>();
        Map<ShipController, Integer> shipScores = new HashMap<>();
        List<ShipController> shipControllers = shipsInitializer.createShipControllers();
        return new GameState(movers, shipControllers, this.removedIds, shipScores);
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

    public Optional<Mover> findMover(String moverId) {
        for (Mover mover : movingEntities) {
            if(mover.getId().equals(moverId)) return Optional.of(mover);
        }
        return Optional.empty();
    }

    public List<Mover> removeMovingEntity(List<Mover> list, Mover entity) {
        List<Mover> listCopy = new ArrayList<>(list);
        listCopy.remove(entity);
        return listCopy;
    }

    public GameState stopShip(String shipId){
        Optional<ShipController> stoppingShipOpt = findShip(shipId);
        if (stoppingShipOpt.isPresent()){
            ShipController stoppingShip = stoppingShipOpt.get();
            ShipController stoppedShip = stoppingShip.stop();
            List<ShipController> newShips = replaceShip(this.ships, stoppingShip, stoppedShip);
            return new GameState(this.movingEntities, newShips, this.removedIds, this.scores);
        }
        return this;
    }

}
