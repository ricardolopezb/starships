package starships.adapters;

import edu.austral.ingsis.starships.ui.ElementModel;
import starships.entities.Asteroid;
import starships.entities.BaseEntity;
import starships.movement.Mover;

public class AsteroidUIAdapter implements CoreEntityToUIElementAdapter<Asteroid>{
    @Override
    public ElementModel adapt(Mover<Asteroid> mover) {
        return null;
    }
}
