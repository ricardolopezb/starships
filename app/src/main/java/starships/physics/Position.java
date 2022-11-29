package starships.physics;

import persistence.gamestate.visitor.Visitable;
import persistence.gamestate.visitor.Visitor;

public class Position implements Visitable {
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

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitPosition(this);
    }
}
