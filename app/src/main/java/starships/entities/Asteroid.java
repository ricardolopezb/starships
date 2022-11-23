package starships.entities;

public class Asteroid extends BaseEntity{

    private final Integer size;
    private final Integer damage;

    protected Asteroid(String id, Integer size) {
        super(id);
        this.size = size;
        this.damage = size/3;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getDamage() {
        return damage;
    }
}
