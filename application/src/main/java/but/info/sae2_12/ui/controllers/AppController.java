package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.App;
import but.info.sae2_12.model.GameMode;
import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.ui.interaction.InteractionMode;
import but.info.sae2_12.ui.interaction.PlayMode;
import but.info.sae2_12.ui.objects.HexSquare;
import coordinates.Coordinate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import but.info.sae2_12.model.Team;

public class AppController implements Initializable {
    public ObjectProperty<InteractionMode> currentMode = new SimpleObjectProperty<>(new PlayMode());

    public HashMap<Coordinate, HexSquare> hexSquares = new HashMap<>();
    public final SimpleObjectProperty<IState> state = new SimpleObjectProperty<>(new FactoryCube().stateForWhiteLineTest());

    public @FXML GameController gameController;
    public @FXML StackPane gameContainer;
    public @FXML GameSettingsController gameSettingsController;
    public @FXML MenusController menusController;
    public @FXML IAController iaController;
    public @FXML GameStateController gameStateController;
    public static final ObjectProperty<Color> couleur1 = new SimpleObjectProperty<>(Color.GREY);
    public static final ObjectProperty<Color> couleur2 = new SimpleObjectProperty<>(Color.DARKGRAY);
    public static final ObjectProperty<Color> couleur3 = new SimpleObjectProperty<>(Color.LIGHTGRAY);

    private static GameMode gameMode;
    private boolean gameEnded = false;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gameController.setController(this);
        this.gameSettingsController.setController(this);
        this.menusController.setController(this);
        this.iaController.setController(this);
        this.gameStateController.setController(this);
        setupGameClip();
        this.gameSettingsController.onAppControllerSet();

        this.setGameMode(App.getGameMode());

        state.addListener((obs, oldState, newState) -> {
            if (newState != null) {
                iaController.updateEvaluationBar(newState);
                // Vérifier si la partie est terminée
                if (!gameEnded) {
                    Team winner = null;
                    try {
                        if (newState.rings().get(Team.BLACK).size() <= 2) winner = Team.BLACK;
                        else if (newState.rings().get(Team.WHITE).size() <= 2) winner = Team.WHITE;
                    } catch (Exception ignored) {
                    }

                    if (winner != null) {
                        gameEnded = true;
                        String winnerText = (winner == Team.WHITE) ? "Blanc" : "Noir";
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Partie terminée");
                            alert.setHeaderText(null);
                            alert.setContentText(winnerText + " a gagné");
                            ButtonType home = new ButtonType("Retour à l'accueil", ButtonBar.ButtonData.OK_DONE);
                            alert.getButtonTypes().setAll(home);
                            alert.showAndWait().ifPresent(response -> {
                                if (response == home) {
                                    try {
                                        App.setRoot("welcome");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        });
                    }
                }
            }
        });

        state.subscribe((state) -> gameStateController.refreshGameStateLabels(state));
        gameStateController.refreshGameStateLabels(state.getValue());
    }

    private void setupGameClip() {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(gameContainer.widthProperty());
        clip.heightProperty().bind(gameContainer.heightProperty());
        gameContainer.setClip(clip);
    }

    public ObjectProperty<Color> getCouleur1(){
        return couleur1;
    }
    public ObjectProperty<Color> getCouleur2(){
        return couleur2;
    }
    public ObjectProperty<Color> getCouleur3(){
        return couleur3;
    }

    public InteractionMode getCurrentMode() {
        return currentMode.getValue();
    }

    public IState getState() {
        return state.getValue();
    }

    public void setState(IState newState) {
        this.state.setValue(newState);
    }

    public void setCurrentMode(InteractionMode mode) {
        this.currentMode.set(mode);
    }

    public static void setGameMode(GameMode mode) {
        gameMode = mode;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }
}