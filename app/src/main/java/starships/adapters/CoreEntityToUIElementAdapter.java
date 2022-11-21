package starships.adapters;

import edu.austral.ingsis.starships.ui.ElementModel;
import starships.entities.BaseEntity;
import starships.movement.Mover;

public interface CoreEntityToUIElementAdapter<T extends BaseEntity>{

    ElementModel adapt(Mover<T> mover);



}
