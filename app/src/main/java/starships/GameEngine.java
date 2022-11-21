package starships;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import starships.entities.Ship;
import starships.entities.weapon.Weapon;
import starships.entities.weapon.WeaponFactory;
import starships.entities.weapon.WeaponType;
import starships.movement.Mover;
import starships.movement.ShipMover;
import starships.physics.Position;
import starships.physics.Vector;
import starships.utils.IdGenerator;

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

    public GameEngine initialize(JSONObject initialConfigJson){
        List<Mover> movers = new ArrayList<>();
        Map<Ship, Integer> shipScores = new HashMap<>();
        List<ShipController> shipControllers = createShipControllers(initialConfigJson);
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

    public List<ShipController> createShipControllers(JSONObject initialConfigJson) {
        List<ShipController> shipControllers = new ArrayList<>();
        JSONArray listOfShips = (JSONArray) initialConfigJson.get("ships");
        Iterator it = listOfShips.iterator();
        while(it.hasNext()){
            shipControllers.add(createShipController((JSONObject) it.next()));
        }
        return shipControllers;
    }

    public ShipController createShipController(JSONObject initialConfigJson) {
        String shipSkin = (String) initialConfigJson.get("skin");
        Integer shipHealth = ((Long) initialConfigJson.get("health")).intValue();
        String weaponType = (String) initialConfigJson.get("weaponType");

        ShipMover shipMover = createShipMover(shipSkin, shipHealth);
        Weapon weapon = WeaponFactory.createWeaponForType(WeaponType.valueOf(weaponType), shipMover.getMover());

        return new ShipController(shipMover, weapon);


    }

    private static ShipMover createShipMover(String shipSkin, Integer shipHealth) {
        Mover<Ship> mover = new Mover<>(
                new Ship("Ship-"+ IdGenerator.generateId(), shipHealth, shipSkin),
                new Position(300, 300),
                new Vector(0D),
                new Vector(0D)
                );

        ShipMover shipMover = new ShipMover(mover);
        return shipMover;
    }


}
