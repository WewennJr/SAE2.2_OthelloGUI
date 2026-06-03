package but.info.sae2_12.model.factory;


import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.state.State;
import but.info.sae2_12.model.Team;
import coordinates.Coordinate;
import coordinates.CoordinateCube;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FactoryCube extends AbstractFactory {

    private static final int[] SIZE = new int[]{4, 7, 8, 9, 10, 9, 10, 9, 8, 7, 4};
    private static final int[] STARTING_Q = new int[]{1, -1, -2, -3, -4, -4, -5, -5, -5, -5, -4};

    protected List<Coordinate> generate() {
        List<Coordinate> res = new ArrayList<>();
        int[] tabSize = SIZE;
        int startingR = -(tabSize.length / 2);
        for (int i = 0; i < tabSize.length; i++) {
            int startingQ = STARTING_Q[i];
            int size = tabSize[i];
            for (int j = 0; j < size; j++) {
                CoordinateCube generate = new CoordinateCube(startingQ, startingR, -(startingQ + startingR));
                res.add(generate);
                startingQ++;
            }
            startingR++;
        }
        return res;
    }


    protected Map<Team, List<Coordinate>> getRings() {
        return Map.of(Team.WHITE, new ArrayList<>(List.of(
                new CoordinateCube(3, 0, -3),
                new CoordinateCube(4, -2, -2),
                new CoordinateCube(3, -2, -1),
                new CoordinateCube(1, 1, -2),
                new CoordinateCube(4, 0, -4)
        )), Team.BLACK, new ArrayList<>(List.of(
                new CoordinateCube(1, 0, -1),
                new CoordinateCube(2, 0, -2),
                new CoordinateCube(-2, 4, -2),
                new CoordinateCube(-3, -1, 4),
                new CoordinateCube(-2, 3, -1))));
    }

    protected Map<Team, List<Coordinate>> getPawns() {
        return Map.of(Team.WHITE, new ArrayList<>(List.of(
                new CoordinateCube(1, -2, 1),
                new CoordinateCube(5, -4, -1),
                new CoordinateCube(-2, 5, -3),
                new CoordinateCube(-3, 3, 0)
        )), Team.BLACK, new ArrayList<>(List.of(
                new CoordinateCube(3, -4, 1),
                new CoordinateCube(3, -4, 1),
                new CoordinateCube(-1, -1, 2),
                new CoordinateCube(-4, 1, 3),
                new CoordinateCube(-5, 4, 1),
                new CoordinateCube(2, -2, 0),
                new CoordinateCube(0, -2, 2)
        )));
    }


    @Override
    public State stateForBlackLineTest() {
        Map<Coordinate, Token> board = filledBoard();

        for (CoordinateCube c : List.of(
                new CoordinateCube(2, -4, 2),
                new CoordinateCube(2, -5, 3),
                new CoordinateCube(4, -1, -3),
                new CoordinateCube(5, -1, -4),
                new CoordinateCube(3, 1, -4),
                new CoordinateCube(0, -4, 4),
                new CoordinateCube(0, -3, 3),
                new CoordinateCube(0, -1, 1),
                new CoordinateCube(0, 0, 0),
                new CoordinateCube(-3, 1, 2),
                new CoordinateCube(-4, 1, 3),
                new CoordinateCube(-5, 1, 4),
                new CoordinateCube(0, 2, -2),
                new CoordinateCube(0, 3, -3),
                new CoordinateCube(1, 3, -4),
                new CoordinateCube(2, 3, -5),
                new CoordinateCube(-2, 1, 1),
                new CoordinateCube(-1, 1, 0),
                new CoordinateCube(-5, 2, 3),
                new CoordinateCube(-5, 3, 2),
                new CoordinateCube(-1, 3, -2),
                new CoordinateCube(-1, 5, -4),
                new CoordinateCube(2, -3, 1),
                new CoordinateCube(3, -1, -2)
        )) {
            board.put(c, new Pawn(Team.BLACK));
        }
        return new State(board, Team.BLACK, List.of());
    }

    @Override
    public State stateForWhiteLineTest() {
        Map<Coordinate, Token> board = filledBoard();

        for (CoordinateCube c : List.of(
                new CoordinateCube(2, -4, 2),
                new CoordinateCube(2, -5, 3),
                new CoordinateCube(4, -1, -3),
                new CoordinateCube(5, -1, -4),
                new CoordinateCube(3, 1, -4),
                new CoordinateCube(0, -4, 4),
                new CoordinateCube(0, -3, 3),
                new CoordinateCube(0, -1, 1),
                new CoordinateCube(-3, 1, 2),
                new CoordinateCube(-4, 1, 3),
                new CoordinateCube(-5, 1, 4),
                new CoordinateCube(0, 2, -2),
                new CoordinateCube(0, 3, -3),
                new CoordinateCube(1, 3, -4),
                new CoordinateCube(2, 3, -5),
                new CoordinateCube(-2, 1, 1),
                new CoordinateCube(-5, 2, 3),
                new CoordinateCube(-5, 3, 2),
                new CoordinateCube(-1, 3, -2),
                new CoordinateCube(-1, 5, -4),
                new CoordinateCube(2, -3, 1),
                new CoordinateCube(3, -1, -2)
        )) {
            board.put(c, new Pawn(Team.BLACK));
        }
        return new State(board, Team.WHITE, List.of());
    }

    @Override
    public State doubleLineStateTest() {
        return null;
    }
}
