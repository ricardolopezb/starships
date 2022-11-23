package starships.persistence;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import starships.entities.ship.ShipController;
import starships.adapters.StarshipUIAdapter;
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
    private final String INITIAL_CONFIG_FILE_PATH = System.getProperty("user.dir") +
            "/app/src/main/java/starships/persistence/initial_config_file.json";

    public List<ShipController> createShipControllers() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(INITIAL_CONFIG_FILE_PATH));
        JSONObject initialConfigJson = (JSONObject) obj;
        List<ShipController> shipControllers = new ArrayList<>();
        JSONArray listOfShips = (JSONArray) initialConfigJson.get("ships");
        Iterator it = listOfShips.iterator();
        Integer idNumber = 1;
        while(it.hasNext()){
            shipControllers.add(createShipController((JSONObject) it.next(), idNumber));
            idNumber++;
        }
        return shipControllers;
    }

    private ShipController createShipController(JSONObject initialConfigJson, Integer shipIdNumber) {
        String shipSkin = (String) initialConfigJson.get("skin");
        Integer shipHealth = ((Long) initialConfigJson.get("health")).intValue();
        String weaponType = (String) initialConfigJson.get("weaponType");

        ShipMover shipMover = createShipMover(shipSkin, shipHealth, shipIdNumber);
        Weapon weapon = WeaponFactory.createWeaponForType(WeaponType.valueOf(weaponType), shipMover.getMover());

        return new ShipController(shipMover, weapon);


    }

    private static ShipMover createShipMover(String shipSkin, Integer shipHealth, Integer shipIdNumber) {
        Mover<Ship> mover = new Mover<>(
                new Ship("Ship-"+shipIdNumber, shipHealth, shipSkin),
                new Position(300, 300),
                new Vector(0D,0D),
                new Vector(0D),
                new StarshipUIAdapter()
        );

        ShipMover shipMover = new ShipMover(mover);
        return shipMover;
    }
}
