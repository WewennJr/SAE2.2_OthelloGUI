package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.ui.objects.HexSquare;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Classe abstraite pour les modes d’interaction avec le plateau.
 */
public abstract class InteractionMode {
    private Paint oldColor;

    /**
     * Gère l’action effectuée lors d’un clic sur une case.
     *
     * @param ev événement de souris
     * @param square case hexagonale concernée
     */
    public abstract void handleClick(MouseEvent ev, HexSquare square);

    /**
     * Appelé lorsque la souris entre dans une case.
     * Change temporairement la couleur de la case.
     *
     * @param ev événement de souris
     * @param square case hexagonale survolée
     */
    public void entered(MouseEvent ev, HexSquare square) {
        this.oldColor = square.getFill();
        square.setFill(Color.ORANGE);
    };

    /**
     * Appelé lorsque la souris quitte une case.
     * Restaure la couleur précédente de la case.
     *
     * @param ev événement de souris
     * @param square case hexagonale concernée
     */
    public void exited(MouseEvent ev, HexSquare square) {
        square.setFill(oldColor);
    }
}
