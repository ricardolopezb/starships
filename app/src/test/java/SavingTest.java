import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import persistence.ShipJsonReader;
import persistence.visitor.JSONWriteVisitor;
import persistence.visitor.Visitor;
import starships.entities.ship.Ship;

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


}
