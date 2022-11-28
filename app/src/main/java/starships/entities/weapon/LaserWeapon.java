//package starships.entities.weapon;
//
//import starships.entities.bullet.Bullet;
//import starships.entities.bullet.LaserBullet;
//import starships.movement.spawners.movers.MultipleSpawner;
//import starships.entities.spawners.MultipleSpawnerFactory;
//import starships.movement.Mover;
//import starships.physics.Position;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LaserWeapon implements Weapon{
//
//    private final MultipleSpawner<Bullet> bulletSpawner;
//    private final Integer bulletsPerShot;
////    private final Double shotSpeed;
////
////    public LaserWeapon(Integer bulletsPerShot, ShotType shotType, Double shotSpeed, Position currentPosition) {
////        this.bulletsPerShot = bulletsPerShot;
////        this.shotSpeed = shotSpeed;
////        final MultipleSpawnerFactory<Bullet> factory = new MultipleSpawnerFactory<>();
////        this.bulletSpawner = factory.getSpawnerForShotType(shotType, currentPosition, shotSpeed);
////    }
//
//    public LaserWeapon(ShotType shotType, Integer bulletsPerShot, Double shotSpeed, Position position) {
//        this.bulletsPerShot = bulletsPerShot;
//        final MultipleSpawnerFactory<Bullet> factory = new MultipleSpawnerFactory<>();
//        this.bulletSpawner = factory.getSpawnerForShotType(shotType, position, shotSpeed);
//    }
//
//    @Override
//    public List<Mover<Bullet>> shoot() {
//        List<Bullet> entitiesToShoot = new ArrayList<>(bulletsPerShot);
//        for (int i = 0; i < bulletsPerShot; i++) {
//            entitiesToShoot.add(new LaserBullet());
//        }
//        return bulletSpawner.spawnMultiple(entitiesToShoot);
//    }
//}

//TODO cual sera mejor?

/**
 * El codigo entre las distintas clases de weapon se ven muy parecidas.
 * Los constructores son iguales, hacen lo mismo y el metodo shoot()
 * solo cambia el tipo de Bullet que se hace, aunque devuelven lo mismo.
 *
 * De hecho, las distintas clases de Bullet pueden ser solo ellas con un valor determinado,
 * y no necesariamente una clase que hardcodee los valores.
 *
 *
 *
 * Hacer que el ShipMover tenga la weapon y no la ship, entonces es mas facil moverse tdo junto compuestamente
 * o algo que sea Movable, como un composite,
 *
 * entonces la Ship seria movable, el weapon seria movable, la bullet seria movable.
 *
 * que la weapon este en el ShipMover es ver que la weapon esta alojada en la posicion de la nave, no en la nave per se
 * el ShipMover es como el punto que representa a la nave en el mapa
 *
 *
 * otra:
 * class ShipController {
 *     ShipMover shipMover;
 *     Weapon weapon;
 *
 *     + accelerate()
 *     + rotate()
 *     + changeWeapon()
 *     + update()
 * }
 *
 * Con esta, se trata a la Weapon como una entidad independiente que esta linkeada a la posicion del shipMover
 * La hitbox se haria sobre este objeto.
 *
 *
 * Para una factory, es mejor hacer constructores dinamicos o estaticos?
 *
 */


//class Weapon{
//    private final MultipleSpawner<Bullet> bulletSpawner;
//    private final Integer bulletsPerShot;
//
//    public Weapon(BulletType bulletType, ShotType shotType, Integer bulletsPerShot, Double shotSpeed, Position position) {
//        this.bulletsPerShot = bulletsPerShot;
//        final MultipleSpawnerFactory<Bullet> factory = new MultipleSpawnerFactory<>();
//        this.bulletSpawner = factory.getSpawnerForShotType(shotType, position, shotSpeed);
//          this.bulletFactory = new BulletFactory(bulletType)
//    }
//        @Override
//        public List<Mover<Bullet>> shoot() {
//            List<Bullet> bullets = bulletFactory.createBullets(bulletsPerShot)
//            return bulletSpawner.spawnMultiple(entitiesToShoot);
//        }

//
//}