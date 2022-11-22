package starships.adapters;

import edu.austral.ingsis.starships.ui.ElementColliderType;
import edu.austral.ingsis.starships.ui.ElementModel;
import edu.austral.ingsis.starships.ui.ImageRef;
import starships.entities.bullet.Bullet;
import starships.movement.Mover;

public class BulletUIAdapter implements CoreEntityToUIElementAdapter<Bullet>{
    @Override
    public ElementModel adapt(Mover<Bullet> mover) {
        return new ElementModel(
                mover.getId(),
                mover.getPosition().getX(),
                mover.getPosition().getY(),
                10.0,
                10.0,
                mover.getFacingDirection().getDegrees(),
                ElementColliderType.Rectangular,
                new ImageRef("bullet", 20.0, 20.0)
        );
    }
}
