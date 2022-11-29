package persistence.visitor;

public interface Visitable {
    <T> T accept(Visitor<T> visitor);
}
