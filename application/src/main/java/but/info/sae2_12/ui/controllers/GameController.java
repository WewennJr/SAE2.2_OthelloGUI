package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.state.State;
import but.info.sae2_12.ui.AppControllerBase;
import but.info.sae2_12.ui.interaction.RemoveLineMode;
import but.info.sae2_12.ui.interaction.PlayMode;
import but.info.sae2_12.ui.objects.HexSquare;
import coordinates.Coordinate;
import coordinates.CoordinateCube;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
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
        pane.setViewOrder(Double.MAX_VALUE);
        pane.toBack();
    }

    public void initMap() {
        for (HexSquare square : this.getController().hexSquares.values()) {
            square.cleanup();
        }
        this.getController().hexSquares.clear();
        this.pane.getChildren().clear();

        List<Set<Coordinate>> lines = IState.getPawnsLines(getController().getState().board());
        getController().setState(new State(
                getController().getState().board(),
                getController().getState().turn(),
                lines
        ));

        if (!lines.isEmpty()) {
            this.getController().setCurrentMode(new RemoveLineMode());
        } else {
            // s'il n'y a plus de lignes, revenir au mode de jeu normal
            this.getController().setCurrentMode(new PlayMode());
        }

        this.getController().getState().board().forEach((coord, tok) -> {
            HexSquare hexSquare = new HexSquare(this.getController(), coord, computeColor(coord.to2DCoordinate().x(), coord.to2DCoordinate().y()).get());
            this.getController().hexSquares.put(coord, hexSquare);
        });

        for (Set<Coordinate> line : this.getController().getState().getLines()) {
            for (Coordinate coordinate : line) {
                this.getController().hexSquares.get(coordinate).setFill(Color.LIGHTGREEN);
            }
        }
    }

    private void setupNavigation() {
        HexSquare.CELLSIZE.set(35);
        this.pane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
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

        this.pane.setOnScroll(event -> {
            double zoomFactor = 1.05;
            if (event.getDeltaY() < 0) {
                zoomFactor = 0.95;
            }
            pane.setScaleX(pane.getScaleX() * zoomFactor);
            pane.setScaleY(pane.getScaleY() * zoomFactor);
        });
    }

    public void setCoordMode(String mode) {
        getController().hexSquares.forEach((coord, hex) -> {
            if (mode.equals("Cubique") && coord instanceof CoordinateCube c) {
                hex.label.setText(c.getQ() + "," + c.getR() + "," + c.getS());
            } else {
                hex.label.setText(coord.to2DCoordinate().x() + "," + coord.to2DCoordinate().y());
            }
        });
    }

    public ObjectProperty<Color> computeColor(int x, int y) {
        return switch (x % 3) {
            case 0 -> AppController.couleur1;
            case 1 -> AppController.couleur2;
            case 2 -> AppController.couleur3;
            default -> AppController.couleur1;
        };
    }
}