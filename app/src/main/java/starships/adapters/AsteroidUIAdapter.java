package starships.adapters;

import edu.austral.ingsis.starships.ui.ElementColliderType;
import edu.austral.ingsis.starships.ui.ElementModel;
import edu.austral.ingsis.starships.ui.ImageRef;
import starships.entities.Asteroid;
import starships.entities.BaseEntity;
import starships.movement.Mover;

public class AsteroidUIAdapter implements CoreEntityToUIElementAdapter<Asteroid>{
    @Override
    public ElementModel adapt(Mover<Asteroid> mover) {
        return new ElementModel(
                mover.getId(),
                mover.getPosition().getX(),
                mover.getPosition().getY(),
                70,
                70,
                mover.getFacingDirection().getDegrees(),
                ElementColliderType.Elliptical,
                new ImageRef("asteroid", 70, 70)
        );

    }
}
