package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.ui.objects.HexSquare;
import coordinates.Coordinate;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import java.util.Set;

public class PlayMode extends InteractionMode {
    private Coordinate currentRingSelection = null;

    @Override
    public void handleClick(MouseEvent ev, HexSquare square) {
        IState currentState = square.appControllerInstance.getState();
        if (currentState.board().get(square.coordinate) instanceof Ring ring && currentRingSelection == null) {
            if(!currentState.turn().equals(ring.getTeam())) {
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
                square.appControllerInstance.state.set(currentState.move(new Move(currentRingSelection, square.coordinate)));
                currentRingSelection = null;
                for (HexSquare hexSquare : square.appControllerInstance.hexSquares.values()) {
                    hexSquare.setIsIndicatorVisible(false);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vous ne pouvez pas jouer ce déplacement");
                alert.show();
            }
        }
    }
}
