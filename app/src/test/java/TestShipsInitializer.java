import org.junit.Test;
import persistence.ShipsInitializer;

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
