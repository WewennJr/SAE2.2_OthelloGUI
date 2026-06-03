package but.info.sae2_12.model.factory;

import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.state.State;
import but.info.sae2_12.model.Team;
import coordinates.Coordinate;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractFactory implements IFactory {
    protected abstract List<Coordinate> generate();

    protected abstract Map<Team, List<Coordinate>> getRings();

    protected abstract Map<Team, List<Coordinate>> getPawns();

    protected Map<Coordinate, Token> filledBoard() {
        Map<Coordinate, Token> board = emptyBoard();
        Map<Team, List<Coordinate>> rings = getRings();
        Map<Team, List<Coordinate>> pawns = getPawns();

        for (Team t : Team.values()) {
            for (Coordinate c : pawns.get(t)) {
                board.put(c, new Pawn(t));
            }
            for (Coordinate c : rings.get(t)) {
                board.put(c, new Ring(t));
            }
        }

        return board;
    }

    protected Map<Coordinate, Token> emptyBoard() {
        HashMap<Coordinate, Token> board = new HashMap<>();
        for (Coordinate c : generate()) {
            board.put(c, null);
        }
        return board;
    }

    @Override
    public State testState() {
        Map<Coordinate, Token> board = filledBoard();

        return new State(board, Team.WHITE, List.of());
    }

    @Override
    public State emptyState() {
        return new State(
                emptyBoard(),
                Team.WHITE,
                List.of());
    }
    
    public IState emptyGame() {
        Map<Coordinate, Token> board = emptyBoard();
        Map<Team, List<Coordinate>> rings = getRings();

        for (Team t : Team.values()) {
            for (Coordinate c : rings.get(t)) {
                board.put(c, new Ring(t));
            }
        }

        return new State(board, Team.WHITE, List.of());
    }

}
