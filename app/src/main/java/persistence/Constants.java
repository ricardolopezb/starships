package persistence;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class Constants {
    public static final Integer SHIP_COLLISION_DAMAGE = 10;
    public static final Integer LASER_BULLET_DAMAGE = 15;
    public static final Integer EXPLOSIVE_BULLET_DAMAGE = 20;

    public static final Integer LASER_WEAPON_BULLETS_PER_SHOT = 2;
    public static final Double LASER_WEAPON_SHOT_SPEED = 5.0;
    public static final Integer EXPLOSIVE_WEAPON_BULLETS_PER_SHOT = 4;
    public static final Double EXPLOSIVE_WEAPON_SHOT_SPEED = 2.0;


    public static final Double ASTEROID_SPEED = 1.1; //1.2
    public static final Double SHIP_ACCELERATION_COEFFICIENT = 0.5;
    public static final Double SHIP_BRAKE_COEFFICIENT = -0.4;
    public static final Integer SHIP_ROTATION_DEGREES = 20;


    public static final Double ASTEROID_SPAWN_RATE = 0.003; //0.001

    public static final String KEYBINDINGS_FILE_PATH = System.getProperty("user.dir") +
            "/app/src/main/java/persistence/keybindings.json";

    public static final String INITIAL_CONFIG_FILE_PATH = System.getProperty("user.dir") +
            "/app/src/main/java/persistence/initial_config_file.json";


    public static final Integer SHIP_SPAWN_POSITION_OFFSET = 70;
    public static final Integer STARTING_X_COORD = 300;
    public static final Integer STARTING_Y_COORD = 300;
    public static final String SAVE_FILE_PATH = System.getProperty("user.dir") +
            "/src/main/java/persistence/save_file.json";;
}
