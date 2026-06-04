package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.ui.AppControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Contrôleur chargé d’afficher et mettre à jour les informations
 * liées à l’état actuel de la partie (joueur, phase, pions, anneaux).
 */
public class GameStateController extends AppControllerBase {

    @FXML public Label playerStatus;
    @FXML public Label phaseStatus;
    @FXML public Label nbAnneauxStatus;
    @FXML public Label nbPionsStatus;

    /**
     * Met à jour l’affichage des informations du jeu.
     *
     * @param currentState état actuel de la partie
     */
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

    /**
     * Détermine la phase actuelle du jeu (déplacement ou suppression de ligne).
     *
     * @param currentState état actuel de la partie
     * @return la phase sous forme de chaîne de caractères
     */
    private String getPhase(IState currentState) {
        if (currentState.getLines().isEmpty()) {
            return "Déplacement";
        }
        else {
            return "Suppression de ligne";
        }
    }

    /**
     * Compte le nombre d’anneaux du joueur courant.
     *
     * @param currentState état actuel de la partie
     * @return nombre d’anneaux du joueur actif
     */
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

    /**
     * Compte le nombre de pions du joueur courant sur le plateau.
     *
     * @param currentState état actuel de la partie
     * @return nombre de pions du joueur actif
     */
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

    /**
     * Convertit une équipe en texte affichable.
     *
     * @param team équipe (blanc ou noir)
     * @return chaîne représentant l’équipe
     */
    private String getTeam(Team team) {
        if (team == Team.WHITE) {
            return "Blanc";
        }
        else {
            return "Noir";
        }
    }

    /**
     * Initialise sinon ça ne marche pas.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
