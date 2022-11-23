package starships.entities.spawners;

import starships.entities.ship.Ship;
import starships.entities.bullet.Bullet;
import starships.entities.weapon.ShotType;
import starships.movement.Mover;

public class MultipleBulletSpawnerFactory {

    public MultipleSpawner<Bullet> getSpawnerForShotType(ShotType shotType, Mover<Ship> mover, Double shotSpeed){
        MultipleSpawner<Bullet> toReturn = null;
        switch (shotType){
            case LINEAR -> toReturn = new LinearBulletSpawner(mover.getPosition(), shotSpeed, mover.getFacingDirection());
            case RADIAL -> toReturn = new RadialBulletSpawner(mover.getPosition(), shotSpeed);
        }
        return toReturn;
    }
}
