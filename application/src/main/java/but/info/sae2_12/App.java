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
public class App extends Application {

    private static Scene scene;
    public static String mode;

    private static GameMode gameMode;

    public static void setGameMode(GameMode mode) {
        gameMode = mode;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("welcome"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        if (fxml.equals("frame")) fxmlLoader.setController(new AppController());
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}