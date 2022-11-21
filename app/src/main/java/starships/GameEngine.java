package starships;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import starships.entities.Ship;
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
