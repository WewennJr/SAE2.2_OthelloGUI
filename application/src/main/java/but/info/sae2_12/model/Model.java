package but.info.sae2_12.model;

import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.actions.RemoveLine;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Token;
import coordinates.Coordinate;
import coordinates.Mode;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Model {
    private final SimpleObjectProperty<IState> currentStateProperty = new SimpleObjectProperty<>();

    public Model(IState state) {
        this.currentStateProperty.set(state);
    }

    public void setCurrentState(IState state) {
        this.currentStateProperty.set(state);
    }

    public Set<Coordinate> movesFrom(Coordinate from) {
        return currentStateProperty.get().availableMoves(from);
    }

    public void moveRing(Coordinate from, Coordinate to) {
        setCurrentState(currentStateProperty.get().move(new Move(from, to)));
    }

    public List<Set<Coordinate>> getPawnsLines() {
        List<Set<Coordinate>> lines = currentStateProperty.get().getLines();
        return (lines.isEmpty()) ? IState.getPawnsLines(currentStateProperty.get().board()) : lines;
    }

    public void removeLine(Set<Coordinate> line, Coordinate ring) {
        setCurrentState(currentStateProperty.get().removeLine(new RemoveLine(line, ring)));
    }

    public Map<Coordinate, Token> getBoard() {
        return currentStateProperty.get().board();
    }

    public Token getTokenAt(Coordinate c) {
        return currentStateProperty.get().board().get(c);
    }

    public List<Coordinate> getNeighbors(Coordinate c) {
        return c.getNeighbors(Mode.POINTY).stream().filter(this::isInField).collect(Collectors.toList());
    }

    public boolean isInField(Coordinate c) {
        return currentStateProperty.get().board().containsKey(c);
    }

    public List<Coordinate> getRings(Team team) {
        return currentStateProperty.get().rings().get(team);
    }

    public List<Coordinate> getPawn(Team team) {
        return currentStateProperty.get().board().keySet().stream().filter(c -> getTokenAt(c) instanceof Pawn p && p.getTeam() == team).toList();
    }

    public Team getTurn() {
        return currentStateProperty.get().turn();
    }

    public IState getCurrentState() {
        return currentStateProperty.get();
    }

    public SimpleObjectProperty<IState> stateProperty() {
        return currentStateProperty;
    }

    public void removeToken(Coordinate c) {
        currentStateProperty.set(currentStateProperty.get().removeToken(c));

    }

    public void toggleToken(Coordinate c, Class<?> tokenClass, Team team) {
        currentStateProperty.set(currentStateProperty.get().toggleToken(c, team, tokenClass));
    }

    public SimpleObjectProperty<IState> currentStateProperty() {
        return currentStateProperty;
    }
}