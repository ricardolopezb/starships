package starships.movement.spawners;

import starships.physics.Position;

public abstract class AbstractSpawner {
    protected final Position position;
    protected final Double speed;

    public AbstractSpawner(Position position) {
        this.position = position;
        this.speed = 1d;
    }

    public AbstractSpawner(Position position, Double speed) {
        this.position = position;
        this.speed = speed;
    }
}
