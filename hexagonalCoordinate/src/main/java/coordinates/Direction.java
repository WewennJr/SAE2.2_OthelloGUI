package coordinates;

public enum Direction {
    NO, N, NE, E, SE, S, SO, O;

    public Direction opposite() {
        return Direction.values()[(this.ordinal() + 4) % 8];
    }
}
