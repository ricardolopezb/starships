package starships.entities;

public abstract class BaseEntity {
    protected final String id;

    protected BaseEntity(String id) {
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

}
