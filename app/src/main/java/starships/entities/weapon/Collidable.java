package starships.entities.weapon;

import java.util.Optional;

public interface Collidable<T> {


    Integer getDamage();
    Optional<T> collide(Collidable other);

    Optional<T> takeDamage(Integer damage);
}
