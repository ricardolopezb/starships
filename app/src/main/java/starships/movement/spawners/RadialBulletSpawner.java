package starships.movement.spawners;

import starships.adapters.BulletUIAdapter;
import starships.entities.bullet.Bullet;
import starships.movement.Mover;
import starships.physics.Position;
import starships.physics.Vector;

import java.util.ArrayList;
import java.util.List;

public class RadialBulletSpawner extends AbstractSpawner implements MultipleSpawner<Bullet>{

    public RadialBulletSpawner(Position position) {
        super(position);
    }

    public RadialBulletSpawner(Position position, Double speed) {
        super(position, speed);
    }

    @Override
    public List<Mover<Bullet>> spawnMultiple(List<Bullet> entities) {
        int quantity = entities.size();
        double arcInterval = 360d / quantity;
        List<Mover<Bullet>> movers = new ArrayList<>(quantity);
        double currentAngle = 0D;
        for (Bullet entity : entities) {
            movers.add(new Mover<>(entity, position, new Vector(currentAngle).multiply(speed), new Vector(currentAngle), new BulletUIAdapter()));
            currentAngle += arcInterval;
        }
        return movers;
    }
}
