package starships.entities.bullet;

import starships.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class BulletFactory {

    private final BulletType bulletType;

    public BulletFactory(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public List<Bullet> generateBullets(int quantity, String ownerId){
        List<Bullet> toReturn = new ArrayList<>(quantity);
        for (int i = 0; i < quantity; i++) {
            toReturn.add(createBulletForType(ownerId));
        }
        return toReturn;
    }

    private Bullet createBulletForType(String ownerId) {
        switch (this.bulletType) {
            case LASER -> {return new Bullet("Bullet-"+ IdGenerator.generateId(), BulletType.LASER, 10, 2, ownerId);}
            case PLASMA -> {return new Bullet("Bullet-"+ IdGenerator.generateId(), BulletType.PLASMA, 14, 2, ownerId);}
            case EXPLOSIVE -> {return new Bullet("Bullet-"+ IdGenerator.generateId(), BulletType.EXPLOSIVE, 15, 4, ownerId);}
            default -> {return null;}
        }
    }

    public BulletType getBulletType() {
        return bulletType;
    }
}
