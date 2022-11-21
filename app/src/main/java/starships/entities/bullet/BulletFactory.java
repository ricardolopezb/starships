package starships.entities.bullet;

import starships.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class BulletFactory {

    private final BulletType bulletType;

    public BulletFactory(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public List<Bullet> generateBullets(int quantity){
        List<Bullet> toReturn = new ArrayList<>(quantity);
        for (int i = 0; i < quantity; i++) {
            toReturn.add(createBulletForType());
        }
        return toReturn;
    }

    private Bullet createBulletForType() {
        switch (this.bulletType) {
            case LASER -> {return new Bullet("Bullet-"+ IdGenerator.generateId(), BulletType.LASER, 10, 2);}
            case PLASMA -> {return new Bullet("Bullet-"+ IdGenerator.generateId(), BulletType.PLASMA, 14, 2);}
            case EXPLOSIVE -> {return new Bullet("Bullet-"+ IdGenerator.generateId(), BulletType.EXPLOSIVE, 15, 4);}
            default -> {return null;}
        }
    }
}
