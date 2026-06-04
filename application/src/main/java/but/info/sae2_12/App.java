package but.info.sae2_12;

import but.info.sae2_12.model.GameMode;
import but.info.sae2_12.ui.controllers.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 * https://www.redblobgames.com/grids/hexagons/
 */

/**
 * Classe principale de l'application pour le projet Yinsh.
 * Initialise la scène principale et gère le chargement ainsi que le changement des FXML.
 */
public class App extends Application {

    /**
     * La scène principale de l'application.
     */
    private static Scene scene;

    /**
     * Le mode de jeu actuel ou de l'application.
     */
    public static String mode;

    /**
     * Initialisation principale pour l'application.
     * Initialise la scène avec la vue de bienvenue ("welcome") et affiche la fenêtre.
     * @param stage la fenêtre principale.
     */

    private static GameMode gameMode;

    public static void setGameMode(GameMode mode) {
        gameMode = mode;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("welcome"), 900, 600);
        stage.setScene(scene);
        stage.setTitle("Yinsh");
        stage.show();
    }

    /**
     * Remplace la scène actuelle par une nouvelle vue FXML.
     * Permet de changer d'écran (par exemple, passer de l'accueil au plateau de jeu).
     * @param fxml le nom du fichier FXML à charger.
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Charge un fichier FXML.
     * Si la vue demandée est "frame", un nouveau AppController lui est associé.
     * @param fxml le nom du fichier FXML à charger.
     * @return le composant Parent de la vue chargée.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        if (fxml.equals("frame")) fxmlLoader.setController(new AppController());
        return fxmlLoader.load();
    }

    /**
     * Méthode principale de Java.
     * Lance l'application JavaFX.
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        launch();
    }

}