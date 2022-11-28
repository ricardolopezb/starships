package persistence;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class WindowConfigurator {
    private final JSONObject properties;


    public WindowConfigurator() {
        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(Constants.INITIAL_CONFIG_FILE_PATH));
        } catch (IOException e) {
            System.out.println("Initial Config. File not found");;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject generalJSON = (JSONObject) obj;
        this.properties = (JSONObject) generalJSON.get("window");
    }

    public Optional<Object> getProperty(String property){
        Object result = this.properties.get(property);
        return result != null ? Optional.of(result) : Optional.empty();
    }
}
