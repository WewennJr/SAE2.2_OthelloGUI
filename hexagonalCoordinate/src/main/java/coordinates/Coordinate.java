package coordinates;

import java.security.InvalidParameterException;
import java.util.List;

public abstract class Coordinate {
    public abstract Point to2DCoordinate();

    public Coordinate toDir(Mode mode, Direction direction) {
        return switch (direction) {
            case NO -> NO(mode);
            case N -> N(mode);
            case NE -> NE(mode);
            case E -> E(mode);
            case SE -> SE(mode);
            case S -> S(mode);
            case SO -> SO(mode);
            case O -> O(mode);
            default -> throw new RuntimeException("Undefined direction");
        };
    }

    /**
     *
     * @param mode Pointy or flat.
     * @return The north-west coordinate.
     */
    public abstract Coordinate NO(Mode mode);

    /**
     *
     * @param mode Pointy or flat.
     * @return The north-east coordinate.
     */
    public abstract Coordinate NE(Mode mode);

    /**
     *
     * @param mode Pointy or flat.
     * @return The east coordinate.
     * @throws InvalidParameterException If asked in {@link Mode#FLAT}
     */
    public abstract Coordinate E(Mode mode);

    /**
     *
     * @param mode Pointy or flat.
     * @return The west coordinate.
     * @throws InvalidParameterException If asked in {@link Mode#FLAT}
     */
    public abstract Coordinate O(Mode mode);

    /**
     *
     * @param mode Pointy or flat.
     * @return The north coordinate.
     * @throws InvalidParameterException If asked in {@link Mode#POINTY}
     */
    public abstract Coordinate N(Mode mode);

    /**
     *
     * @param mode Pointy or flat.
     * @return The south coordinate.
     * @throws InvalidParameterException If asked in {@link Mode#POINTY}
     */
    public abstract Coordinate S(Mode mode);

    /**
     *
     * @param mode Pointy or flat.
     * @return The south-west coordinate.
     */
    public abstract Coordinate SO(Mode mode);

    /**
     *
     * @param mode Pointy or flat.
     * @return The south-east coordinate.
     */
    public abstract Coordinate SE(Mode mode);

    public List<Coordinate> getNeighbors(Mode mode) {
        return (mode == Mode.FLAT) ? neighborsFlat() : neighborsPointy();
    }

    private List<Coordinate> neighborsFlat() {
        return List.of(NO(Mode.FLAT), N(Mode.FLAT), NE(Mode.FLAT), SE(Mode.FLAT), S(Mode.FLAT), SO(Mode.FLAT));
    }

    private List<Coordinate> neighborsPointy() {
        return List.of(NO(Mode.POINTY), NE(Mode.POINTY), O(Mode.POINTY), E(Mode.POINTY), SO(Mode.POINTY), SE(Mode.POINTY));
    }


    /**
     * Must compute each coordinate between the current one the one given.
     * @param mode If we want to compute in a pointy or flat way.
     * @param to The destination.
     * @return A list of coordinate, empty if {@code this} and {@code to} are the same.
     * @throws DifferentAxisException If the two coordinate aren't on the same axis.
     */
    public abstract List<Coordinate> between(Mode mode, Coordinate to);
}
