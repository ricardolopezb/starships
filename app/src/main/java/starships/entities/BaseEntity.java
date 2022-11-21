package starships.entities;

import starships.adapters.CoreEntityToUIElementAdapter;

public abstract class BaseEntity {
    protected final String id;

    protected BaseEntity(String id) {
        this.id = id;

    }

    public String getId(){
        return this.id;
    }


}
