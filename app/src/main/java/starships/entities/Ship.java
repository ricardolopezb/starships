package starships.entities;

import starships.entities.weapon.Weapon;
import starships.entities.weapon.WeaponType;
import starships.physics.Position;

import java.util.Optional;

public class Ship extends BaseEntity{
    private final String skin;
    private final Integer health;

    public Ship(String id, Integer health, String skin) {
        super(id);
        this.skin = skin;
        this.health = health;
    }

    //If damage destroys the ship, returns Empty.
    public Optional<Ship> takeDamage(Integer damage){
        if(damage >= this.health) return Optional.empty();
        else return Optional.of(new Ship(this.id, this.health-damage, skin));
    }

    public String getSkin() {
        return skin;
    }

    public Integer getHealth() {
        return health;
    }

    public String getId(){
        return this.id;
    }
}
