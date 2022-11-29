package persistence.visitor;

import starships.GameState;
import starships.entities.Asteroid;
import starships.entities.bullet.Bullet;
import starships.entities.ship.Ship;
import starships.entities.ship.ShipController;
import starships.entities.weapon.Weapon;
import starships.movement.Mover;
import starships.movement.ShipMover;
import starships.physics.Position;
import starships.physics.Vector;

public interface Visitor<T> {
    T visitShip(Ship ship);
    T visitAsteroid(Asteroid asteroid);
    T visitBullet(Bullet bullet);

    T visitGameState(GameState gameState);
    T visitMover(Mover mover);
    T visitWeapon(Weapon weapon);
    T visitShipController(ShipController shipController);
    T visitShipMover(ShipMover shipMover);
    T visitPosition(Position position);
    T visitVector(Vector vector);


}
