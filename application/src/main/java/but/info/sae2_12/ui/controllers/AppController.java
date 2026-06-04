package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.state.IState;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    public final SimpleObjectProperty<IState> state = new SimpleObjectProperty<>(new FactoryCube().stateForWhiteLineTest());

    public @FXML GameController gameController;
    public @FXML GameSettingsController gameSettingsController;
    public @FXML MenusController menusController;
    public @FXML IAController iaController;
    public @FXML GameStateController gameStateController;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gameController.setController(this);
        this.gameSettingsController.setController(this);
        this.menusController.setController(this);
        this.iaController.setController(this);
        this.gameStateController.setController(this);

        state.addListener((observable, oldState, newState) -> gameStateController.refreshGameStateLabels(newState));
        gameStateController.refreshGameStateLabels(state.getValue());
    }
}
