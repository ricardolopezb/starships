package adapters;

import edu.austral.ingsis.starships.ui.ElementModel;
import starships.entities.BaseEntity;
import starships.entities.ship.ShipController;
import starships.movement.Mover;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface CoreEntityToUIElementAdapter<T extends BaseEntity>{

    ElementModel adapt(Mover<T> mover);





}
