package starships.entities;

public abstract class BaseEntity implements Collidable {
    protected final String id;
    protected final EntityType type;

    protected BaseEntity(String id, EntityType type) {
        this.id = id;
        this.type = type;
    }

    public String getId(){
        return this.id;
    }

    public EntityType getEntityType() {
        return type;
    }
}


