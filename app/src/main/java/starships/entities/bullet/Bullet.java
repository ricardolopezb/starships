package starships.entities.bullet;

import starships.entities.BaseEntity;
import starships.entities.Collidable;
import starships.entities.EntityType;
import starships.utils.ScoreDTO;

import java.util.Optional;

public class Bullet extends BaseEntity {
    private final BulletType bulletType;
    private final Integer damage;
    private final Integer size;
    private final String ownerId;

    public Bullet(String id, BulletType type, Integer damage, Integer size, String ownerId) {
        super(id, EntityType.BULLET);
        this.bulletType = type;
        this.damage = damage;
        this.size = size;
        this.ownerId = ownerId;
    }

    public Integer getDamage() {
        return damage;
    }

    @Override
    public Optional collide(Collidable other) {
        if(((BaseEntity) other).getId().equals(ownerId) || ((BaseEntity) other).getEntityType() == EntityType.BULLET)
            return Optional.of(this);
        return this.takeDamage(other.getDamage());
    }

    @Override
    public Optional takeDamage(Integer damage) {
        return Optional.empty();
    }

    @Override
    public ScoreDTO getScore() {
        return new ScoreDTO(this.ownerId, this.damage);
    }


    public Integer getSize() {
        return size;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
