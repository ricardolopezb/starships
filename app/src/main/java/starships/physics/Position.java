package starships.physics;

public class Position {
    private final Integer x;
    private final Integer y;

    public Position(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Position sum(Position position){
        return new Position(this.x + position.getX(), this.y + position.getY());
    }

    public Position multiply(Double coefficient){
        return new Position((int)(this.x*coefficient), (int)(this.y*coefficient));
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
