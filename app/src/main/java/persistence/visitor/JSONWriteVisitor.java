package persistence.visitor;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import persistence.Constants;
import starships.GameState;
import starships.entities.Asteroid;
import starships.entities.bullet.Bullet;
import starships.entities.ship.Ship;
import starships.entities.ship.ShipController;
import starships.entities.weapon.Weapon;
import starships.entities.weapon.WeaponDTO;
import starships.movement.Mover;
import starships.movement.ShipMover;
import starships.physics.Position;
import starships.physics.Vector;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class JSONWriteVisitor implements Visitor<JSONObject> {
    @Override
    public JSONObject visitShip(Ship ship) {
        JSONObject obj = new JSONObject();
        obj.put("id", ship.getId());
        obj.put("entity-type", ship.getEntityType().name());
        obj.put("skin", ship.getSkin());
        obj.put("health", ship.getHealth());
        return obj;
    }

    @Override
    public JSONObject visitAsteroid(Asteroid asteroid) {
        JSONObject obj = new JSONObject();
        obj.put("id", asteroid.getId());
        obj.put("entity-type", asteroid.getEntityType().name());
        obj.put("size", asteroid.getSize());
        obj.put("damage", asteroid.getDamage());
        obj.put("health", asteroid.getHealth());
        return obj;
    }

    @Override
    public JSONObject visitBullet(Bullet bullet) {
        JSONObject obj = new JSONObject();
        obj.put("id", bullet.getId());
        obj.put("entity-type", bullet.getEntityType().name());
        obj.put("size", bullet.getSize());
        obj.put("damage", bullet.getDamage());
        obj.put("ownerId", bullet.getOwnerId());
        obj.put("bullet-type", bullet.getBulletType().name());
        return obj;
    }

    @Override
    public JSONObject visitGameState(GameState gameState) {
        JSONObject saveObj = new JSONObject();
        saveObj.put("scores", gameState.getScores());
        saveObj.put("removedIds", gameState.getRemovedIds());

        List<JSONObject> shipControllersJsons = getShipControllersJsons(gameState);

        saveObj.put("ships", shipControllersJsons);

        List<Object> moversJsons = getMoversJsons(gameState);
        saveObj.put("moving-entities", moversJsons);

        return saveObj;
    }



    @NotNull
    private List<Object> getMoversJsons(GameState gameState) {
        return gameState.getMovingEntities().stream().map(mover -> mover.accept(this)).toList();
    }

    @NotNull
    private List<JSONObject> getShipControllersJsons(GameState gameState) {
        return gameState.getShips().stream().map(shipController -> shipController.accept(this)).toList();
    }

    @Override
    public JSONObject visitMover(Mover mover) {
        JSONObject obj = new JSONObject();
        obj.put("entity", mover.getEntity().accept(this));
        obj.put("position", mover.getPosition().accept(this));
        obj.put("movement-vector", mover.getMovementVector().accept(this));
        obj.put("facing-direction", mover.getFacingDirection().accept(this));
        return obj;
    }

    @Override
    public JSONObject visitWeapon(Weapon weapon) {
        WeaponDTO dto = WeaponDTO.fromWeapon(weapon);
        return dto.toJson();
    }

    @Override
    public JSONObject visitShipController(ShipController shipController) {
        JSONObject obj = new JSONObject();
        obj.put("ship-mover", shipController.getShipMover().accept(this));
        obj.put("weapon", shipController.getWeapon().accept(this));
        return obj;
    }

    @Override
    public JSONObject visitShipMover(ShipMover shipMover) {
        JSONObject obj = new JSONObject();
        obj.put("mover", shipMover.getMover().accept(this));
        return obj;
    }

    @Override
    public JSONObject visitPosition(Position position) {
        JSONObject obj = new JSONObject();
        obj.put("x", position.getX());
        obj.put("y", position.getY());
        return obj;

    }

    @Override
    public JSONObject visitVector(Vector vector) {
        JSONObject obj = new JSONObject();
        obj.put("x", vector.getX());
        obj.put("y", vector.getY());
        return obj;
    }
}
