package starships.movement;

import edu.austral.ingsis.starships.ui.ElementModel;
import adapters.CoreEntityToUIElementAdapter;
import starships.entities.BaseEntity;
import starships.physics.Position;
import starships.physics.Vector;

public class Mover<T extends BaseEntity> {
    private final T entity;
    private final Position position;
    private final Vector movementVector;
    private final Vector facingDirection;
    private final CoreEntityToUIElementAdapter<T> adapter;

    public Mover(T entity, Position position, Vector movementVector, Vector facingDirection, CoreEntityToUIElementAdapter<T> adapter) {
        this.entity = entity;
        this.position = position;
        this.movementVector = movementVector;
        this.facingDirection = facingDirection;
        this.adapter = adapter;
    }

    public Mover<T> updatePosition(){
        Position newPosition = new Position((int)(this.position.getX()+this.movementVector.getX()), (int)(this.position.getY()+this.movementVector.getY()));
        //System.out.println(this.entity.getId() +" new pos: x = " + newPosition.getX() + " y = " + newPosition.getY());
        return new Mover<>(this.entity, newPosition, this.movementVector, this.facingDirection, this.adapter);
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

    public CoreEntityToUIElementAdapter<T> getAdapter() {
        return adapter;
    }

    public ElementModel adapt() {
        return this.adapter.adapt(this);
    }
}
