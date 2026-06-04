package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.ui.AppControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MenusController extends AppControllerBase {
    public void onAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Informations du groupe");
        alert.setContentText("Application réalisée par :\n" +
                "   COLLET Léo\n" +
                "   DUTERTRE Gabriel\n" +
                "   LEGROS Ewenn\n" +
                "   TOULORGE Nathan\n");
        alert.showAndWait();
    }
}
