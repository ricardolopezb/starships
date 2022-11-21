package starships.physics;

import java.util.Objects;

public class Vector {
    private final Double x;
    private final Double y;
    private final Double radians;

    public Vector(Double x, Double y) {
        this.x = x;
        this.y = y;
        double temp = Math.acos(x / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
        this.radians = temp > 2*Math.PI ? temp - 2*Math.PI : temp;
    }

    // Constructor for a vector with degrees from X axis
    public Vector(Double degrees){
        this.x = Math.cos(Math.toRadians(degrees));
        this.y = Math.sin(Math.toRadians(degrees));
        double temp = Math.toRadians(degrees);
        this.radians = temp > 2*Math.PI ? temp - 2*Math.PI : temp;
    }

    public Vector sum(Vector v){
        final Double newVectorX = this.x + v.getX();
        final Double newVectorY = this.y + v.getY();
        return new Vector(newVectorX, newVectorY);
    }

    public Vector multiply(Double coefficient){
        return new Vector(this.x*coefficient, this.y*coefficient);
    }

    public Double getDegrees(){
        return Math.toDegrees(this.radians);
    }

    public Double getSpeed(){
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getRadians(){
        return this.radians;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Math.abs(this.x - vector.getX()) < 0.05 && Math.abs(this.y - vector.getY()) < 0.05;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
