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

        BulletType type = mover.getEntity().getBulletType();
        Double height =0.0;
        ImageRef bulletPicture = null;
        switch (type) {
            case LASER -> {
                height = 5.45;
                bulletPicture = new ImageRef("laser_bullet", 5.45, 20);
            }
            case EXPLOSIVE -> {
                height = 20.0;
                bulletPicture = new ImageRef("unnamed", 20, 20);
            }
            //case PLASMA -> new ImageRef("plasma_bullet", 20,20);
        };


        return new ElementModel(
                mover.getId(),
                mover.getPosition().getX(),
                mover.getPosition().getY(),
                height, //5.45 para laser
                20,
                mover.getFacingDirection().getDegrees(),
                ElementColliderType.Rectangular,
                bulletPicture
        );
    }
}
