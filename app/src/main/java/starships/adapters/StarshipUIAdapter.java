package starships.adapters;

import edu.austral.ingsis.starships.ui.ElementColliderType;
import edu.austral.ingsis.starships.ui.ElementModel;
import edu.austral.ingsis.starships.ui.ImageRef;
import starships.entities.Ship;
import starships.movement.Mover;

public class StarshipUIAdapter implements CoreEntityToUIElementAdapter<Ship> {



    @Override
    public ElementModel adapt(Mover<Ship> mover) {
        return new ElementModel(
                mover.getId(),
                mover.getPosition().getX(),
                mover.getPosition().getY(),
                40.0,
                40.0,
                mover.getFacingDirection().getDegrees() + 270,
                ElementColliderType.Triangular,
                new ImageRef("starship", 70.0, 70.0)
        );
    }
}
