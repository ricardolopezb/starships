package starships.movement;

import edu.austral.ingsis.starships.ui.ElementModel;
import starships.adapters.StarshipUIAdapter;
import starships.entities.Ship;
import starships.physics.Position;
import starships.physics.Vector;

public class ShipMover {
    private final Mover<Ship> mover;

    public ShipMover(Mover<Ship> shipMover) {
        this.mover = shipMover;
    }

    public ShipMover accelerate(Double coefficient){
        Vector currentMovement = mover.getMovementVector();
        Vector facingDirection = mover.getFacingDirection();
        Vector accelerationDirection = facingDirection.multiply(coefficient);
        Vector newCurrentMovement = currentMovement.sum(accelerationDirection);

        Mover<Ship> newMover = new Mover<>(mover.getEntity(), mover.getPosition(), newCurrentMovement, mover.getFacingDirection(), mover.getAdapter());

        return new ShipMover(newMover);

    }

    // degrees is the difference in degrees between current facing direction and the desired one
    public ShipMover rotate(Integer degrees){
        Vector facingDirection = mover.getFacingDirection();
        Vector newFacingDirection = new Vector(facingDirection.getDegrees() + degrees);
        Mover<Ship> newMover = new Mover<>(mover.getEntity(), mover.getPosition(), mover.getMovementVector(), newFacingDirection, mover.getAdapter());
        return new ShipMover(newMover);
    }

    public ShipMover updatePosition(){
        return new ShipMover(mover.updatePosition());
    }

    public Mover<Ship> getMover() {
        return this.mover;
    }

    public Position getPosition() {
        return this.mover.getPosition();
    }

    public Vector getMovementVector() {
        return this.mover.getMovementVector();
    }
    public Vector getFacingDirection() {
        return this.mover.getFacingDirection();
    }

    public String getId() {
        return mover.getId();
    }

    public ElementModel adapt() {
        return this.mover.adapt();
    }
}
