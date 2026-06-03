package coordinates;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Class used to represent the coordinate of a hexagon with a three axis system : q, r and s.
 * <br/>
 * <br/>
 *
 * Can't only use a X,Y,Z system because every set of coordinate doesn't exist on a hexagon map.
 * <br/>
 * <br/>
 *
 * More information <a href="https://www.redblobgames.com/grids/hexagons/#coordinates-cube">here</a>
 */
public class CoordinateCube extends Coordinate {
    private final int q;
    private final int r;
    private final int s;

    public CoordinateCube(int q, int r, int s) {
        if (q + r + s != 0) {
            throw new IllegalArgumentException(String.format("The sum of Q, R and S must be 0, currently %d for (%d,%d,%d)", q + r + s, q, r, s));
        }

        this.q = q;
        this.r = r;
        this.s = s;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public int getS() {
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateCube that = (CoordinateCube) o;
        return q == that.q && r == that.r && s == that.s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r, s);
    }

    @Override
    public String toString() {
        return String.format("[%d,%d,%d]", q, r, s);
    }

    @Override
    public Point to2DCoordinate() {
        int parity = r & 1;
        int col = q + (r - parity) / 2;
        int row = r;


        col += 9;
        row += 5;
        if ((row & 1) != 1) {
            int d = 8 - col;
            col = 8 - d * 2;
        } else {
            int d = 9 - col;
            col = 9 - d * 2;
        }

        return new Point(col, row);
    }

    @Override
    public Coordinate NO(Mode mode) {
        return switch (mode) {
            case POINTY -> new CoordinateCube(q, r - 1, s + 1);
            case FLAT -> new CoordinateCube(q - 1, s + 1, r);
        };
    }

    @Override
    public Coordinate NE(Mode mode) {
        return new CoordinateCube(q + 1, r - 1, s);
    }

    @Override
    public Coordinate E(Mode mode) {
        if (mode == Mode.FLAT) throw new InvalidParameterException("Flat mode doesn't have a right neighbor.");
        return new CoordinateCube(q + 1, r, s - 1);
    }

    @Override
    public Coordinate O(Mode mode) {
        if (mode == Mode.FLAT) throw new InvalidParameterException("Flat mode doesn't have a left neighbor.");
        return new CoordinateCube(q - 1, r, s + 1);
    }

    @Override
    public Coordinate N(Mode mode) {
        if (mode == Mode.POINTY) throw new InvalidParameterException("Pointy mode doesn't have a up neighbor.");
        return new CoordinateCube(q, r - 1, s + 1);
    }

    @Override
    public Coordinate S(Mode mode) {
        if (mode == Mode.POINTY) throw new InvalidParameterException("Pointy mode doesn't have a down neighbor.");
        return new CoordinateCube(q, r + 1, s - 1);
    }

    @Override
    public Coordinate SO(Mode mode) {
        return new CoordinateCube(q - 1, r + 1, s);
    }

    @Override
    public Coordinate SE(Mode mode) {
        return switch (mode) {
            case POINTY -> new CoordinateCube(q, r + 1, s - 1);
            case FLAT -> new CoordinateCube(q + 1, r, s - 1);
        };

    }

    /**
     * @param mode Pointy or flat, doesn't impact the calculus of the coordinate in cubic representation
     * @param to   Target coordinate with which we want the intermediate coordinate
     * @return List of coordinate between this and to
     */
    @Override
    public List<Coordinate> between(Mode mode, Coordinate to) {
        if (!(to instanceof CoordinateCube dest))
            throw new IllegalArgumentException("Given coordinate isn't a CoordinateCube : " + to);

        if (to.equals(this)) return List.of();

        List<Coordinate> res = new ArrayList<>();
        int currentR = r, endR = dest.r, currentS = s, endS = dest.s, currentQ = q, endQ = dest.q;

        int dr = -Integer.compare(currentR, endR);
        int ds = -Integer.compare(currentS, endS);
        int dq = -Integer.compare(currentQ, endQ);

        if (dr != 0 && ds != 0 && dq != 0) {
            throw new DifferentAxisException(String.format("%s and %s have no common axis.", this, to));
        }

        while (currentR != endR || currentS != endS || currentQ != endQ) {
            currentR += dr;
            currentS += ds;
            currentQ += dq;

            res.add(new CoordinateCube(currentQ, currentR, currentS));
        }

        res.remove(res.size() - 1);

        return res;
    }

}
