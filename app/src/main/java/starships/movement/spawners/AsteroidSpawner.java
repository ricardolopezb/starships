package starships.movement.spawners;

import org.jetbrains.annotations.NotNull;
import adapters.AsteroidUIAdapter;
import starships.entities.Asteroid;
import starships.movement.Mover;
import persistence.Constants;
import persistence.WindowConfigurator;
import starships.physics.Position;
import starships.physics.Vector;
import starships.utils.IdGenerator;

import static starships.utils.RandomNumberGenerator.getRandomNumber;

public class AsteroidSpawner {

    private final Integer maxX;
    private final Integer maxY;


    public AsteroidSpawner() {
        WindowConfigurator wc = WindowConfigurator.getInstance();


        this.maxX = getIntegerPropertyValueWithDefault(wc, "width", 950);
        this.maxY = getIntegerPropertyValueWithDefault(wc, "height", 800);
    }

    private static int getIntegerPropertyValueWithDefault(WindowConfigurator wc, String property, int defaultValue) {
        return wc.getProperty(property).isPresent() ? ((Long) wc.getProperty(property).get()).intValue() : defaultValue;
    }

    public Mover<Asteroid> spawnAsteroid(Position targetShipPosition){
        Side randomSide = getRandomSide();
        return switch (randomSide) {
            case TOP -> spawnInCoordinates(getRandomNumber(0, maxX), 0+70, targetShipPosition);
            case BOTTOM -> spawnInCoordinates(getRandomNumber(0, maxX), maxY-10, targetShipPosition);
            case LEFT -> spawnInCoordinates(0+10, getRandomNumber(0, maxY), targetShipPosition);
            case RIGHT -> spawnInCoordinates(maxX-10, getRandomNumber(0, maxY), targetShipPosition);
        };
    }
    private enum Side {
        TOP, BOTTOM, LEFT, RIGHT
    }


    private Mover<Asteroid> spawnInCoordinates(Integer x, Integer y, Position targetShipPosition) {
        Integer randomSize = getRandomNumber(50, 100);
        return new Mover<>(
                new Asteroid("Asteroid-" + IdGenerator.generateId(), randomSize),
                new Position(x, y),
                getMovementVectorToShipPostition(x, y, targetShipPosition),
                new Vector(1.0,1.0),
                new AsteroidUIAdapter()
        );
    }

    @NotNull
    private Vector getMovementVectorToShipPostition(Integer x, Integer y, Position targetShipPosition) {
        return generateMovementVector(x, y, targetShipPosition);
    }

    private static Vector generateMovementVector(Integer x, Integer y, Position targetShipPosition) {
        return new Vector((double) (targetShipPosition.getX() - x), (double) (targetShipPosition.getY() - y)).normalize().multiply(Constants.ASTEROID_SPEED);
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
