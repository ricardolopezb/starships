package starships.entities.bullet;

import starships.entities.BaseEntity;
import starships.entities.Collidable;

import java.util.Optional;

public class Bullet extends BaseEntity {
    private final BulletType type;
    private final Integer damage;
    private final Integer size;
    private final String ownerId;

    public Bullet(String id, BulletType type, Integer damage, Integer size, String ownerId) {
        super(id);
        this.type = type;
        this.damage = damage;
        this.size = size;
        this.ownerId = ownerId;
    }

    public Integer getDamage() {
        return damage;
    }

    @Override
    public Optional collide(Collidable other) {
        if(((BaseEntity) other).getId().equals(ownerId)) return Optional.of(this);
        return this.takeDamage(other.getDamage());
    }

    @Override
    public Optional takeDamage(Integer damage) {
        return Optional.empty();
    }


    public Integer getSize() {
        return size;
    }

    public BulletType getType() {
        return type;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
