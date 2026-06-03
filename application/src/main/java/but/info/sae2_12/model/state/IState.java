package but.info.sae2_12.model.state;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.actions.RemoveLine;
import coordinates.Coordinate;
import coordinates.Direction;
import coordinates.Mode;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Token;

import java.util.*;

public interface IState {
    /**
     * Move a ring from a position to another, each point between {@code from} and  {@code to} will have his color changed.
     *
     * @param move The move to make.
     * @return The new state.
     * @throws IllegalArgumentException      If the position {@code from} doesn't have a ring on it or if the {@code to} isn't free.
     * @throws IndexOutOfBoundsException     If the position {@code from} isn't in the board.
     * @throws UnsupportedOperationException If the current position has lines to remove before playing a ring.
     */
    IState move(Move move);

    /**
     * Remove a line of 5 pawns and a ring of the current player.
     *
     * @param removeLine The action to do.
     * @return The new game state
     * @throws IllegalArgumentException      If the {@code ring} coordinate isn't a ring, or it's not a ring of the current player.
     * @throws IllegalArgumentException      If the {@code line} isn't a correct line : Not all 5 pawns or not all of the current player.
     * @throws UnsupportedOperationException If the current state does not contain any line.
     */
    IState removeLine(RemoveLine removeLine);

    /**
     * Compute all possible move from a given coordinate.
     *
     * @param from Ring's coordinate.
     * @return All possible coordinate where the ring can move, if the given coordinate doesn't have a ring, it will return
     * an empty set.
     */
    Set<Coordinate> availableMoves(Coordinate from);

    /**
     * @return The current board.
     */
    Map<Coordinate, Token> board();

    /**
     * @return The position of each team's rings
     */
    Map<Team, List<Coordinate>> rings();

    List<Set<Coordinate>> getLines();

    Team turn();

    static List<Set<Coordinate>> getPawnsLines(Map<Coordinate, Token> board) {
        Set<Set<Coordinate>> res = new HashSet<>();
        List<Coordinate> pawnsToTest = board.keySet().stream().filter(c -> board.get(c) instanceof Pawn p).toList();
        
        for (Coordinate c : pawnsToTest) {
            if (!(board.get(c) instanceof Pawn p)) continue;
            Team team = p.getTeam();
            for (Direction d : List.of(Direction.E, Direction.NE, Direction.NO)) {
                LinkedList<Coordinate> currentSet = new LinkedList<>();
                currentSet.add(c);
                Coordinate leftPosition = c.toDir(Mode.POINTY, d);
                Coordinate rightPosition = c.toDir(Mode.POINTY, d.opposite());
                while (board.containsKey(leftPosition) && board.get(leftPosition) instanceof Pawn pawnLeft && pawnLeft.getTeam() == team) {
                    currentSet.add(leftPosition);
                    leftPosition = leftPosition.toDir(Mode.POINTY, d);

                    if (currentSet.size() == 5) {
                        res.add(new HashSet<>(currentSet));
                        currentSet.removeLast();
                        break;
                    }
                }

                while (board.containsKey(rightPosition) && board.get(rightPosition) instanceof Pawn pawnRight && pawnRight.getTeam() == team) {
                    currentSet.add(0, rightPosition);
                    rightPosition = rightPosition.toDir(Mode.POINTY, d.opposite());

                    if (currentSet.size() == 5) {
                        res.add(new HashSet<>(currentSet));
                        currentSet.removeLast();
                        break;
                    }


                }
            }
        }

        return res.stream().toList();
    }

    IState removeToken(Coordinate c);

    IState toggleToken(Coordinate position, Team team, Class<?> token);
}
