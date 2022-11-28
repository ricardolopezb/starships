package starships.entities.weapon;

import starships.entities.ship.Ship;
import starships.entities.bullet.BulletType;
import starships.movement.Mover;
import persistence.Constants;

public class WeaponFactory {

    public static Weapon createWeaponForType(WeaponType type, Mover<Ship> mover){
        return switch(type) {
            case LASER -> new Weapon(
                    WeaponType.LASER,
                    BulletType.LASER,
                    ShotType.LINEAR,
                    Constants.LASER_WEAPON_BULLETS_PER_SHOT,
                    Constants.LASER_WEAPON_SHOT_SPEED,
                    mover
            );

            case EXPLOSIVE -> new Weapon(
                    WeaponType.EXPLOSIVE,
                    BulletType.EXPLOSIVE,
                    ShotType.RADIAL,
                    Constants.EXPLOSIVE_WEAPON_BULLETS_PER_SHOT,
                    Constants.EXPLOSIVE_WEAPON_SHOT_SPEED,
                    mover
            );

        };
    }

}
