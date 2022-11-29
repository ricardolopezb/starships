package starships.entities;

import persistence.gamestate.visitor.Visitor;
import starships.utils.ScoreDTO;

import java.util.Optional;

public class Asteroid extends BaseEntity{

    private final Integer size;
    private final Integer damage;
    private final Integer health;

    public Asteroid(String id, Integer size) {
        super(id, EntityType.ASTEROID);
        this.size = size;
        this.damage = size/3;
        this.health = size;
    }
    public Asteroid(String id, Integer size, Integer health){
        super(id, EntityType.ASTEROID);
        this.size = size;
        this.damage = size/3;
        this.health = health;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getDamage() {
        return damage;
    }

    @Override
    public Optional collide(Collidable other) {
        return this.takeDamage(other.getDamage());
    }

    public Integer getHealth(){
        return health;
    }

    public Optional<Asteroid> takeDamage(Integer damage){
        if(damage >= this.health) return Optional.empty();
        else return Optional.of(new Asteroid(this.id, this.size, this.health-damage));
    }

    @Override
    public ScoreDTO getScore() {
        return new ScoreDTO(this.id, 0);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitAsteroid(this);
    }
}
