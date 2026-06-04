package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.ui.objects.HexSquare;
import coordinates.Coordinate;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import java.util.Set;

public class RemoveLineMode extends InteractionMode {
    private Coordinate currentRingSelection = null;

    @Override
    public void handleClick(MouseEvent ev, HexSquare square) {
    }
}
