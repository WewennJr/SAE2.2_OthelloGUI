package but.info.sae2_12.model.factory;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Ring;
import coordinates.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public interface IFactory {
    IState testState();
    IState stateForBlackLineTest();
    IState stateForWhiteLineTest();
    IState emptyState();
    IState doubleLineStateTest();
    IState emptyGame();

    default IState randomGame() {
        IState state = emptyState();

        List<Coordinate> squares = new ArrayList<>(state.board().keySet().stream().toList());

        for (Team t : Team.values()) {
            for (int i = 0; i < 5; i++) {
                Coordinate pos = squares.remove(new Random().nextInt(squares.size()));
                state = state.toggleToken(pos, t, Ring.class);
            }
        }

        return state;
    }
}
