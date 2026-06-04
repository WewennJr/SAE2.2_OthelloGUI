package but.info.sae2_12.ui;

import but.info.sae2_12.ui.controllers.AppController;

public abstract class AppControllerBase {
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
}
