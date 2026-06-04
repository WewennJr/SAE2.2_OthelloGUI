package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.ui.AppControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameStateController extends AppControllerBase {

    @FXML public Label playerStatus;
    @FXML public Label phaseStatus;
    @FXML public Label nbAnneauxStatus;
    @FXML public Label nbPionsStatus;

    public void refreshGameStateLabels(IState currentState) {
        if (currentState == null) {
            playerStatus.setText("...");
            phaseStatus.setText("...");
            nbAnneauxStatus.setText("0");
            nbPionsStatus.setText("0");
            return;
        }

        playerStatus.setText(getTeam(currentState.turn()));
        phaseStatus.setText(getPhase(currentState));
        nbAnneauxStatus.setText(String.valueOf(countRings(currentState)));
        nbPionsStatus.setText(String.valueOf(countPawns(currentState)));
    }

    private String getPhase(IState currentState) {
        if (currentState.getLines().isEmpty()) {
            return "Déplacement";
        }
        else {
            return "Suppression de ligne";
        }
    }

    private int countRings(IState currentState) {
        if (currentState == null) {
            return 0;
        }
        List<?> rings = currentState.rings().get(currentState.turn());
        if ((rings == null)) {
            return 0;
        }
        else {
            return rings.size();
        }
    }

    private int countPawns(IState currentState) {
        if (currentState == null) {
            return 0;
        }
        int count = 0;
        for (Object token : currentState.board().values()) {
            if (token instanceof but.info.sae2_12.model.tokens.Pawn p) {
                if (p.getTeam() == currentState.turn()) {
                    count++;
                }
            }
        }
        return count;
    }

    private String getTeam(Team team) {
        if (team == Team.WHITE) {
            return "Blanc";
        }
        else {
            return "Noir";
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
