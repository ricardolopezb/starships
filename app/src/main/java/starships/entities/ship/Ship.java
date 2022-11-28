package starships.entities.ship;

import starships.entities.BaseEntity;
import starships.entities.Collidable;
import persistence.Constants;
import starships.entities.EntityType;
import starships.entities.bullet.Bullet;
import starships.utils.ScoreDTO;

import java.util.Optional;

public class Ship extends BaseEntity {
    private final String skin;
    private final Integer health;

    public Ship(String id, Integer health, String skin) {
        super(id, EntityType.SHIP);
        this.skin = skin;
        this.health = health;
    }

    //If damage destroys the ship, returns Empty.
    public Optional<Ship> takeDamage(Integer damage){
        if(damage >= this.health) return Optional.empty();
        else return Optional.of(new Ship(this.id, this.health-damage, skin));
    }

    @Override
    public ScoreDTO getScore() {
        return new ScoreDTO(this.id, 0);
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


    @Override
    public Integer getDamage() {
        return Constants.SHIP_COLLISION_DAMAGE;
    }

    @Override
    public Optional collide(Collidable other) {
        return otherIsMyBullet(other) ? Optional.of(this) : this.takeDamage(other.getDamage());
    }

    private boolean otherIsMyBullet(Collidable other) {
        BaseEntity otherAsEntity = (BaseEntity) other;
        return otherAsEntity.getEntityType() == EntityType.BULLET && ((Bullet) otherAsEntity).getOwnerId().equals(this.id);
    }

}
