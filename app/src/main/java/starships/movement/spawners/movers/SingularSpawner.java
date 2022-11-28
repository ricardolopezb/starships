package starships.movement.spawners.movers;

import starships.entities.BaseEntity;
import starships.movement.Mover;
import starships.physics.Vector;

public interface SingularSpawner<T extends BaseEntity> {

    Mover<T> spawn(Vector movementDirection);

}
