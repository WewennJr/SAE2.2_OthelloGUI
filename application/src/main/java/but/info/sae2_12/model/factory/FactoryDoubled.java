package but.info.sae2_12.model.factory;

import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.state.State;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.tokens.Ring;
import coordinates.Coordinate;
import coordinates.CoordinateDoubled;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FactoryDoubled extends AbstractFactory {
    private static final int[] SIZE = new int[]{4, 7, 8, 9, 10, 9, 10, 9, 8, 7, 4};
    private static final int[] STARTING_X = new int[]{6, 3, 2, 1, 0, 1, 0, 1, 2, 3, 6};

    protected List<Coordinate> generate() {
        List<Coordinate> res = new ArrayList<>();
        for (int i = 0; i < SIZE.length; i++) {
            int startingX = STARTING_X[i];
            for (int j = 0; j < SIZE[i]; j++) {
                CoordinateDoubled generate = new CoordinateDoubled(i, startingX);
                res.add(generate);
                startingX += 2;
            }
        }
        return res;
    }

    protected Map<Team, List<Coordinate>> getRings() {
        return Map.of(Team.WHITE, new ArrayList<>(List.of(
                new CoordinateDoubled(3, 13),
                new CoordinateDoubled(3, 15),
                new CoordinateDoubled(5, 15),
                new CoordinateDoubled(5, 17),
                new CoordinateDoubled(6, 12)
        )), Team.BLACK, new ArrayList<>(List.of(
                new CoordinateDoubled(4, 2),
                new CoordinateDoubled(5, 11),
                new CoordinateDoubled(5, 13),
                new CoordinateDoubled(8, 8),
                new CoordinateDoubled(9, 9)

        )));
    }

    protected Map<Team, List<Coordinate>> getPawns() {
        return Map.of(Team.WHITE, new ArrayList<>(List.of(
                new CoordinateDoubled(1, 15),
                new CoordinateDoubled(3, 9),
                new CoordinateDoubled(8, 6),
                new CoordinateDoubled(10, 10)
        )), Team.BLACK, new ArrayList<>(List.of(
                new CoordinateDoubled(1, 11),
                new CoordinateDoubled(3, 7),
                new CoordinateDoubled(3, 11),
                new CoordinateDoubled(4, 6),
                new CoordinateDoubled(6, 2),
                new CoordinateDoubled(9, 3)
        )));
    }

    @Override
    public State stateForBlackLineTest() {
        Map<Coordinate, Token> board = filledBoard();

        for (CoordinateDoubled c : List.of(new CoordinateDoubled(2, 6),
                new CoordinateDoubled(1, 5),
                new CoordinateDoubled(4, 8),
                new CoordinateDoubled(5, 9),
                new CoordinateDoubled(6, 16),
                new CoordinateDoubled(6, 4),
                new CoordinateDoubled(7, 11),
                new CoordinateDoubled(8, 16),
                new CoordinateDoubled(8, 2),
                new CoordinateDoubled(7, 1),
                new CoordinateDoubled(8, 12),
                new CoordinateDoubled(8, 14),
                new CoordinateDoubled(8, 10),
                new CoordinateDoubled(10, 12),
                new CoordinateDoubled(4, 18),
                new CoordinateDoubled(4, 16),
                new CoordinateDoubled(4, 14),
                new CoordinateDoubled(2, 10),
                new CoordinateDoubled(0, 8),
                new CoordinateDoubled(1, 9),
                new CoordinateDoubled(6, 0),
                new CoordinateDoubled(6, 6),
                new CoordinateDoubled(6, 8)
        )) {
            board.put(c, new Pawn(Team.BLACK));
        }
        return new State(board, Team.BLACK, List.of());
    }

    @Override
    public State stateForWhiteLineTest() {
        Map<Coordinate, Token> board = filledBoard();

        for (CoordinateDoubled c : List.of(new CoordinateDoubled(2, 6),
                new CoordinateDoubled(1, 5),
                new CoordinateDoubled(4, 8),
                new CoordinateDoubled(6, 16),
                new CoordinateDoubled(6, 4),
                new CoordinateDoubled(7, 11),
                new CoordinateDoubled(8, 16),
                new CoordinateDoubled(8, 2),
                new CoordinateDoubled(7, 1),
                new CoordinateDoubled(8, 12),
                new CoordinateDoubled(8, 14),
                new CoordinateDoubled(8, 10),
                new CoordinateDoubled(10, 12),
                new CoordinateDoubled(4, 18),
                new CoordinateDoubled(4, 16),
                new CoordinateDoubled(4, 14),
                new CoordinateDoubled(2, 10),
                new CoordinateDoubled(0, 8),
                new CoordinateDoubled(1, 9),
                new CoordinateDoubled(6, 0),
                new CoordinateDoubled(6, 6)
        )) {
            board.put(c, new Pawn(Team.BLACK));
        }
        return new State(board, Team.WHITE, List.of());
    }

    @Override
    public State doubleLineStateTest() {
        Map<Coordinate, Token> board = emptyBoard();
        Map<Team, List<Coordinate>> rings = Map.of(Team.WHITE, new ArrayList<>(List.of(
                new CoordinateDoubled(2, 12),
                new CoordinateDoubled(2, 16),
                new CoordinateDoubled(5, 15),
                new CoordinateDoubled(5, 17),
                new CoordinateDoubled(6, 12)
        )), Team.BLACK, new ArrayList<>(List.of(
                new CoordinateDoubled(4, 2),
                new CoordinateDoubled(5, 11),
                new CoordinateDoubled(5, 13),
                new CoordinateDoubled(8, 8),
                new CoordinateDoubled(9, 9)

        )));
        ;
        Map<Team, List<Coordinate>> pawns = Map.of(Team.WHITE, new ArrayList<>(List.of(
                new CoordinateDoubled(4, 6),
                new CoordinateDoubled(4, 8),
                new CoordinateDoubled(3, 9),
                new CoordinateDoubled(4, 12),
                new CoordinateDoubled(4, 14)
        )), Team.BLACK, new ArrayList<>(List.of(
                new CoordinateDoubled(3, 5),
                new CoordinateDoubled(3, 7),
                new CoordinateDoubled(4, 10),
                new CoordinateDoubled(3, 11),
                new CoordinateDoubled(3, 13)
        )));

        for (Team t : Team.values()) {
            for (Coordinate c : pawns.get(t)) {
                board.put(c, new Pawn(t));
            }
            for (Coordinate c : rings.get(t)) {
                board.put(c, new Ring(t));
            }
        }

        return new State(board, Team.BLACK, List.of());
    }
}
