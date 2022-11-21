package starships.entities.bullet;

import starships.entities.BaseEntity;

public class Bullet extends BaseEntity {
    private final BulletType type;
    private final Integer damage;
    private final Integer size;

    public Bullet(String id, BulletType type, Integer damage, Integer size) {
        super(id, adapter);
        this.type = type;
        this.damage = damage;
        this.size = size;
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
}
