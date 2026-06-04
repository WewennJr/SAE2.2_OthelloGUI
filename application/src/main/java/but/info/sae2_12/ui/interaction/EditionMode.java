package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.model.state.State;
import but.info.sae2_12.model.tokens.Token;
import but.info.sae2_12.ui.objects.HexSquare;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

/**
 * Mode d’édition du plateau permettant d’ajouter ou de supprimer des tokens
 * directement en cliquant sur les cases.
 */
public class EditionMode extends InteractionMode {
    private final Token token;

    /**
     * Crée un mode d’édition avec le token à placer.
     *
     * @param token token à insérer lors d’un clic gauche
     */
    public EditionMode(Token token) {
        this.token = token;
    }

    /**
     * Gère les clics sur une case du plateau.
     * - Clic gauche : ajoute un token sur la case
     * - Clic droit : supprime le contenu de la case
     *
     * @param ev événement de souris
     * @param square case hexagonale cliquée
     */
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
