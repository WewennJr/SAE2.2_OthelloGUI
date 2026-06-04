package but.info.sae2_12.ui.controllers;
import but.info.sae2_12.model.factory.FactoryDoubled;
import but.info.sae2_12.ui.AppControllerBase;
import but.info.sae2_12.ui.objects.HexSquare;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSettingsController extends AppControllerBase {
    private @FXML Button nouveau;
    private @FXML Button testBlanc;
    private @FXML Button testNoir;
    private @FXML Button testDouble;
    private @FXML Slider epaisseur;
    private @FXML Slider zoom;
    private @FXML CheckBox coordonnees;
    private @FXML CheckBox edition;
    private @FXML VBox sousZone;
    private @FXML ColorPicker colorPicker1;
    private @FXML ColorPicker colorPicker2;
    private @FXML ColorPicker colorPicker3;
    private @FXML ListView formatChoice;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        sousZone.setVisible(false);
        sousZone.setManaged(false);
        sousZone.setDisable(true);

        edition.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sousZone.setVisible(newVal);
            sousZone.setManaged(newVal);
            sousZone.setDisable(!newVal);
        });

        coordonnees.selectedProperty().addListener((obs, oldVal, newVal) -> {
            getController().hexSquares.values().forEach(
                    hex ->hex.setVisibleAndChilds(newVal)
            );
        });

        nouveau.setOnAction(event -> {
            getController().state.set(new FactoryDoubled().randomGame());
        });

        testNoir.setOnAction(event -> {
            getController().state.set(new FactoryDoubled().stateForBlackLineTest());
        });

        testBlanc.setOnAction(event -> {
            getController().state.set(new FactoryDoubled().stateForWhiteLineTest());
        });

        testDouble.setOnAction(event -> {
            getController().state.set(new FactoryDoubled().doubleLineStateTest());
        });
    }

    @FXML
    public void onAppControllerSet(){
        colorPicker1.valueProperty().bindBidirectional(AppController.couleur1);
        colorPicker2.valueProperty().bindBidirectional(AppController.couleur2);
        colorPicker3.valueProperty().bindBidirectional(AppController.couleur3);

        AppController.couleur1.addListener((obs, oldVal, newVal) -> getController().gameController.initMap());
        AppController.couleur2.addListener((obs, oldVal, newVal) -> getController().gameController.initMap());
        AppController.couleur3.addListener((obs, oldVal, newVal) -> getController().gameController.initMap());
    }
}
