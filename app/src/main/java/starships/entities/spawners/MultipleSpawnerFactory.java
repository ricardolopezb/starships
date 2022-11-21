package starships.entities.spawners;

import starships.entities.BaseEntity;
import starships.entities.Ship;
import starships.entities.weapon.ShotType;
import starships.movement.Mover;
import starships.physics.Position;

public class MultipleSpawnerFactory<T extends BaseEntity> {

    public MultipleSpawner<T> getSpawnerForShotType(ShotType shotType, Mover<Ship> mover, Double shotSpeed){
        MultipleSpawner<T> toReturn = null;
        switch (shotType){
            case LINEAR -> toReturn = new LinearSpawner<>(mover.getPosition(), shotSpeed, mover.getFacingDirection());
            case RADIAL -> toReturn = new RadialSpawner<>(mover.getPosition(), shotSpeed);
        }
        return toReturn;
    }
}
