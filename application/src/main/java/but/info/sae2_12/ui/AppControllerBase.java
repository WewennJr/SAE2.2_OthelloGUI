package but.info.sae2_12.ui;

import but.info.sae2_12.ui.controllers.AppController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AppControllerBase implements Initializable {
    private AppController controller;

    /**
     * Permet de récupérer le controlleur en chef
     * @return AppController
     */
    public AppController getController() {
        return controller;
    }

    /**
     * Définir de controlleur en chef (à ne pas utiliser, normalement)
     * @param controller
     */
    public void setController(AppController controller) {
        this.controller = controller;
        this.onMainControllerSetup();
    }

    /**
     * Event custom qui se déclanche après initialize et le constructeur du controlleur principal
     */
    public void onMainControllerSetup() {
        return;
    }

    /**
     * (hérité de javafx)
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param rb
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public abstract void initialize(URL url, ResourceBundle rb);

    public void onAppControllerSet(){

    }
}
