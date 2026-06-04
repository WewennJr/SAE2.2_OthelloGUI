package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import but.info.sae2_12.ui.AppControllerBase;
import coordinates.Coordinate;
import coordinates.CoordinateCube;
import coordinates.CoordinateDoubled;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Map;

public class MenusController extends AppControllerBase {

    @FXML private MenuBar menuBar;

    private Stage getStage(){
        if (menuBar != null && menuBar.getScene() != null){
            return (Stage) menuBar.getScene().getWindow();
        }
        return null;
    }

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

    @FXML
    public void onSave() throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Sauvegarder la partie");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier de sauvegarde Yinsh", "*.yinsh"));
        fc.setInitialFileName("partie.yinsh");

        Stage stage = getStage();
        File file = fc.showSaveDialog(stage);
        saveState(file, getController().state.getValue());
    }

    public void saveState(File file, IState state) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        writer.write("SAE212");
        writer.newLine();

        if (state.turn() == Team.BLACK) {
            writer.write("Turn:Black");
        } else {
            writer.write("Turn:White");
        }
        writer.newLine();

        for (Map.Entry<Coordinate, Token> entry : state.board().entrySet()) {
            CoordinateCube c = (CoordinateCube) entry.getKey();
            Token t = entry.getValue();
            String token = "";
            String team = "";
            if (t != null) {
                if (t instanceof Ring) {
                    token = "R";
                } else {
                    token = "P";
                }
                if (t.getTeam() == Team.BLACK) {
                    team = "B";
                } else {
                    team = "W";
                }
                String line = c.getQ() + "," + c.getR() + "," + c.getS() + "," + token + "," + team;
                writer.write(line);
                writer.newLine();
            }
        }
        writer.close();
    }

}
