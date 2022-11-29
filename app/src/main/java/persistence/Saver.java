package persistence;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import starships.GameState;
import starships.entities.ship.ShipController;
import starships.movement.Mover;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Saver {

    public void saveGameState(GameState gameState) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scores", gameState.getScores());
        jsonObject.put("removedIds", gameState.getRemovedIds());
        jsonObject.put("ships", gameState.getShips());
        System.out.println(jsonObject.toJSONString());

    }

    public GameState readGameState() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(Constants.SAVE_FILE_PATH));
        JSONObject saveJson = (JSONObject) obj;

        List<String> removedIds = readRemovedIds((JSONArray) saveJson.get("removedIds"));
        Map<String, Integer> scores = (Map) saveJson.get("scores");

        List<ShipController> shipControllers = readShipControllers(saveJson);
        List<Mover> movingEntities = readMovingEntities(saveJson);
        return new GameState(movingEntities, shipControllers, removedIds, scores);

    }

    private List<Mover> readMovingEntities(JSONObject saveJson) {
        return new MoverJsonReader().readMovers((JSONArray) saveJson.get("moving-entities"));
    }

    private List<String> readRemovedIds(JSONArray removedIdsArray){
        List<String> removedIdsList = new ArrayList<>();
        for (Object o : removedIdsArray) {
            removedIdsList.add((String) o);
        }
        return removedIdsList;
    }

    private static List<ShipController> readShipControllers(JSONObject saveJson) {
        return new ShipJsonReader().readShipControllers((JSONArray) saveJson.get("ships"));
    }

}
