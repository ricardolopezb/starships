package adapters;

import edu.austral.ingsis.starships.ui.ElementColliderType;
import edu.austral.ingsis.starships.ui.ElementModel;
import edu.austral.ingsis.starships.ui.ImageRef;
import starships.entities.bullet.Bullet;
import starships.entities.bullet.BulletType;
import starships.movement.Mover;

public class BulletUIAdapter implements CoreEntityToUIElementAdapter<Bullet>{
    @Override
    public ElementModel adapt(Mover<Bullet> mover) {

        BulletType type = mover.getEntity().getType();
        ImageRef bulletPicture = switch (type) {
            case LASER -> new ImageRef("laser_bullet", 20, 20);
            case EXPLOSIVE -> new ImageRef("explosive_bullet", 20, 20);
            //case PLASMA -> new ImageRef("plasma_bullet", 20,20);
        };


        return new ElementModel(
                mover.getId(),
                mover.getPosition().getX(),
                mover.getPosition().getY(),
                10.0,
                10.0,
                mover.getFacingDirection().getDegrees(),
                ElementColliderType.Rectangular,
                bulletPicture
        );
    }
}
