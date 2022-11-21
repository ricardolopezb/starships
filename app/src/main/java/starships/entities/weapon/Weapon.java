package starships.entities.weapon;

import starships.entities.Ship;
import starships.entities.bullet.BulletFactory;
import starships.entities.bullet.BulletType;
import starships.entities.spawners.MultipleSpawner;
import starships.entities.spawners.MultipleSpawnerFactory;
import starships.movement.Mover;
import starships.entities.bullet.Bullet;
import starships.physics.Position;

import java.util.List;

public class Weapon {
    private final BulletFactory bulletFactory;
    private final WeaponType weaponType;
    private final ShotType shotType;
    private final MultipleSpawner<Bullet> multipleSpawner;
    private final Integer bulletsPerShot;

    // Aca le estoy pasando el mover al SpawnerFactory, para que con el pueda obtener lo que necesita para cada uno
    //para el radial, solo toma la posicion,
    // para el linear toma tambien la facing direction
    // me hace ruido hacerlo asi
    public Weapon(WeaponType weaponType, BulletType bulletType, ShotType shotType, Integer bulletsPerShot, Double shotSpeed, Mover<Ship> mover) {
        this.bulletFactory = new BulletFactory(bulletType);
        this.weaponType = weaponType;
        this.shotType = shotType;
        final MultipleSpawnerFactory<Bullet> factory = new MultipleSpawnerFactory<>();
        this.multipleSpawner = factory.getSpawnerForShotType(shotType, mover, shotSpeed);
        this.bulletsPerShot = bulletsPerShot;
    }

    public Weapon(WeaponDTO dto, Mover<Ship> mover){
        this.bulletFactory = new BulletFactory(dto.bulletType());
        this.weaponType = dto.weaponType();
        this.shotType = dto.shotType();
        final MultipleSpawnerFactory<Bullet> factory = new MultipleSpawnerFactory<>();
        this.multipleSpawner = factory.getSpawnerForShotType(shotType, mover, dto.shotSpeed());
        this.bulletsPerShot = dto.bulletsPerShot();
    }

    public List<Mover<Bullet>> shoot(){
        List<Bullet> bullets = bulletFactory.generateBullets(bulletsPerShot);
        return multipleSpawner.spawnMultiple(bullets);
    }

    public BulletFactory getBulletFactory() {
        return bulletFactory;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public ShotType getShotType() {
        return shotType;
    }

    public MultipleSpawner<Bullet> getMultipleSpawner() {
        return multipleSpawner;
    }
}
