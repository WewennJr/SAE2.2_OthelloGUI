package but.info.sae2_12.model.state;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.actions.RemoveLine;
import coordinates.Coordinate;
import coordinates.Direction;
import coordinates.Mode;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public record State(Map<Coordinate, Token> board,
                    Team turn,
                    List<Set<Coordinate>> lines) implements IState {

    /**
     * Move a ring from a position to another, each point between {@code from} and  {@code to} will have his color changed.
     *
     * @param move The move to make.
     * @return The new state.
     * @throws IllegalArgumentException  if the position {@code from} doesn't have a ring on it or if the {@code to} isn't free.
     * @throws IndexOutOfBoundsException if the position {@code from} isn't in the board.
     */
    public IState move(Move move) {
        Coordinate from = move.getFrom();
        Coordinate to = move.getTo();
        
        if (!(board.get(from) instanceof Ring ring)) {
            throw new IllegalArgumentException("No ring in " + from);
        }

        if (board.get(from).getTeam() != turn) {
            throw new IllegalArgumentException(String.format("The ring in %s doesn't belong to %s team", from, turn));
        }

        if (board.get(to) != null) {
            throw new IllegalArgumentException(String.format("Can't move to %s, there is already a %s", to, board.get(to).getClass()));
        }

        if (!board.containsKey(to)) {
            throw new IndexOutOfBoundsException(String.format("Can't move to %s, out of the map", to));
        }

        if (!lines().isEmpty()) {
            throw new RuntimeException(String.format("Can't move from this state, there is line(s) to remove.", turn));
        }

        Map<Coordinate, Token> board = new HashMap<>();
        for (var entry : board().entrySet()) {
            board.put(entry.getKey(), (entry.getValue() == null) ? null : entry.getValue().clone());
        }

        Map<Team, List<Coordinate>> rings = new HashMap<>();
        for (var entry : rings().entrySet()) {
            rings.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        List<Coordinate> jumpedOver = from.between(Mode.POINTY, to);
        List<Coordinate> createdPawn = new ArrayList<>();
        createdPawn.add(from);

        for (Coordinate c : jumpedOver) {
            if (board.get(c) instanceof Pawn pawn) {
                pawn.changeTeam();
                createdPawn.add(c);
            }
        }

        rings.get(turn).remove(from);
        rings.get(turn).add(to);
        board.put(to, board.get(from));
        board.put(from, new Pawn(turn));

        List<Set<Coordinate>> lines = IState.getPawnsLines(board);

        return new State(board, (lines.isEmpty()) ? turn.other() : turn, lines);
    }

    /**
     * Remove a line of 5 pawn and a ring of the current player.
     *
     * @param removeLine The move to make.
     * @return the new IState with data removed.
     * @throws RuntimeException if there is one of those errors :
     *                          <ul>
     *                              <li>There is no line to remove</li>
     *                              <li>The line given isn't 5 pawns long</li>
     *                              <li>The given ring coordinate isn't a ring of the current player</li>
     *                          </ul>
     */
    @Override
    public IState removeLine(RemoveLine removeLine) {
        Set<Coordinate> removed = removeLine.getLine();
        Coordinate ring = removeLine.getRing();
        
        if (lines.isEmpty()) {
            throw new RuntimeException("No line to remove in this situation, must move a ring.");
        }

        if (!lines.contains(removed)) {
            throw new RuntimeException(String.format("Line %s isn't a correct line", removed));
        }

        if (removed.size() != 5) {
            throw new RuntimeException(String.format("Can only remove a 5 pawn line, a %d were given.", removed.size()));
        }

        if (!(board.get(ring) instanceof Ring)) {
            throw new RuntimeException(String.format("The position %s doesn't contain a ring, there is a : %s", ring, board.get(ring)));
        }

        for (Coordinate c : removed) {
            if (!(board().get(c) instanceof Pawn p && p.getTeam() == board.get(ring).getTeam())) {
                throw new RuntimeException("Pawns and ring to remove must be on the same team");
            }
        }

        Map<Coordinate, Token> board = new HashMap<>();

        for (var entry : board().entrySet()) {
            Token value = (entry.getValue() == null || removed.contains(entry.getKey()) || entry.getKey().equals(ring)) ? null : entry.getValue().clone();
            board.put(entry.getKey(), value);
        }

        List<Set<Coordinate>> lines = IState.getPawnsLines(board);

        return new State(board, (lines.isEmpty()) ? turn.other() : turn, lines);
    }

    @Override
    public Set<Coordinate> availableMoves(Coordinate from) {
        if (!(board.get(from) instanceof Ring)) return Set.of();

        Set<Coordinate> candidates = new HashSet<>();

        for (Direction d : Direction.values()) {
            try {
                Coordinate current = from.toDir(Mode.POINTY, d);
                boolean _continue = true;
                boolean isJumping = false;
                while (isInField(current) && _continue) {
                    Token t = board.get(current);
                    if (t == null) {
                        candidates.add(current);
                        if (isJumping) _continue = false;
                    }

                    if (t instanceof Pawn) isJumping = true;
                    if (t instanceof Ring) _continue = false;

                    current = current.toDir(Mode.POINTY, d);
                }
            } catch (IllegalArgumentException ignored) {

            }
        }

        return candidates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return turn == state.turn && Objects.equals(board, state.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }

    public boolean isInField(Coordinate c) {
        return board().containsKey(c);
    }

    /**
     *
     * @return The actual winner if one exists, null otherwise.
     */
    public Team winner() {
        Map<Team, List<Coordinate>> rings = rings();

        if (rings.get(Team.BLACK).size() <= 2) return Team.BLACK;
        if (rings.get(Team.WHITE).size() <= 2) return Team.WHITE;
        return null;
    }

    public Map<Team, List<Coordinate>> rings() {
        Map<Team, List<Coordinate>> res = Map.of(Team.WHITE, new ArrayList<>(), Team.BLACK, new ArrayList<>());

        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            if (entry.getValue() instanceof Ring r) {
                res.get(r.getTeam()).add(entry.getKey());
            }
        }

        return res;
    }

    @Override
    public List<Set<Coordinate>> getLines() {
        return lines;
    }

    public IState toggleToken(Coordinate position, Team team, Class<?> token) {
        Map<Coordinate, Token> newMap = new HashMap<>(board);

        Token target = newMap.get(position);
        if (target != null && target.getTeam() == team && target.getClass() == token) {
            newMap.put(position, null);
        } else {
            try {
                newMap.put(position, (Token) token.getConstructors()[0].newInstance(team));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new State(newMap, turn, IState.getPawnsLines(newMap));
    }

    public IState removeToken(Coordinate c) {
        Map<Coordinate, Token> newMap = new HashMap<>(board);

        newMap.put(c, null);

        return new State(newMap, turn, lines);
    }
}
