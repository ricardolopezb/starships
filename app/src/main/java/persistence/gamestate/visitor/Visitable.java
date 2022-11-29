package persistence.gamestate.visitor;

public interface Visitable {
    <T> T accept(Visitor<T> visitor);
}
