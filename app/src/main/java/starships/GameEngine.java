package starships;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import starships.entities.Ship;
import starships.entities.bullet.Bullet;
import starships.entities.weapon.Weapon;
import starships.entities.weapon.WeaponFactory;
import starships.entities.weapon.WeaponType;
import starships.movement.Mover;
import starships.movement.ShipMover;
import starships.persistence.ShipsInitializer;
import starships.physics.Position;
import starships.physics.Vector;
import starships.utils.IdGenerator;

import java.io.IOException;
import java.util.*;

public class GameEngine {

    private final List<Mover> movingEntities;
    private final List<ShipController> ships;


    private final Map<Ship, Integer> scores;

    public GameEngine(List<Mover> movingEntities, List<ShipController> ships, Map<Ship, Integer> scores) {
        this.movingEntities = movingEntities;
        this.ships = ships;
        this.scores = scores;
    }

    public GameEngine(){
        this.movingEntities = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.scores = new HashMap<>();
    }

    public GameEngine accelerateShip(String shipId, Double coef) {
        for (ShipController ship : ships) {
            if(ship.getId().equals(shipId)){
                ShipController acceleratedShip = ship.accelerate(coef);
                List<ShipController> newList = replaceShip(ship, acceleratedShip);
                return new GameEngine(this.movingEntities, newList, scores);
            }

        }
        return this;
    }

    public GameEngine rotateShip(String shipId, Integer degrees) {
        for (ShipController ship : ships) {
            if(ship.getId().equals(shipId)){
                ShipController acceleratedShip = ship.rotate(degrees);
                List<ShipController> newList = replaceShip(ship, acceleratedShip);
                return new GameEngine(this.movingEntities, newList, scores);
            }

        }
        return this;
    }

    public GameEngine shoot(String shipId){
        for (ShipController ship : ships){
            if(ship.getId().equals(shipId)){
                List<Mover<Bullet>> shotBullets = ship.shoot();
                List<Mover> newMovers = new ArrayList<>(this.movingEntities);
                newMovers.addAll(shotBullets);
                return new GameEngine(newMovers, this.ships, this.scores);
            }
        }
        return this;
    }

    @NotNull
    private List<ShipController> replaceShip(ShipController ship, ShipController acceleratedShip) {
        ships.remove(ship);
        List<ShipController> newList = new ArrayList<>(ships);
        newList.add(acceleratedShip);
        return newList;
    }


    public GameEngine initialize() throws IOException, ParseException {
        ShipsInitializer shipsInitializer = new ShipsInitializer();
        List<Mover> movers = new ArrayList<>();
        Map<Ship, Integer> shipScores = new HashMap<>();
        List<ShipController> shipControllers = shipsInitializer.createShipControllers();
        return new GameEngine(movers, shipControllers, shipScores);
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




}
