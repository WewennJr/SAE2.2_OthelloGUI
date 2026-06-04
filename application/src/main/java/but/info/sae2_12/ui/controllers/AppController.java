package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.factory.FactoryDoubled;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.ui.interaction.InteractionMode;
import but.info.sae2_12.ui.interaction.PlayMode;
import but.info.sae2_12.ui.interaction.TempInteractMode;
import but.info.sae2_12.ui.objects.HexSquare;
import coordinates.Coordinate;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    public ObjectProperty<IState> state = new SimpleObjectProperty<>(new FactoryDoubled().stateForBlackLineTest());

    public ObservableValue<InteractionMode> currentMode = new SimpleObjectProperty<>(new PlayMode());

    public HashMap<Coordinate, HexSquare> hexSquares = new HashMap<>();

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
    }

    public InteractionMode getCurrentMode() {
        return currentMode.getValue();
    }

    public IState getState() {
        return state.getValue();
    }
}