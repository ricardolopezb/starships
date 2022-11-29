package starships.entities;

import persistence.visitor.Visitable;

public abstract class BaseEntity implements Collidable, Visitable {
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


