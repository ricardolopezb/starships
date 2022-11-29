import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import persistence.GameStateSaver;
import persistence.ShipJsonReader;
import persistence.visitor.JSONWriteVisitor;
import persistence.visitor.Visitor;
import starships.GameState;
import starships.entities.ship.Ship;

import java.io.IOException;

public class SavingTest {

    @Test
    public void testShipSaving(){
        Ship ship = new Ship("id1", 500, "basic");
        Visitor<JSONObject> v = new JSONWriteVisitor();
        JSONObject shipJson = ship.accept(v);
        //JSONObject shipJson = ship.toJson();
        ShipJsonReader r = new ShipJsonReader();
//        System.out.println(shipJson.toJSONString());
        Assert.assertEquals(ship, r.readShipJson(shipJson));
    }

    @Test
    public void testGameStateRead() throws IOException, ParseException {
        GameStateSaver s = new GameStateSaver();
        GameState gs = s.readGameState();
        System.out.println("lmao");
    }




}
