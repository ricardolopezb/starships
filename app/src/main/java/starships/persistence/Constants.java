package starships.persistence;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class Constants {
    public static final Integer SHIP_COLLISION_DAMAGE = 10;
    public static final Double ASTEROID_SPEED = 1.2;
}
