package starships.entities;

public class Asteroid extends BaseEntity{
    protected Asteroid(String id) {
        super(id, new AsteroidUIAdapter());
    }
}
