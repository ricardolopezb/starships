package starships.entities.ship;

import edu.austral.ingsis.starships.ui.ElementModel;
import starships.entities.bullet.Bullet;
import starships.entities.weapon.Weapon;
import starships.entities.weapon.WeaponDTO;
import starships.movement.Mover;
import starships.movement.ShipMover;

import java.util.List;
import java.util.Optional;

public class ShipController {
    private final ShipMover shipMover;
    private final Weapon weapon;


    public ShipController(ShipMover shipMover, Weapon weapon) {
        this.shipMover = shipMover;
        this.weapon = weapon;
    }

    public ElementModel adapt(){
        return this.shipMover.adapt();
    }

    public ShipController update(){
        ShipMover newShipMover = this.shipMover.updatePosition();
        return new ShipController(newShipMover, new Weapon(WeaponDTO.fromWeapon(this.weapon), newShipMover.getMover())); // todo confirmar si esto esta bien como inmutable

    }

    public ShipController changeWeapon(WeaponDTO weaponDTO){
        return new ShipController(this.shipMover, new Weapon(weaponDTO, this.shipMover.getMover()));
    }

    public ShipController accelerate(Double coefficient){
        return new ShipController(this.shipMover.accelerate(coefficient), weapon);
    }

    public ShipController rotate(Integer degrees){
        return new ShipController(this.shipMover.rotate(degrees), weapon);

    }

    public String getId(){
        return shipMover.getId();
    }

    public List<Mover<Bullet>> shoot(){
        return this.weapon.shoot(this.shipMover.getId());
    }

    public Optional<ShipController> takeDamage(Integer damage){
        Optional<Ship> newShip = this.shipMover.getMover().getEntity().takeDamage(damage);
        if(newShip.isEmpty()) return Optional.empty();
        else{

            return Optional.of(new ShipController(
                    new ShipMover(
                            new Mover<>(
                                    newShip.get(),
                                    this.shipMover.getPosition(),
                                    this.shipMover.getMovementVector(),
                                    this.shipMover.getFacingDirection(),
                                    this.shipMover.getMover().getAdapter()
                            )
                    ),
                    weapon)
            );

        }
    }

    public ShipMover getShipMover() {
        return shipMover;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
