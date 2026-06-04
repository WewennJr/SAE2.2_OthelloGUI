package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.ui.objects.HexSquare;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EditionMode extends InteractionMode {
    @Override
    public void handleClick(MouseEvent ev, HexSquare square) {
        if (ev.getButton().equals(MouseButton.SECONDARY)) {
            square.appControllerInstance.getState().board().replace(
                    square.coordinate, null
            );
        } else if (ev.getButton().equals(MouseButton.PRIMARY)) {
            //TODO 3.3.1.3
        }
    }
}
