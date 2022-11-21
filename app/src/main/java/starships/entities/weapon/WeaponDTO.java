package starships.entities.weapon;

import starships.entities.bullet.BulletType;


/**
 * Este DTO es para poder updatear una Weapon en el ShipController sin
 * tener informacion sobre su Mover
 * */


public record WeaponDTO(
        WeaponType weaponType,
        BulletType bulletType,
        ShotType shotType,
        Integer bulletsPerShot,
        Double shotSpeed
) {
}
