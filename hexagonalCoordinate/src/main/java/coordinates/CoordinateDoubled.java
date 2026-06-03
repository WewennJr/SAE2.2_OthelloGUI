package coordinates;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Class used to represent the coordinate of a hexagon with only a Y and a X coordinate, such as [6,0], [8,0], etc.
 * More information <a href="https://www.redblobgames.com/grids/hexagons/#coordinates-doubled">here</a>
 */
public class CoordinateDoubled extends Coordinate {
    private final int y;
    private final int x;

    public CoordinateDoubled(int y, int x) {
        this.y = y;
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateDoubled that = (CoordinateDoubled) o;
        return y == that.y && x == that.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x);
    }

    public String toString() {
        return String.format("[%d, %d]", y, x);
    }

    @Override
    public Point to2DCoordinate() {
        return new Point(x, y);
    }

    @Override
    public Coordinate NO(Mode mode) {
        return new CoordinateDoubled(y - 1, x - 1);
    }

    @Override
    public Coordinate NE(Mode mode) {
        return new CoordinateDoubled(y - 1, x + 1);
    }

    @Override
    public Coordinate E(Mode mode) {
        if (mode == Mode.FLAT) throw new InvalidParameterException("Flat mode doesn't have a right neighbor.");
        return new CoordinateDoubled(y, x + 2);
    }

    @Override
    public Coordinate O(Mode mode) {
        if (mode == Mode.FLAT) throw new InvalidParameterException("Flat mode doesn't have a left neighbor.");
        return new CoordinateDoubled(y, x - 2);
    }

    @Override
    public Coordinate N(Mode mode) {
        if (mode == Mode.POINTY) throw new InvalidParameterException("Pointy mode doesn't have a up neighbor.");
        return new CoordinateDoubled(y - 2, x);
    }

    @Override
    public Coordinate S(Mode mode) {
        if (mode == Mode.POINTY) throw new InvalidParameterException("Pointy mode doesn't have a down neighbor.");
        return new CoordinateDoubled(y + 2, x);
    }

    @Override
    public Coordinate SO(Mode mode) {
        return new CoordinateDoubled(y + 1, x - 1);
    }

    @Override
    public Coordinate SE(Mode mode) {
        return new CoordinateDoubled(y + 1, x + 1);
    }

    /**
     * @param mode Pointy or flat, change the way we compute coordinate between same line or same column
     * @param to   Target coordinate with which we want the intermediate coordinate
     * @return List of coordinate between this and to
     */
    @Override
    public List<Coordinate> between(Mode mode, Coordinate to) {
        if (!(to instanceof CoordinateDoubled dest))
            throw new IllegalArgumentException("Given coordinate isn't a CoordinateCube : " + to);

        if (to.equals(this)) return List.of();

        List<Coordinate> res = new ArrayList<>();

        if (mode == Mode.POINTY && dest.y == y) {
            // If on the same line, the space between columns must be even.
            if ((dest.x - x) % 2 != 0) {
                throw new DifferentAxisException(String.format("%s and %s have no common axis.", this, to));
            }

            int dx = (dest.x > x) ? 2 : -2;
            int currentX = x;
            while (currentX != dest.x) {
                currentX += dx;
                res.add(new CoordinateDoubled(y, currentX));
            }
        } else if (mode == Mode.FLAT && dest.x == x) {
            if ((dest.y - y) % 2 != 0) {
                throw new DifferentAxisException(String.format("%s and %s have no common axis.", this, to));
            }

            int dy = (dest.y > y) ? 2 : -2;
            int currentY = y;
            while (currentY != dest.y) {
                currentY += dy;
                res.add(new CoordinateDoubled(currentY, x));
            }
        } else {
            // If not on the same line, the distance between x & y-axis must be the same length.
            if (abs(dest.x - x) != abs(dest.y - y)) {
                throw new DifferentAxisException(String.format("%s and %s have no common axis.", this, to));
            }
            int dx = (dest.x > x) ? 1 : -1;
            int dy = (dest.y > y) ? 1 : -1;
            int currentX = x;
            int currentY = y;

            while (currentX != dest.x && currentY != dest.y) {
                currentX += dx;
                currentY += dy;

                res.add(new CoordinateDoubled(currentY, currentX));
            }
        }

        res.remove(res.size() - 1);

        return res;
    }
}
