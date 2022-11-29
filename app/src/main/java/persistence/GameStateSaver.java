package persistence;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import persistence.visitor.JSONWriteVisitor;
import starships.GameState;
import starships.entities.ship.ShipController;
import starships.movement.Mover;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GameStateSaver {

    public void saveGameState(GameState gameState) {
        JSONWriteVisitor jsonVisitor = new JSONWriteVisitor();
        JSONObject saveJson = gameState.accept(jsonVisitor);
        writeJson(saveJson);

    }
    private static void writeJson(JSONObject saveObj) {
        try {
            PrintWriter pw = new PrintWriter(Constants.WRITE_SAVE_FILE_PATH);
            pw.write(saveObj.toJSONString());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public GameState readGameState() {
        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(Constants.WRITE_SAVE_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject saveJson = (JSONObject) obj;

        List<String> removedIds = readRemovedIds((JSONArray) saveJson.get("removedIds"));
        Map<String, Integer> scores = readScores((JSONObject) saveJson.get("scores"));

        List<ShipController> shipControllers = readShipControllers(saveJson);
        List<Mover> movingEntities = readMovingEntities(saveJson);
        return new GameState(movingEntities, shipControllers, removedIds, scores);

    }

    private Map<String, Integer> readScores(JSONObject object) {
        Map<String, Integer> scores = new HashMap<>();
        Iterator<Map.Entry> it = object.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = it.next();
            scores.put((String)pair.getKey(), ((Long) pair.getValue()).intValue());
        }
        return scores;
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
