package but.info.sae2_12.model.actions;

import coordinates.Coordinate;

import java.util.Set;

public class RemoveLine extends Action {
    private Set<Coordinate> line;
    private Coordinate ring;

    public RemoveLine(Set<Coordinate> line, Coordinate ring) {
        this.line = line;
        this.ring = ring;
    }

    public Set<Coordinate> getLine() {
        return line;
    }

    public void setLine(Set<Coordinate> line) {
        this.line = line;
    }

    public Coordinate getRing() {
        return ring;
    }

    public void setRing(Coordinate ring) {
        this.ring = ring;
    }
}
