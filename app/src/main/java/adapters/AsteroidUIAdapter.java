package adapters;

import edu.austral.ingsis.starships.ui.ElementColliderType;
import edu.austral.ingsis.starships.ui.ElementModel;
import edu.austral.ingsis.starships.ui.ImageRef;
import starships.entities.Asteroid;
import starships.movement.Mover;

public class AsteroidUIAdapter implements CoreEntityToUIElementAdapter<Asteroid>{
    @Override
    public ElementModel adapt(Mover<Asteroid> mover) {
        Integer size = mover.getEntity().getSize();
        String selectedImage = Math.random() <= 0.5 ? "asteroid" : "satellite";
        return new ElementModel(
                mover.getId(),
                mover.getPosition().getX(),
                mover.getPosition().getY(),
                size,
                size,
                mover.getFacingDirection().getDegrees(),
                ElementColliderType.Elliptical,
                new ImageRef(selectedImage, size, size)
        );

    }
}
