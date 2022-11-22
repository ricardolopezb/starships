package starships.entities.weapon;

import starships.entities.bullet.BulletType;


/**
 * Este DTO es para poder updatear una Weapon en el ShipController sin
 * tener informacion sobre su Mover
 * */


public class WeaponDTO{
        private WeaponType weaponType;
        private BulletType bulletType;
        private ShotType shotType;
        private Integer bulletsPerShot;
        private Double shotSpeed;

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public void setBulletType(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public void setShotType(ShotType shotType) {
        this.shotType = shotType;
    }

    public void setBulletsPerShot(Integer bulletsPerShot) {
        this.bulletsPerShot = bulletsPerShot;
    }

    public void setShotSpeed(Double shotSpeed) {
        this.shotSpeed = shotSpeed;
    }

    public static WeaponDTO fromWeapon(Weapon weapon){
        WeaponDTO dto = new WeaponDTO();
        dto.setBulletsPerShot(weapon.getBulletsPerShot());
        dto.setWeaponType(weapon.getWeaponType());
        dto.setBulletType(weapon.getBulletType());
        dto.setShotType(weapon.getShotType());
        dto.setShotSpeed(weapon.getShotSpeed());
        return dto;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public ShotType getShotType() {
        return shotType;
    }

    public Integer getBulletsPerShot() {
        return bulletsPerShot;
    }

    public Double getShotSpeed() {
        return shotSpeed;
    }
}
