package starships;

import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import starships.entities.Ship;
import starships.entities.bullet.Bullet;
import starships.movement.Mover;
import starships.persistence.ShipsInitializer;

import java.io.IOException;
import java.util.*;

public class GameEngine {

    private final List<Mover> movingEntities;
    private final List<ShipController> ships;
    private final List<String> removedIds;
    private final Map<Ship, Integer> scores;

    public GameEngine(List<Mover> movingEntities, List<ShipController> ships, List<String> removedIds,Map<Ship, Integer> scores) {
        this.movingEntities = movingEntities;
        this.ships = ships;
        this.scores = scores;
        this.removedIds = removedIds;
    }

    public GameEngine(){
        this.movingEntities = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.scores = new HashMap<>();
        this.removedIds = new ArrayList<>();
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

    public GameEngine damageShips(String ship1Id, String ship2Id, Integer damage){
        Optional<ShipController> ship1 = findShip(ship1Id);
        Optional<ShipController> ship2 = findShip(ship2Id);
        List<ShipController> newShipList = new ArrayList<>(this.ships);
        List<String> newRemovedIds = new ArrayList<>(removedIds);
        System.out.println("Collided!");
        System.out.println("ship1 = " + ship1Id + " ship2 health= " +ship2.get().getShipMover().getMover().getEntity().getHealth());

        if(ship1.isPresent() && ship2.isPresent()){
            Optional<ShipController> damagedShip1 = ship1.get().takeDamage(damage);
            Optional<ShipController> damagedShip2 = ship2.get().takeDamage(damage);

            if(damagedShip1.isPresent()) newShipList = replaceShip(newShipList, ship1.get(), damagedShip1.get());
            else {
                newRemovedIds = pureAddString(newRemovedIds, ship1.get().getId());
                newRemovedIds.add(ship1.get().getId());
            }

            if(damagedShip2.isPresent()) newShipList = replaceShip(newShipList, ship2.get(), damagedShip2.get());
            else {
                newShipList = removeShip(newShipList, ship2.get());
                newRemovedIds = pureAddString(newRemovedIds, ship2.get().getId());
                System.out.println("ship2 exploded");
            }

        }

        return new GameEngine(this.movingEntities, newShipList, newRemovedIds, this.scores);

    }

    public List<String> pureAddString(List<String> list, String elem){
        List<String> newList = new ArrayList<>(list);
        list.add(elem);
        return list;
    }

    private List<ShipController> removeShip(List<ShipController> list, ShipController shipController){
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

    private Optional<ShipController> findShip(String shipId){
        for (ShipController ship : ships) {
            if(ship.getId().equals(shipId)) return Optional.of(ship);
        }
        return Optional.empty();
    }

    @NotNull
    private List<ShipController> replaceShip(List<ShipController> list, ShipController ship, ShipController acceleratedShip) {
        List<ShipController> newList = new ArrayList<>(list);
        newList.remove(ship);
        newList.add(acceleratedShip);
        return newList;
    }


    public GameEngine initialize() throws IOException, ParseException {
        ShipsInitializer shipsInitializer = new ShipsInitializer();
        List<Mover> movers = new ArrayList<>();
        Map<Ship, Integer> shipScores = new HashMap<>();
        List<ShipController> shipControllers = shipsInitializer.createShipControllers();
        return new GameEngine(movers, shipControllers, this.removedIds, shipScores);
    }

    public List<Mover> getMovingEntities() {
        return movingEntities;
    }

    public List<ShipController> getShips() {
        return ships;
    }

    public Map<Ship, Integer> getScores() {
        return scores;
    }

    public List<String> getRemovedIds() {
        return removedIds;
    }
}
