package starships.entities.spawners;

import starships.adapters.BulletUIAdapter;
import starships.entities.BaseEntity;
import starships.entities.bullet.Bullet;
import starships.movement.Mover;
import starships.physics.Position;
import starships.physics.Vector;

import java.util.ArrayList;
import java.util.List;

public class LinearBulletSpawner extends AbstractSpawner implements MultipleSpawner<Bullet> {

    private final Vector facingDirection;

    public LinearBulletSpawner(Position position, Double speed, Vector facingDirection){
        super(position, speed);
        this.facingDirection = facingDirection;
    }

    // TODO see how to get facingDirection to shoot to front
    @Override
    public List<Mover<Bullet>> spawnMultiple(List<Bullet> entities) {
        int quantity = entities.size();
        int separation = 15; // number that serves as parameter, can be modified
        Integer deltaX = 2;
        Integer deltaY = 2;
        Position separationPos = new Position(deltaX, deltaY);
        List<Mover<Bullet>> movers = new ArrayList<>(quantity);

        int i = 0;
        if(quantity % 2 != 0) {
            movers.add(new Mover<>(entities.get(i), position, facingDirection.multiply(speed), facingDirection, new BulletUIAdapter()));
            i++;

        }

        for (; i < quantity; i++) {
            movers.add(new Mover<>(entities.get(i), position.sum(separationPos), facingDirection.multiply(speed), facingDirection, new BulletUIAdapter()));
            separationPos = separationPos.multiply(-1d);

        }
        return movers;

    }
}
