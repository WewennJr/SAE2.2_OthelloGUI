package but.info.sae2_12.ui;

import but.info.sae2_12.ui.controllers.AppController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AppControllerBase implements Initializable {
    private AppController controller;

    public AppController getController() {
        return controller;
    }

    public void setController(AppController controller) {
        this.controller = controller;
        this.onMainControllerSetup();
    }

    public void onMainControllerSetup() {
        return;
    }

    public abstract void initialize(URL url, ResourceBundle rb);
}
