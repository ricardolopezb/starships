package starships.persistence;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class WindowConfigurator {
    private final JSONObject properties;
    private final String INITIAL_CONFIG_FILE_PATH = System.getProperty("user.dir") +
            "/app/src/main/java/starships/persistence/initial_config_file.json";

    public WindowConfigurator() {
        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(INITIAL_CONFIG_FILE_PATH));
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
