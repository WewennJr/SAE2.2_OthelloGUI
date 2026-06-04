package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import but.info.sae2_12.ui.AppControllerBase;
import but.info.sae2_12.ui.interaction.EditionMode;
import but.info.sae2_12.ui.interaction.PlayMode;
import but.info.sae2_12.ui.objects.HexSquare;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur du menu des paramètres de jeu.
 * Gère les options d'affichage (zoom, épaisseur des bordures et affichage des coordonnées (2D, Cubique)), les couleurs des cases, le mode édition et plusieurs options de lancements de parties.
 */

public class GameSettingsController extends AppControllerBase {
    private @FXML Button nouveau;
    private @FXML Button testBlanc;
    private @FXML Button testNoir;
    private @FXML Button testState;
    private @FXML Slider epaisseur;
    private @FXML Slider zoom;
    private @FXML RadioButton radioNoir;
    private @FXML RadioButton radioBlanc;
    private @FXML RadioButton radioPion;
    private @FXML RadioButton radioAnneau;
    private @FXML CheckBox coordonnees;
    private @FXML CheckBox edition;
    private @FXML VBox sousZone;
    private @FXML ColorPicker colorPicker1;
    private @FXML ColorPicker colorPicker2;
    private @FXML ColorPicker colorPicker3;
    private @FXML ListView formatChoice;

    /**
     * Initialise les composants de l'interface :
     * les boutons de partie, le mode édition, le zoom, l'épaisseur (de 1 à 5) et le format des coordonnées (2D, Cubique).
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formatChoice.getItems().addAll("2D", "Cubique");
        formatChoice.getSelectionModel().select(0);
        sousZone.setVisible(false);
        sousZone.setManaged(false);
        sousZone.setDisable(true);

        edition.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sousZone.setVisible(newVal);
            sousZone.setManaged(newVal);
            sousZone.setDisable(!newVal);
        });

        nouveau.setOnAction(event -> {
            getController().state.set(new FactoryCube().randomGame());
        });

        testNoir.setOnAction(event -> {
            getController().state.set(new FactoryCube().stateForBlackLineTest());
        });

        testBlanc.setOnAction(event -> {
            getController().state.set(new FactoryCube().stateForWhiteLineTest());
        });

        testState.setOnAction(event -> {
            getController().state.set(new FactoryCube().testState());
        });

        zoom.valueProperty().subscribe(val -> HexSquare.CELLSIZE.set(val.intValue()));
        epaisseur.valueProperty().subscribe(val -> HexSquare.EPAISSEUR.set(val.intValue()));
    }

    /**
     * Appelé après l'injection du contrôleur principal (AppController).
     * Met en place les liaisons des composants de l'interface, les listeners d'événements et le comportement du mode édition.
     */

    @FXML
    @Override
    public void onMainControllerSetup() {
        colorPicker1.valueProperty().bindBidirectional(AppController.couleur1);
        colorPicker2.valueProperty().bindBidirectional(AppController.couleur2);
        colorPicker3.valueProperty().bindBidirectional(AppController.couleur3);

        AppController.couleur1.addListener((obs, oldVal, newVal) -> getController().gameController.initMap());
        AppController.couleur2.addListener((obs, oldVal, newVal) -> getController().gameController.initMap());
        AppController.couleur3.addListener((obs, oldVal, newVal) -> getController().gameController.initMap());

        HexSquare.COORDINATES_VISIBLE.set(coordonnees.isSelected());
        coordonnees.selectedProperty().addListener((obs, oldVal, newVal) -> HexSquare.COORDINATES_VISIBLE.set(newVal));

        formatChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                getController().gameController.setCoordMode((String) newVal);
            }
        });

        Runnable updateEditMode = () -> {
            if (edition.isSelected()) {
                Team team;
                if (radioNoir.isSelected()) {
                    team = Team.BLACK;
                } else {
                    team = Team.WHITE;
                }
                Token token;
                if (radioPion.isSelected()) {
                    token = new Pawn(team);
                } else {
                    token = new Ring(team);
                }
                getController().setCurrentMode(new EditionMode(token));
            }
        };

        edition.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) updateEditMode.run();
            else getController().setCurrentMode(new PlayMode());
        });

        radioNoir.selectedProperty().addListener((obs, o, n) -> updateEditMode.run());
        radioBlanc.selectedProperty().addListener((obs, o, n) -> updateEditMode.run());
        radioPion.selectedProperty().addListener((obs, o, n) -> updateEditMode.run());
        radioAnneau.selectedProperty().addListener((obs, o, n) -> updateEditMode.run());
    }

    public boolean isEditionEnable() {
        return edition.isSelected();
    }
}
