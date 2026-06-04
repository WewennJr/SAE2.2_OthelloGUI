package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.ui.objects.HexSquare;
import javafx.scene.input.MouseEvent;

/**
 * Permet de debug avant l'arrivée du reste des classes
 * Pas interressant
 */
public class TempInteractMode extends InteractionMode{
    @Override
    public void handleClick(MouseEvent ev, HexSquare square) {
        square.appControllerInstance.getState().board().replace(
                square.coordinate, null
        );
    }
}
