package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.App;
import but.info.sae2_12.model.GameMode;
import javafx.fxml.FXML;
import javafx.application.Platform;

public class WelcomeController {

    @FXML
    public void onPlayerVsPlayer() {
        try {
            App.setGameMode(GameMode.PLAYER_VS_PLAYER);
            App.setRoot("frame");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onPlayerVsIA() {
        try {
            App.setGameMode(GameMode.PLAYER_VS_AI);
            App.setRoot("frame");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onQuit() {
        Platform.exit();
    }
}