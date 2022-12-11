package game;

import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import persistence.gamestate.visitor.Visitor;
import starships.entities.Asteroid;
import starships.entities.BaseEntity;
import starships.entities.EntityType;
import starships.entities.bullet.Bullet;
import starships.entities.ship.ShipController;
import starships.entities.weapon.WeaponType;
import starships.movement.Mover;
import starships.movement.ShipMover;
import starships.movement.spawners.AsteroidSpawner;
import persistence.ShipsInitializer;
import starships.physics.Position;
import starships.utils.RandomNumberGenerator;
import starships.utils.ScoreDTO;

import java.io.IOException;
import java.util.*;

public class LiveGame implements GameState {

    private final List<Mover> movingEntities;
    private final List<ShipController> ships;
    private final List<String> removedIds;
    private final Map<String, Integer> scores;
    private final Boolean validState;


    public LiveGame(List<Mover> movingEntities, List<ShipController> ships, List<String> removedIds, Map<String, Integer> scores) {
        this.movingEntities = movingEntities;
        this.ships = ships;
        this.scores = scores;
        this.removedIds = removedIds;
        this.validState = true;
    }

    public LiveGame(){
        this.movingEntities = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.scores = new HashMap<>();
        this.removedIds = new ArrayList<>();
        this.validState = false;

    }

