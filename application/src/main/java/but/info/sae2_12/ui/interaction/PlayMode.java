package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.model.GameMode;
import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.ui.objects.HexSquare;
import coordinates.Coordinate;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.Set;

/**
 * Mode de jeu principal gérant les interactions de déplacement des anneaux.
 * Permet de sélectionner un anneau, d’afficher les coups possibles et d’effectuer un déplacement.
 * Gère également la transition vers la suppression de ligne et le déclenchement de l’IA.
 */
public class PlayMode extends InteractionMode {
    private Coordinate currentRingSelection = null;

    /**
     * Gère les clics sur une case en mode jeu.
     * - Sélection d’un anneau
     * - Affichage des déplacements possibles
     * - Exécution d’un mouvement
     * - Déclenchement éventuel de l’IA
     *
     * @param ev événement de souris
     * @param square case hexagonale cliquée
     */
    @Override
    public void handleClick(MouseEvent ev, HexSquare square) {

        IState currentState = square.appControllerInstance.getState();

        if (currentState.board().get(square.coordinate) instanceof Ring ring && currentRingSelection == null) {

            if (!currentState.turn().equals(ring.getTeam())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ce n'est pas votre tour.");
                alert.show();
                return;
            }

            square.toggleIndicator();
            currentRingSelection = square.coordinate;

            Set<Coordinate> validMoves = currentState.availableMoves(square.coordinate);
            for (Coordinate validMove : validMoves) {
                square.appControllerInstance.hexSquares.get(validMove).toggleIndicator();
            }
        }

        if (currentState.board().get(square.coordinate) == null) {
            try {
                square.appControllerInstance.state.set(
                        currentState.move(new Move(currentRingSelection, square.coordinate))
                );

                currentRingSelection = null;

                for (HexSquare hexSquare : square.appControllerInstance.hexSquares.values()) {
                    hexSquare.setIsIndicatorVisible(false);
                }

                if (!square.appControllerInstance.getState().getLines().isEmpty()) {

                    square.appControllerInstance.setCurrentMode(new RemoveLineMode());
                }

                // Si on est en Player vs IA et que c'est bien au tour de l'IA après le coup,
                // lancer l'IA. Sinon (par ex. lignes à supprimer par le joueur), ne rien faire.
                if (square.appControllerInstance.getGameMode() == GameMode.PLAYER_VS_AI
                        && square.appControllerInstance.getState().turn() == but.info.sae2_12.model.Team.BLACK) {

                    PauseTransition pause = new PauseTransition(Duration.millis(300));
                    pause.setOnFinished(e -> square.appControllerInstance.iaController.playAI(square.appControllerInstance));
                    pause.play();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vous ne pouvez pas jouer ce déplacement");
                alert.show();
            }
        }
    }
}