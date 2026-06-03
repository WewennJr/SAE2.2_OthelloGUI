package but.info.sae2_12.model;


import javafx.scene.paint.Color;

public enum Team {
    BLACK(Color.BLACK), WHITE(Color.WHITE);
    private final Color color;

    private Team(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Team other() {
        return (this == BLACK) ? WHITE : BLACK;
    }

}