    public GameState accelerateShip(String shipId, Double coef) {
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            ShipController acceleratedShip = foundShip.get().accelerate(coef);
            List<ShipController> newList = replaceShip(this.ships, foundShip.get(), acceleratedShip);
            return new LiveGame(this.movingEntities, newList, this.removedIds, scores);
        } else {
            return this;
        }

    }


    public GameState rotateShip(String shipId, Integer degrees) {
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            ShipController acceleratedShip = foundShip.get().rotate(degrees);
            List<ShipController> newList = replaceShip(this.ships, foundShip.get(), acceleratedShip);
            return new LiveGame(this.movingEntities, newList, this.removedIds, scores);
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
        Map<String, Integer> newScores = addScore(element1.get(), element2.get());
        Optional<BaseEntity> collidedElement1 = collideElements(element1.get(), element2.get());
        Optional<BaseEntity> collidedElement2 = collideElements(element2.get(), element1.get());
        insertCollidedElementsInLists(shipsCopy, moversCopy, removedIdsCopy, element1, element2, collidedElement1, collidedElement2);
        return new LiveGame(moversCopy, shipsCopy, removedIdsCopy, newScores);

    }

    public GameState handleReachBounds(String elementId){
        if(elementId.startsWith("Ship"))
            return stopShip(elementId);
        else return this;
    }

    public GameState handleOutOfBounds(String elementId){
        List<Mover> moverList = new ArrayList<>(this.movingEntities);
        List<String> newRemovedIds = new ArrayList<>(this.removedIds);
        List<ShipController> newShips = new ArrayList<>(this.ships);
        if(!elementId.startsWith("Ship")){
            moverList = getMoversCopyWithMoverRemoved(elementId);
            newRemovedIds.add(elementId);
        } else {
            newShips = resetShipPosition(elementId, newShips);
        }
        return new LiveGame(moverList, newShips, newRemovedIds, this.scores);
    }

    private List<ShipController> resetShipPosition(String elementId, List<ShipController> newShips) {
        Optional<ShipController> originalShip = findShip(elementId);
        if(originalShip.isPresent()){
            ShipController resetShip = originalShip.get().resetToInitialPosition();
            newShips = replaceShip(this.ships, originalShip.get(), resetShip);

        }
        return newShips;
    }

    public GameState changeWeapon(String shipId){
        Optional<ShipController> foundShip = findShip(shipId);
        if(foundShip.isPresent()){
            WeaponType nextWeapon = getNextWeapon(foundShip.get().getWeapon().getWeaponType());
            ShipController changedWeaponShip = foundShip.get().changeWeapon(nextWeapon);
            List<ShipController> newList = replaceShip(this.ships, foundShip.get(), changedWeaponShip);
            return new LiveGame(this.movingEntities, newList, this.removedIds, scores);
        } else {
            return this;
        }
    }

    private WeaponType getNextWeapon(WeaponType weaponType) {
        return weaponType == WeaponType.LASER ? WeaponType.EXPLOSIVE : WeaponType.LASER;
    }

    private Map<String, Integer> addScore(Mover element1, Mover element2) {
        Map<String, Integer> newScores = new HashMap<>(this.scores);
        newScores = addScoreToMover(newScores, element1.getEntity().getScore());
        newScores = addScoreToMover(newScores, element2.getEntity().getScore());
        System.out.println(newScores);
        return newScores;

    }

    private Map<String, Integer> addScoreToMover(Map<String, Integer> newScores, ScoreDTO score) {
        Map<String, Integer> scoreMapToAdd = new HashMap<>(newScores);
           if(newScores.containsKey(score.id())){
            Integer value = newScores.get(score.id()).intValue();
            scoreMapToAdd.put(score.id(), value + score.score());
        }
        return scoreMapToAdd;
    }

    private static Optional collideElements(Mover element1, Mover element2) {
        return element1.getEntity().collide(element2.getEntity());
    }

    private void insertCollidedElementsInLists(List<ShipController> shipsCopy, List<Mover> moversCopy, List<String> removedIdsCopy, Optional<Mover> element1, Optional<Mover> element2, Optional<BaseEntity> collidedElement1, Optional<BaseEntity> collidedElement2) {
        insertIntoCorrespondingList(collidedElement1, element1, shipsCopy, moversCopy, removedIdsCopy);
        insertIntoCorrespondingList(collidedElement2, element2, shipsCopy, moversCopy, removedIdsCopy);
    }

    @NotNull
    private List<Mover> getMoversCopyWithMoverRemoved(String element1Id, String element2Id) {
        return new ArrayList<>(this.movingEntities.stream().filter(mover -> !mover.getId().equals(element1Id) && !mover.getId().equals(element2Id)).toList());
    }
    @NotNull
    private List<Mover> getMoversCopyWithMoverRemoved(String element1Id) {
        return new ArrayList<>(this.movingEntities.stream().filter(mover -> !mover.getId().equals(element1Id)).toList());
    }


    @NotNull
    private List<ShipController> getShipsCopyWithShipRemoved(String element1Id, String element2Id) {
        return new ArrayList<>(this.ships.stream().filter(shipController -> !shipController.getId().equals(element1Id) && !shipController.getId().equals(element2Id)).toList());
    }



    private void insertIntoCorrespondingList(Optional<BaseEntity> collisionResult, Optional<Mover> element1, List<ShipController> shipsCopy, List<Mover> moversCopy, List<String> removedIdsCopy) {
        if(collisionResult.isEmpty()){
            removedIdsCopy.add(element1.get().getId());
            return;
        }
        if(collisionResult.get().getId().startsWith("Ship")){
            ShipController collidedShipController = getCollidedShipController(collisionResult);
            shipsCopy.add(collidedShipController);
        } else {
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
        AsteroidSpawner asteroidSpawner = new AsteroidSpawner();
        Integer targetShipIndex = RandomNumberGenerator.getRandomNumber(0, ships.size());
        Position targetShipPosition = ships.get(targetShipIndex).getShipMover().getPosition();
        return asteroidSpawner.spawnAsteroid(targetShipPosition);
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
        if(foundShip.isPresent() && movingBulletQuantity() < 70){
            List<Mover<Bullet>> shotBullets = foundShip.get().shoot();
            List<Mover> newMovers = new ArrayList<>(this.movingEntities);
            newMovers.addAll(shotBullets);
            return new LiveGame(newMovers, this.ships, this.removedIds,this.scores);
        } else {
            return this;
        }
    }

    private int movingBulletQuantity() {
        int count = 0;
        for (Mover movingEntity : movingEntities) {
            if (movingEntity.getEntity().getEntityType() == EntityType.BULLET)
                count++;
        }
        return count;
    }

    private Optional<ShipController> findShip(String shipId){
        for (ShipController ship : ships) {
            if(ship.getId().equals(shipId)) return Optional.of(ship);
        }
        return Optional.empty();
    }

    @NotNull
    private List<ShipController> replaceShip(List<ShipController> list, ShipController ship, ShipController updatedShip) {
        List<ShipController> newList = new ArrayList<>(list);
        newList.remove(ship);
        newList.add(updatedShip);
        return newList;
    }


    public static GameState newGame() throws IOException, ParseException {
        ShipsInitializer shipsInitializer = new ShipsInitializer();
        List<Mover> movers = new ArrayList<>();
        List<ShipController> shipControllers = shipsInitializer.createShipControllers();
        Map<String, Integer> shipScores = initializeShipScores(shipControllers);
        List<String> removedIds = new ArrayList<>();
        return new LiveGame(movers, shipControllers, removedIds, shipScores);
    }

    private static Map<String, Integer> initializeShipScores(List<ShipController> shipControllers) {
        Map<String, Integer> scoresMap = new HashMap<>();
        for (ShipController shipController : shipControllers) {
            scoresMap.put(shipController.getId(), 0);
        }
        return scoresMap;
    }

    public List<Mover> getMovingEntities() {
        return movingEntities;
    }

    public List<ShipController> getShips() {
        return ships;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public List<String> getRemovedIds() {
        return removedIds;
    }

    private Optional<Mover> findMover(String moverId) {
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
            return new LiveGame(this.movingEntities, newShips, this.removedIds, this.scores);
        }
        return this;
    }

    @Override
    public GameState pause() {
        return new PausedGame(this);
    }

    @Override
    public GameState unpause() {
        return this;
    }

    @Override
    public GameState getCopyWith(List<Mover> movers, List<ShipController> ships, List<String> removedIds, Map<String, Integer> scores) {
        return new LiveGame(movers, ships, removedIds, scores);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitGameState(this);
    }
}
