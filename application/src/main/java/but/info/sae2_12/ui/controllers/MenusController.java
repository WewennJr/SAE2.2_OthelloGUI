package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import but.info.sae2_12.ui.AppControllerBase;
import coordinates.Coordinate;
import coordinates.CoordinateCube;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Contrôleur de la barre de menu de l'application Yinsh.
 * Gère les actions liées aux fichiers (sauvegarde, chargement), les informations de l'application et la fermeture du jeu.
 */
public class MenusController extends AppControllerBase {

    @FXML private MenuBar menuBar;

    /**
     * Récupère la fenêtre principale (Stage) associée à la barre de menu.
     * * @return la Stage actuelle ou null si la barre de menu n'est pas encore affichée.
     */
    private Stage getStage(){
        if (menuBar != null && menuBar.getScene() != null){
            return (Stage) menuBar.getScene().getWindow();
        }
        return null;
    }

    /**
     * Action du bouton "À propos".
     * Affiche une fenêtre de dialogue d'information contenant les noms des développeurs du projet.
     */
    public void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("À propos");
        alert.setHeaderText("Informations du groupe");

        Label title = new Label("Application réalisée par ");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label members = new Label("" +
                "   * COLLET Léo\n" +
                "   * DUTERTRE Gabriel\n" +
                "   * LEGROS Ewenn\n" +
                "   * TOULORGE Nathan\n\n"
        );
        members.setStyle("-fx-font-size: 13px;");

        Label copyright = new Label("© Copyright 2026");
        copyright.setStyle("-fx-text-fill: gray;");

        VBox vbox = new VBox(title, members, copyright);

        alert.getDialogPane().setContent(vbox);

        alert.showAndWait();
    }

    /**
     * Action du bouton "Sauvegarder".
     * Ouvre une fenêtre de dialogue de sélection de fichier pour enregistrer la partie actuelle au format ".yinsh".
     */
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

    /**
     * Écrit l'état actuel du jeu dans un fichier de sauvegarde.
     * Le format inclut une sécurité, l'équipe dont c'est le tour, et la liste des pions/anneaux présents sur le plateau.
     * @param file le fichier dans lequel sauvegarder la partie.
     * @param state l'état actuel du jeu.
     */
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

    /**
     * Action du bouton "Charger".
     * Ouvre une fenêtre de dialogue pour sélectionner un fichier de sauvegarde ".yinsh" et met à jour l'état du jeu.
     */
    @FXML
    public void onLoad() throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Charger une partie");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier de sauvegarde Yinsh", "*.yinsh"));

        Stage stage = getStage();
        File file = fc.showOpenDialog(stage);

        if (file != null) {
            ((SimpleObjectProperty<IState>) getController().state).setValue(loadState(file));
        }
    }

    /**
     * Ouvre un fichier de sauvegarde et met l'état du jeu correspondant.
     * Vérifie la validité du fichier avec la sécurité "SAE212" avant de définir le tour actuel et la position des pièces.
     * @param file le fichier de sauvegarde à charger.
     * @return un nouvel objet IState représentant la partie chargée.
     */
    public IState loadState(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String security = reader.readLine();
        if (!security.equals("SAE212")) {
            throw new IOException("Fichier incorrect !");
        }

        Team teamTurn;
        String turnLine = reader.readLine();
        if (turnLine.equals("Turn:Black")) {
            teamTurn = Team.BLACK;
        } else {
            teamTurn = Team.WHITE;
        }

        IState state = new FactoryCube().emptyState();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

            int q = Integer.parseInt(parts[0]);
            int r = Integer.parseInt(parts[1]);
            int s = Integer.parseInt(parts[2]);

            CoordinateCube coordinate = new CoordinateCube(q, r, s);

            String tokenType = parts[3];

            Team tokenTeam;
            if (parts[4].equals("B")) {
                tokenTeam = Team.BLACK;
            } else {
                tokenTeam = Team.WHITE;
            }

            Token token;
            if (tokenType.equals("R")) {
                token = new Ring(tokenTeam);
            } else {
                token = new Pawn(tokenTeam);
            }

            state = state.toggleToken(coordinate, tokenTeam, token.getClass());
        }

        return state;
    }

    /**
     * Action du bouton "Quitter".
     * Ferme l'application si le contrôleur principal est actif.
     */
    @FXML
    public void onQuit() {
        if (getController() != null) {
            Platform.exit();
        }
    }

    /**
     * Initialise le contrôleur.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
