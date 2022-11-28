package starships.movement.spawners.movers;

import starships.entities.BaseEntity;
import starships.movement.Mover;
import starships.physics.Vector;

import java.util.List;

public interface MultipleSpawner<T extends BaseEntity> {

    List<Mover<T>> spawnMultiple(List<T> entities);
}
