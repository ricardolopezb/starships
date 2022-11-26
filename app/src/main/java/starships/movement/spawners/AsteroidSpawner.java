package starships.movement.spawners;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import starships.adapters.AsteroidUIAdapter;
import starships.entities.Asteroid;
import starships.entities.ship.Ship;
import starships.movement.Mover;
import starships.persistence.WindowConfigurator;
import starships.physics.Position;
import starships.physics.Vector;
import starships.utils.IdGenerator;
import starships.utils.RandomNumberGenerator;

import java.util.Random;

import static starships.utils.RandomNumberGenerator.getRandomNumber;

public class AsteroidSpawner {

    private final Integer maxX;
    private final Integer maxY;


    public AsteroidSpawner() {
        WindowConfigurator wc = new WindowConfigurator();
        this.maxX = ((Long) wc.getProperty("width")).intValue();
        this.maxY = ((Long) wc.getProperty("height")).intValue();}

    public Mover<Asteroid> spawnAsteroid(Position targetShipPosition){
        Side randomSide = getRandomSide();
        return switch (randomSide) {
            case TOP -> spawnInCoordinates(getRandomNumber(0, maxX), 0+10, targetShipPosition);
            case BOTTOM -> spawnInCoordinates(getRandomNumber(0, maxX), maxY-10, targetShipPosition);
            case LEFT -> spawnInCoordinates(0+10, getRandomNumber(0, maxY), targetShipPosition);
            case RIGHT -> spawnInCoordinates(maxX-10, getRandomNumber(0, maxY), targetShipPosition);
        };
    }
    private enum Side {
        TOP, BOTTOM, LEFT, RIGHT
    }


    private Mover<Asteroid> spawnInCoordinates(Integer x, Integer y, Position targetShipPosition) {
        return new Mover<>(
                new Asteroid("Asteroid-" + IdGenerator.generateId(), getRandomNumber(20, 600)),
                new Position(x, y),
                getMovementVectorToShipPostition(x, y, targetShipPosition),
                new Vector(1.0,1.0),
                new AsteroidUIAdapter()
        );
    }

    @NotNull
    private Vector getMovementVectorToShipPostition(Integer x, Integer y, Position targetShipPosition) {
        //System.out.println("Target ship postition " + targetShipPosition.getX() + " " + targetShipPosition.getY());
        //System.out.println((double) (targetShipPosition.getX() - x)+ " " + (double) (targetShipPosition.getY() - y));
        //return new Vector((double) (targetShipPosition.getX() - x)*0.01, (double) (targetShipPosition.getY() - y)*0.01);
        return new Vector(0.0, -1.0);
    }


    private Side getRandomSide(){
        Integer side = getRandomNumber(0, 4);
        return switch (side){
            case 0 -> Side.TOP;
            case 1 -> Side.RIGHT;
            case 2 -> Side.BOTTOM;
            case 3 -> Side.LEFT;
            default -> Side.RIGHT;
        };
    }
}
