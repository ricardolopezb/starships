package starships.entities.ship;

import edu.austral.ingsis.starships.ui.ElementModel;
import persistence.gamestate.visitor.Visitable;
import persistence.gamestate.visitor.Visitor;
import starships.entities.bullet.Bullet;
import starships.entities.weapon.Weapon;
import starships.entities.weapon.WeaponDTO;
import starships.entities.weapon.WeaponFactory;
import starships.entities.weapon.WeaponType;
import starships.movement.Mover;
import starships.movement.ShipMover;

import java.util.List;

public class ShipController implements Visitable {
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

    public ShipController changeWeapon(WeaponType weaponType){
        return new ShipController(this.shipMover, WeaponFactory.createWeaponForType(weaponType, this.shipMover.getMover()));



        //return new ShipController(this.shipMover, new Weapon(weaponDTO, this.shipMover.getMover()));
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



    public ShipMover getShipMover() {
        return shipMover;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public ShipController stop() {
        return new ShipController(this.shipMover.stopShip(), this.weapon);
    }

    public ShipController resetToInitialPosition(){
        return new ShipController(this.shipMover.resetToInitialPosition(), this.weapon);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitShipController(this);
    }
}
