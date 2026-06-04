package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.model.state.State;
import but.info.sae2_12.model.tokens.Token;
import but.info.sae2_12.ui.objects.HexSquare;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

public class EditionMode extends InteractionMode {
    private final Token token;

    public EditionMode(Token token) {
        this.token = token;
    }

    @Override
    public void handleClick(MouseEvent ev, HexSquare square) {
        var board = new HashMap<>(square.appControllerInstance.getState().board());
        if (ev.getButton().equals(MouseButton.SECONDARY)) {
            board.put(square.coordinate, null);
            square.appControllerInstance.state.set(new State(board, square.appControllerInstance.getState().turn(),  square.appControllerInstance.getState().getLines()
            ));

        } else if (ev.getButton().equals(MouseButton.PRIMARY)) {
            board.put(square.coordinate, token);
            square.appControllerInstance.state.set(new State(
                    board,
                    square.appControllerInstance.getState().turn(),
                    square.appControllerInstance.getState().getLines()
            ));
        }
    }
}
