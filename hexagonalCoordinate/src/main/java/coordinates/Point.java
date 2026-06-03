package coordinates;

public record Point(int x, int y) {

    @Override
    public String toString() {
        return String.format("[%d,%d]", y, x);
    }
}
