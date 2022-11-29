package persistence.gamestate;

import adapters.AsteroidUIAdapter;
import adapters.BulletUIAdapter;
import adapters.CoreEntityToUIElementAdapter;
import adapters.StarshipUIAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import starships.entities.Asteroid;
import starships.entities.BaseEntity;
import starships.entities.EntityType;
import starships.entities.bullet.Bullet;
import starships.entities.bullet.BulletType;
import starships.entities.ship.Ship;
import starships.entities.ship.ShipController;
import starships.movement.Mover;
import starships.physics.Position;
import starships.physics.Vector;

import java.util.ArrayList;
import java.util.List;

public class MoverJsonReader {

    public List<Mover> readMovers(JSONArray moversJson) {
        List<Mover> moversToReturn = new ArrayList<>();
        for (Object mover : moversJson) {
            moversToReturn.add(readMoverJson((JSONObject) mover));
        }
        return moversToReturn;
    }


    private Mover readMoverJson(JSONObject moverJson) {
        JSONObject entityJson = (JSONObject) moverJson.get("entity");
        BaseEntity entity = readEntity(entityJson);
        Position position = readPositionJson((JSONObject) moverJson.get("position"));
        Vector movingVector = readVectorJson((JSONObject) moverJson.get("movement-vector"));
        Vector facingDirection = readVectorJson((JSONObject) moverJson.get("facing-direction"));

        return createMover(entity, position, movingVector, facingDirection);
    }

    private BaseEntity readEntity(JSONObject entityJson) {
        EntityType entityType = EntityType.valueOf((String) entityJson.get("entity-type"));
        return switch (entityType){
            case SHIP -> readShip(entityJson);
            case BULLET -> readBullet(entityJson);
            case ASTEROID -> readAsteroid(entityJson);
        };

    }

    private BaseEntity readAsteroid(JSONObject entityJson) {
        String id = (String) entityJson.get("id");
        Integer health = ((Long) entityJson.get("health")).intValue();
        Integer size = ((Long) entityJson.get("size")).intValue();
        return new Asteroid(id, size, health);
    }

    private BaseEntity readBullet(JSONObject entityJson) {
        String id = (String) entityJson.get("id");
        BulletType type = BulletType.valueOf((String) entityJson.get("bullet-type"));
        Integer damage = ((Long) entityJson.get("damage")).intValue();
        Integer size = ((Long) entityJson.get("size")).intValue();
        String ownerId = (String) entityJson.get("ownerId");

        return new Bullet(id, type, damage, size, ownerId);
    }

    private BaseEntity readShip(JSONObject entityJson) {
        String id = (String) entityJson.get("id");
        Integer health = ((Integer) entityJson.get("health"));
        String skin = (String) entityJson.get("skin");
        return new Ship(id, health, skin);
    }

    @NotNull
    private static Mover<BaseEntity> createMover(BaseEntity entity, Position position, Vector movingVector, Vector facingDirection) {

        CoreEntityToUIElementAdapter adapter = getAdapter(entity);
        return new Mover<BaseEntity>(
                entity,
                position,
                movingVector,
                facingDirection,
                adapter
        );
    }

    @NotNull
    private static CoreEntityToUIElementAdapter getAdapter(BaseEntity entity) {
        return switch (entity.getEntityType()) {
            case SHIP -> new StarshipUIAdapter();
            case BULLET -> new BulletUIAdapter();
            case ASTEROID -> new AsteroidUIAdapter();
        };
    }

    private Vector readVectorJson(JSONObject vectorJson) {
        Double x = (Double) vectorJson.get("x");
        Double y = (Double) vectorJson.get("y");
        return new Vector(x,y);
    }

    private Position readPositionJson(JSONObject position) {
        Integer x = ((Long) position.get("x")).intValue();
        Integer y = ((Long) position.get("y")).intValue();
        return new Position(x,y);
    }

}
