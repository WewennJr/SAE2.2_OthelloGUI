package but.info.sae2_12.ui.objects;

import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import but.info.sae2_12.ui.controllers.AppController;
import coordinates.Coordinate;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.TextAlignment;
import javafx.util.Subscription;

public class HexSquare extends Polygon {
    public static final SimpleIntegerProperty CELLSIZE = new SimpleIntegerProperty(35);
    public AppController appControllerInstance;
    SimpleBooleanProperty isIndicatorVisible = new SimpleBooleanProperty(false);
    Color color;
    public Coordinate coordinate;
    Circle shape;
    Circle visualIndicator;
    Label label;
    private final Subscription sizeSubscription;
    private Subscription indicatorSubscription;

    public HexSquare(AppController appController, Coordinate coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
        this.appControllerInstance = appController;

        setFill(color);
        setStroke(Color.BLACK);
        setStrokeWidth(0.5);
        setStrokeType(StrokeType.CENTERED);

        this.appControllerInstance.gameController.pane.getChildren().add(this);
        buildLabel();
        updateLayoutForSize(CELLSIZE.get());

        this.sizeSubscription = CELLSIZE.subscribe((val) -> {
            updateLayoutForSize(val.intValue());
        });

        bindEventsToNode(this, visualIndicator, label);
    }

    private void updateLayoutForSize(int size) {
        double col = coordinate.to2DCoordinate().x();
        double row = coordinate.to2DCoordinate().y();

        if (shape != null) {
            this.appControllerInstance.gameController.pane.getChildren().remove(shape);
        }
        if (visualIndicator != null) {
            this.appControllerInstance.gameController.pane.getChildren().remove(visualIndicator);
        }

        setLayoutX((col * size) / 2);
        setLayoutY(row * size - ((double) size / 4 * row));

        getPoints().clear();
        getPoints().addAll(
                (double) (-size / 2), (double) (-size / 4),
                (double) 0, (double) (-size / 2),
                (double) (size / 2), (double) (-size / 4),
                (double) (size / 2), (double) (size / 4),
                (double) 0, (double) (size / 2),
                (double) (-size / 2), (double) (size / 4)
        );

        buildShape(size);
        buildVisualIndicator(size);
    }

    private void buildLabel() {
        this.label = new Label(coordinate.to2DCoordinate().x() + "," + coordinate.to2DCoordinate().y());
        this.label.layoutXProperty().bind(this.layoutXProperty().subtract(this.label.widthProperty().divide(2)));
        this.label.layoutYProperty().bind(this.layoutYProperty().subtract(this.label.heightProperty().divide(2)));
        this.appControllerInstance.gameController.pane.getChildren().add(this.label);

        this.label.toFront();
        this.label.setTextAlignment(TextAlignment.CENTER);
    }

    private void buildVisualIndicator(int size) {
        this.visualIndicator = new Circle(size / 3.5, Color.RED);

        visualIndicator.setCenterX(getLayoutX());
        visualIndicator.setCenterY(getLayoutY());
        this.appControllerInstance.gameController.pane.getChildren().add(visualIndicator);
        this.visualIndicator.setFill(Color.TRANSPARENT);
        this.visualIndicator.setStrokeWidth(2f);
        this.visualIndicator.setStroke(Color.DARKORANGE);
        this.visualIndicator.toFront();

        if (indicatorSubscription != null) {
            indicatorSubscription.unsubscribe();
        }
        indicatorSubscription = isIndicatorVisible.subscribe((value) -> visualIndicator.setVisible(value));
    }

    private void buildShape(int size) {
        Token token = this.appControllerInstance.getState().board().get(coordinate);

        if (token instanceof Pawn pawn) {
            this.shape = new Circle(size / 4.0, pawn.getTeam().getColor());
        }
        if (token instanceof Ring ring) {
            this.shape = new Circle(size / 3.0, ring.getTeam().getColor());
            this.shape.setFill(Color.TRANSPARENT);
            this.shape.setStrokeWidth(3f);
            this.shape.setStroke(ring.getTeam().getColor());
        }

        if (shape != null) {
            shape.setCenterX(getLayoutX());
            shape.setCenterY(getLayoutY());
            this.appControllerInstance.gameController.pane.getChildren().add(shape);
            bindEventsToNode(shape);
        }
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
        setVisible(newVal);
        shape.setVisible(newVal);
        label.setVisible(newVal);
        visualIndicator.setVisible(newVal);

    }

    public void cleanup() {
        if (sizeSubscription != null) {
            sizeSubscription.unsubscribe();
        }
        if (indicatorSubscription != null) {
            indicatorSubscription.unsubscribe();
        }

        this.appControllerInstance.gameController.pane.getChildren().removeAll(this, label, shape, visualIndicator);
    }
}