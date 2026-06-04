package but.info.sae2_12.ui.controllers;
import but.info.sae2_12.ui.AppControllerBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSettingsController extends AppControllerBase {
    private @FXML Slider epaisseur;
    private @FXML Slider zoom;
    private @FXML CheckBox coordonnees;
    private @FXML CheckBox edition;
    private @FXML VBox sousZone;
    private @FXML ColorPicker colorPicker1;
    private @FXML ColorPicker colorPicker2;
    private @FXML ColorPicker colorPicker3;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sousZone.setVisible(false);
        sousZone.setManaged(false);
        sousZone.setDisable(true);

        edition.selectedProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Edition cochée : " + newVal);
            sousZone.setVisible(newVal);
            sousZone.setManaged(newVal);
            sousZone.setDisable(!newVal);
        });
    }

    @FXML
    public void setAppControlleur(){
        colorPicker1.valueProperty().bindBidirectional(AppController.couleur1);
        colorPicker2.valueProperty().bindBidirectional(AppController.couleur2);
        colorPicker3.valueProperty().bindBidirectional(AppController.couleur3);
    }
}
