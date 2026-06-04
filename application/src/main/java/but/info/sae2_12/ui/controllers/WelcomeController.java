package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.App;
import but.info.sae2_12.model.GameMode;
import javafx.fxml.FXML;
import javafx.application.Platform;

/**
 * Contrôleur de l'écran d'accueil.
 */
public class WelcomeController {

    /**
     * Lance une partie en Player Vs Player.
     */
    @FXML
    public void onPlayerVsPlayer() {
        try {
            App.setGameMode(GameMode.PLAYER_VS_PLAYER);
            App.setRoot("frame");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lance une partie en Player Vs AI.
     */
    @FXML
    public void onPlayerVsIA() {
        try {
            App.setGameMode(GameMode.PLAYER_VS_AI);
            App.setRoot("frame");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Quitte l'application quand on a lancé pour rien ou fini une partie.
     */
    @FXML
    public void onQuit() {
        Platform.exit();
    }
}