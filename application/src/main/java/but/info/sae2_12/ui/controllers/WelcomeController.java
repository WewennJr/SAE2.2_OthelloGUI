package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.App;
import javafx.fxml.FXML;
import javafx.application.Platform;

public class WelcomeController {

    @FXML
    public void onPlayerVsPlayer() {
        try { App.setRoot("frame"); }
        catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void onPlayerVsIA() {
        try { App.setRoot("frame"); }
        catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void onLoad() {
        // à implémenter
    }

    @FXML
    public void onQuit() {
        Platform.exit();
    }
}