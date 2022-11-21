package starships.entities.spawners;

import starships.entities.BaseEntity;
import starships.movement.Mover;
import starships.physics.Position;
import starships.physics.Vector;

import java.util.ArrayList;
import java.util.List;

public class RadialSpawner<T extends BaseEntity> extends AbstractSpawner implements MultipleSpawner<T>{

    public RadialSpawner(Position position) {
        super(position);
    }

    public RadialSpawner(Position position, Double speed) {
        super(position, speed);
    }

    @Override
    public List<Mover<T>> spawnMultiple(List<T> entities) {
        int quantity = entities.size();
        double arcInterval = 360d / quantity;
        List<Mover<T>> movers = new ArrayList<>(quantity);
        double currentAngle = 0D;
        for (T entity : entities) {
            movers.add(new Mover<>(entity, position, new Vector(currentAngle).multiply(speed), new Vector(currentAngle)));
            currentAngle += arcInterval;
        }
        return movers;
    }
}
