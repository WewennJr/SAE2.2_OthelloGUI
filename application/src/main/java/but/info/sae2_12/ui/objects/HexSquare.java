package but.info.sae2_12.ui.objects;

import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import but.info.sae2_12.ui.controllers.AppController;
import coordinates.Coordinate;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.TextAlignment;

public class HexSquare extends Polygon {
    public static final int CELLSIZE = 50;
    public AppController appControllerInstance;
    SimpleBooleanProperty isIndicatorVisible = new SimpleBooleanProperty(false);
    Color color;
    public Coordinate coordinate;
    Circle shape;
    Circle visualIndicator;
    Label label;

    public HexSquare(AppController appController, Coordinate coordinate, Color color) {
        Token token = appController.getState().board().get(coordinate);
        this.coordinate = coordinate;
        this.color = color;
        this.appControllerInstance = appController;
        this.visualIndicator = new Circle(CELLSIZE / 3.5, Color.RED);

        if (token instanceof Pawn pawn) {
            this.shape = new Circle(CELLSIZE / 4.0, pawn.getTeam().getColor());
        }
        if (token instanceof Ring ring) {
            this.shape = new Circle(CELLSIZE / 3.0, ring.getTeam().getColor());
            this.shape.setFill(Color.TRANSPARENT);
            this.shape.setStrokeWidth(3f);
            this.shape.setStroke(ring.getTeam().getColor());
        }


        getPoints().addAll((double) (-CELLSIZE / 2), (double) (-CELLSIZE / 4), (double) 0, (double) (-CELLSIZE / 2), (double) (CELLSIZE / 2), (double) (-CELLSIZE / 4), (double) (CELLSIZE / 2), (double) (CELLSIZE / 4), (double) 0, (double) (CELLSIZE / 2), (double) (-CELLSIZE / 2), (double) (CELLSIZE / 4));

        double col = coordinate.to2DCoordinate().x();
        double row = coordinate.to2DCoordinate().y();
        setFill(color);
        setStroke(Color.BLACK);
        setStrokeWidth(0.5);
        setStrokeType(StrokeType.CENTERED);

        setLayoutX((col * CELLSIZE) / 2);
        setLayoutY(row * (CELLSIZE) - ((double) CELLSIZE / 4 * row));

        this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> appControllerInstance.getCurrentMode().exited(event, this));

        this.label = new Label(coordinate.to2DCoordinate().x() + "," + coordinate.to2DCoordinate().y());
        this.label.layoutXProperty().bind(
                this.layoutXProperty().subtract(this.label.widthProperty().divide(2))
        );
        this.label.layoutYProperty().bind(
                this.layoutYProperty().subtract(this.label.heightProperty().divide(2))
        );
        this.appControllerInstance.gameController.pane.getChildren().add(this);

        this.appControllerInstance.gameController.pane.getChildren().add(this.label);
        this.label.toFront();
        this.label.setTextAlignment(TextAlignment.CENTER);

        visualIndicator.setCenterX(getLayoutX());
        visualIndicator.setCenterY(getLayoutY());
        this.appControllerInstance.gameController.pane.getChildren().add(visualIndicator);
        this.visualIndicator.setFill(Color.TRANSPARENT);
        this.visualIndicator.setStrokeWidth(2f);
        this.visualIndicator.setStroke(Color.DARKORANGE);
        this.visualIndicator.toFront();
        if (shape != null) {
            shape.setCenterX(getLayoutX());
            shape.setCenterY(getLayoutY());
            this.visualIndicator.setFill(Color.BLACK);
            this.appControllerInstance.gameController.pane.getChildren().add(shape);
            bindEventsToNode(shape);
        }
        isIndicatorVisible.subscribe((value) -> visualIndicator.setVisible(value));
        bindEventsToNode(this, visualIndicator, label);
    }

    public void toggleIndicator() {
        this.isIndicatorVisible.set(!this.isIndicatorVisible.get());
    }

    private void bindEventsToNode(Node... n) {
        for (Node node : n) {
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> appControllerInstance.getCurrentMode().handleClick(event, this));
            node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> appControllerInstance.getCurrentMode().entered(event, this));
            node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> appControllerInstance.getCurrentMode().exited(event, this));
        }
    }

    public void setIsIndicatorVisible(boolean isIndicatorVisible) {
        this.isIndicatorVisible.set(isIndicatorVisible);
    }

    public void setVisibleAndChilds(Boolean newVal) {

    }
}
