package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.state.State;
import but.info.sae2_12.ui.AppControllerBase;
import but.info.sae2_12.ui.interaction.RemoveLineMode;
import but.info.sae2_12.ui.interaction.PlayMode;
import but.info.sae2_12.ui.objects.HexSquare;
import coordinates.Coordinate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class GameController extends AppControllerBase {
    @FXML
    public Pane pane;

    public double translationXValue = 0;
    public double translationYValue = 0;
    private double lastMouseX;
    private double lastMouseY;

    @Override
    public void onMainControllerSetup() {
        super.onMainControllerSetup();
        initMap();
        setupNavigation();

        this.getController().state.subscribe((state) -> initMap());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initMap() {
        this.getController().hexSquares.clear();
        this.pane.getChildren().clear();
        List<Set<Coordinate>> lines = IState.getPawnsLines(getController().getState().board());
        getController().setState(new State(
                getController().getState().board(),
                getController().getState().turn(),
                lines
        ));
        if(!lines.isEmpty()) {
            this.getController().setCurrentMode(new RemoveLineMode());
        } else {
            // s'il n'y a plus de lignes, revenir au mode de jeu normal
            this.getController().setCurrentMode(new PlayMode());
        }


        this.getController().getState().board().forEach((coord, tok) -> {
            HexSquare hexSquare = new HexSquare(this.getController(), coord, computeColor(coord.to2DCoordinate().x(), coord.to2DCoordinate().y()));
            this.getController().hexSquares.put(coord, hexSquare);
        });

        for (Set<Coordinate> line : this.getController().getState().getLines()) {
            for (Coordinate coordinate : line) {
                this.getController().hexSquares.get(coordinate).setFill(Color.LIGHTGREEN);
            }
        }

    }

    private void setupNavigation() {
        this.pane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (!(event.getPickResult().getIntersectedNode() instanceof HexSquare)) {
                lastMouseX = event.getSceneX();
                lastMouseY = event.getSceneY();
            }
        });

        this.pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (!(event.getPickResult().getIntersectedNode() instanceof HexSquare)) {
                double deltaX = event.getSceneX() - lastMouseX;
                double deltaY = event.getSceneY() - lastMouseY;
                translationXValue += deltaX;
                translationYValue += deltaY;
                pane.setTranslateX(translationXValue);
                pane.setTranslateY(translationYValue);
                lastMouseX = event.getSceneX();
                lastMouseY = event.getSceneY();
            }
        });
    }

    public Color computeColor(int x, int y) {
        return switch (x % 3) {
            case 0 -> Color.LIGHTGRAY;
            case 1 -> Color.GRAY;
            case 2 -> Color.DARKGRAY;
            default -> Color.BLACK;
        };
    }
}