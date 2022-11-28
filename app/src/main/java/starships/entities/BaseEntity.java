package starships.entities;

import starships.adapters.CoreEntityToUIElementAdapter;
import starships.entities.weapon.Collidable;

public abstract class BaseEntity implements Collidable {
    protected final String id;

    protected BaseEntity(String id) {
        this.id = id;

    }

    public String getId(){
        return this.id;
    }


}
