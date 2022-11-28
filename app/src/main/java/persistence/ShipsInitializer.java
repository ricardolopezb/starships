package persistence;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import starships.entities.ship.ShipController;
import adapters.StarshipUIAdapter;
import starships.entities.ship.Ship;
import starships.entities.weapon.Weapon;
import starships.entities.weapon.WeaponFactory;
import starships.entities.weapon.WeaponType;
import starships.movement.Mover;
import starships.movement.ShipMover;
import starships.physics.Position;
import starships.physics.Vector;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShipsInitializer {



    public List<ShipController> createShipControllers() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(Constants.INITIAL_CONFIG_FILE_PATH));
        JSONObject initialConfigJson = (JSONObject) obj;
        List<ShipController> shipControllers = new ArrayList<>();
        JSONArray listOfShips = (JSONArray) initialConfigJson.get("ships");
        Iterator it = listOfShips.iterator();
        Integer idNumber = 1;
        Integer offset = 0;
        while(it.hasNext()){
            shipControllers.add(createShipController((JSONObject) it.next(), idNumber, offset));
            idNumber++;
            offset += Constants.SHIP_SPAWN_POSITION_OFFSET;
        }
        return shipControllers;
    }

    private ShipController createShipController(JSONObject initialConfigJson, Integer shipIdNumber, Integer offset) {
        String shipSkin = (String) initialConfigJson.get("skin");
        Integer shipHealth = ((Long) initialConfigJson.get("health")).intValue();
        String weaponType = (String) initialConfigJson.get("weaponType");

        ShipMover shipMover = createShipMover(shipSkin, shipHealth, shipIdNumber, offset);
        Weapon weapon = WeaponFactory.createWeaponForType(WeaponType.valueOf(weaponType), shipMover.getMover());

        return new ShipController(shipMover, weapon);


    }

    private static ShipMover createShipMover(String shipSkin, Integer shipHealth, Integer shipIdNumber, Integer offset) {
        Mover<Ship> mover = new Mover<>(
                new Ship("Ship-"+shipIdNumber, shipHealth, shipSkin),
                new Position(Constants.STARTING_X_COORD+offset, Constants.STARTING_Y_COORD),
                new Vector(0D,0D),
                new Vector(0D),
                new StarshipUIAdapter()
        );

        ShipMover shipMover = new ShipMover(mover);
        return shipMover;
    }
}
