package starships.entities.weapon;

import starships.entities.Ship;
import starships.entities.bullet.BulletFactory;
import starships.entities.bullet.BulletType;
import starships.entities.spawners.MultipleSpawner;
import starships.entities.spawners.MultipleBulletSpawnerFactory;
import starships.movement.Mover;
import starships.entities.bullet.Bullet;

import java.util.List;

public class Weapon {
    private final BulletFactory bulletFactory;
    private final WeaponType weaponType;
    private final ShotType shotType;
    private final MultipleSpawner<Bullet> multipleSpawner;
    private final Integer bulletsPerShot;
    private final Double shotSpeed;

    // Aca le estoy pasando el mover al SpawnerFactory, para que con el pueda obtener lo que necesita para cada uno
    //para el radial, solo toma la posicion,
    // para el linear toma tambien la facing direction
    // me hace ruido hacerlo asi
    public Weapon(WeaponType weaponType, BulletType bulletType, ShotType shotType, Integer bulletsPerShot, Double shotSpeed, Mover<Ship> mover) {
        this.bulletFactory = new BulletFactory(bulletType);
        this.weaponType = weaponType;
        this.shotType = shotType;
        final MultipleBulletSpawnerFactory factory = new MultipleBulletSpawnerFactory();
        this.shotSpeed = shotSpeed;
        this.multipleSpawner = factory.getSpawnerForShotType(shotType, mover, shotSpeed);
        this.bulletsPerShot = bulletsPerShot;
    }

    public Weapon(WeaponDTO dto, Mover<Ship> mover){
        this.bulletFactory = new BulletFactory(dto.getBulletType());
        this.weaponType = dto.getWeaponType();
        this.shotType = dto.getShotType();
        this.shotSpeed = dto.getShotSpeed();
        final MultipleBulletSpawnerFactory factory = new MultipleBulletSpawnerFactory();
        this.multipleSpawner = factory.getSpawnerForShotType(shotType, mover, dto.getShotSpeed());
        this.bulletsPerShot = dto.getBulletsPerShot();
    }

    public List<Mover<Bullet>> shoot(String ownerId){
        List<Bullet> bullets = bulletFactory.generateBullets(bulletsPerShot, ownerId);
        return multipleSpawner.spawnMultiple(bullets);
    }

    public Double getShotSpeed(){return shotSpeed;}

    public BulletFactory getBulletFactory() {
        return bulletFactory;
    }

    public BulletType getBulletType(){return bulletFactory.getBulletType();}

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public ShotType getShotType() {
        return shotType;
    }

    public MultipleSpawner<Bullet> getMultipleSpawner() {
        return multipleSpawner;
    }

    public Integer getBulletsPerShot() {
        return bulletsPerShot;
    }
}
