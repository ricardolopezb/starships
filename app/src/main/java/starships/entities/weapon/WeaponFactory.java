package starships.entities.weapon;

import starships.entities.Ship;
import starships.entities.bullet.BulletType;
import starships.movement.Mover;

public class WeaponFactory {

    public static Weapon createWeaponForType(WeaponType type, Mover<Ship> mover){
        return switch(type) {
            case LASER -> new Weapon(
                    WeaponType.LASER,
                    BulletType.LASER,
                    ShotType.LINEAR,
                    2,
                    2.0,
                    mover
            );

            case EXPLOSIVE -> new Weapon(
                    WeaponType.EXPLOSIVE,
                    BulletType.EXPLOSIVE,
                    ShotType.RADIAL,
                    4,
                    2.0,
                    mover
            );

        };
    }

}
