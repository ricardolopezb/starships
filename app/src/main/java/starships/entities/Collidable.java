package starships.entities;

import starships.utils.ScoreDTO;

import java.util.Optional;

public interface Collidable<T> {
    Integer getDamage();

    Optional<T> collide(Collidable other);

    Optional<T> takeDamage(Integer damage);

    ScoreDTO getScore();
}
