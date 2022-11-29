package persistence;

import adapters.StarshipUIAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import starships.entities.EntityType;
import starships.entities.ship.Ship;
import starships.entities.ship.ShipController;
import starships.entities.weapon.Weapon;
import starships.entities.weapon.WeaponDTO;
import starships.movement.Mover;
import starships.movement.ShipMover;
import starships.physics.Position;
import starships.physics.Vector;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShipJsonReader {


    public List<ShipController> readShipControllers(JSONArray ships) {
        List<ShipController> shipControllers = new ArrayList<>();
        for (Object ship : ships) {
            shipControllers.add(readShipController((JSONObject) ship));
        }
        return shipControllers;
    }

    private ShipController readShipController(JSONObject initialConfigJson) {
        ShipMover shipMover = readShipMover((JSONObject)initialConfigJson.get("ship-mover"));
        Weapon weapon = readWeapon((JSONObject) initialConfigJson.get("weapon"), shipMover.getMover());


//        String shipSkin = (String) initialConfigJson.get("skin");
//        Integer shipHealth = ((Long) initialConfigJson.get("health")).intValue();
//        String weaponType = (String) initialConfigJson.get("weaponType");
//
//        ShipMover shipMover = createShipMover(shipSkin, shipHealth, shipIdNumber, offset);
//        Weapon weapon = WeaponFactory.createWeaponForType(WeaponType.valueOf(weaponType), shipMover.getMover());

        return new ShipController(shipMover, weapon);


    }

    private Weapon readWeapon(JSONObject jsonObject, Mover<Ship> mover) {
        WeaponDTO dto = WeaponDTO.fromJson(jsonObject);
        return new Weapon(dto, mover);
    }

    private ShipMover readShipMover(JSONObject jsonObject) {
        JSONObject moverJson = (JSONObject) jsonObject.get("mover");
        Mover<Ship> mover = readMover(moverJson);
        return new ShipMover(mover);
    }

    private Mover<Ship> readMover(JSONObject moverJson) {
        JSONObject shipJson = (JSONObject) moverJson.get("entity");
        Ship ship = readShipJson(shipJson);
        Position position = readPositionJson((JSONObject) moverJson.get("position"));
        Vector movingVector = readVectorJson((JSONObject) moverJson.get("movement-vector"));
        Vector facingDirection = readVectorJson((JSONObject) moverJson.get("facing-direction"));

        return createMover(ship, position, movingVector, facingDirection);
    }

    @NotNull
    private static Mover<Ship> createMover(Ship ship, Position position, Vector movingVector, Vector facingDirection) {
        return new Mover<>(
                ship,
                position,
                movingVector,
                facingDirection,
                new StarshipUIAdapter()
        );
    }

    private Vector readVectorJson(JSONObject vectorJson) {
        Double x = (Double) vectorJson.get("x");
        Double y = (Double) vectorJson.get("y");
        return new Vector(x,y);
    }

    private Position readPositionJson(JSONObject position) {
        Integer x = ((Long) position.get("x")).intValue();
        Integer y = ((Long) position.get("y")).intValue();
        return new Position(x,y);
    }

    public Ship readShipJson(JSONObject shipJson) {
        String id = (String) shipJson.get("id");
        Integer health = ((Long) shipJson.get("health")).intValue();
        String skin = (String) shipJson.get("skin");
        return new Ship(id, health, skin);
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
