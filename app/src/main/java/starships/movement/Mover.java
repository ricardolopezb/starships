package starships.movement;

import starships.entities.BaseEntity;
import starships.physics.Position;
import starships.physics.Vector;

public class Mover<T extends BaseEntity> {
    private final T entity;
    private final Position position;
    private final Vector movementVector;
    private final Vector facingDirection;

    public Mover(T entity, Position position, Vector movementVector, Vector facingDirection) {
        this.entity = entity;
        this.position = position;
        this.movementVector = movementVector;
        this.facingDirection = facingDirection;
    }

    public Mover<T> updatePosition(){
        Position newPosition = new Position((int)(this.position.getX()+this.movementVector.getX()), (int)(this.position.getY()+this.movementVector.getY()));
        return new Mover<>(this.entity, newPosition, this.movementVector, this.facingDirection);
    }

    public Position getPosition() {
        return position;
    }

    public Vector getMovementVector() {
        return movementVector;
    }

    public Vector getFacingDirection() {
        return facingDirection;
    }

    public T getEntity() {
        return entity;
    }

    public String getId() {
        return this.entity.getId();
    }
}
