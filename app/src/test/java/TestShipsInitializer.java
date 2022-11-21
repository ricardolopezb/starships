import org.json.simple.parser.ParseException;
import org.junit.Test;
import starships.persistence.ShipsInitializer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TestShipsInitializer {

    @Test
    public void testSingleShipsInitialization(){
        try{
            ShipsInitializer s = new ShipsInitializer();
            s.createShipControllers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
