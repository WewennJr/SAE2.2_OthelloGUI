package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.ui.objects.HexSquare;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class InteractionMode {
    private Paint oldColor;
    public abstract void handleClick(MouseEvent ev, HexSquare square);
    public void entered(MouseEvent ev, HexSquare square) {
        this.oldColor = square.getFill();
        square.setFill(Color.ORANGE);
    };
    public void exited(MouseEvent ev, HexSquare square) {
        square.setFill(oldColor);
    }
}
