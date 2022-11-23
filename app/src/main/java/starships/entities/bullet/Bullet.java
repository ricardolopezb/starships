package starships.entities.bullet;

import starships.entities.BaseEntity;

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
