package but.info.sae2_12.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private @FXML GameController gameController;
    private @FXML GameSettingsController gameSettingsController;
    private @FXML MenusController menusController;
    private @FXML IAController iaController;
    private @FXML GameStateController gameStateController;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gameController.setController(this);
        this.gameSettingsController.setController(this);
        this.menusController.setController(this);
        this.iaController.setController(this);
        this.gameStateController.setController(this);
    }
}
