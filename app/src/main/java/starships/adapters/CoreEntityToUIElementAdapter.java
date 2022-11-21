package starships.adapters;

import edu.austral.ingsis.starships.ui.ElementModel;
import starships.entities.BaseEntity;

public interface CoreEntityToUIElementAdapter<T> {

    ElementModel adapt(T coreEntity);


}
