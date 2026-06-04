package but.info.sae2_12.ui.interaction;

import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.actions.RemoveLine;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.App;
import but.info.sae2_12.model.GameMode;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.ui.objects.HexSquare;
import coordinates.Coordinate;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RemoveLineMode extends InteractionMode {
    private Set<Coordinate> currentLineSelection = null;
    private final Map<Coordinate, Paint> colorMap = new HashMap<>(); // coord -> old color

    @Override
    public void handleClick(MouseEvent ev, HexSquare square) {
        IState currentGameState = square.appControllerInstance.getState();
        List<Set<Coordinate>> lines = currentGameState.getLines();
        Set<Coordinate> relatedLine = lines.stream().filter(line -> line.contains(square.coordinate)).findFirst().orElse(null);

        if (currentLineSelection == null) {
            if (relatedLine != null) {
                currentLineSelection = relatedLine;
                for (Coordinate coordinate : relatedLine) {
                    colorMap.put(coordinate, square.appControllerInstance.hexSquares.get(coordinate).getFill());
                    square.appControllerInstance.hexSquares.get(coordinate).setFill(Color.RED);
                    currentGameState.rings().get(currentGameState.turn()).forEach(ring -> square.appControllerInstance.hexSquares.get(ring).setFill(Color.LIGHTBLUE));
                }
            }
        } else {
            square.appControllerInstance.setState(square.appControllerInstance.getState().removeLine(new RemoveLine(currentLineSelection, square.coordinate)));
            currentLineSelection = null;
            colorMap.clear();
            square.appControllerInstance.gameController.initMap();
            if(square.appControllerInstance.getState().getLines().isEmpty()) {
                square.appControllerInstance.setCurrentMode(new PlayMode());
                  // Vérifier si c'est au tour de l'IA après la suppression de la ligne
                  if (App.getGameMode() == GameMode.PLAYER_VS_AI
                          && square.appControllerInstance.getState().turn() == Team.BLACK) {
                      PauseTransition pause = new PauseTransition(Duration.millis(300));
                      pause.setOnFinished(e -> square.appControllerInstance.iaController.playAI(square.appControllerInstance));
                      pause.play();
                  }
            }
        }
    }

    @Override
    public void entered(MouseEvent ev, HexSquare square) {
        IState currentGameState = square.appControllerInstance.getState();

        if (currentLineSelection != null) {
            if (currentGameState.board().get(square.coordinate) instanceof Ring ring && ring.getTeam() == currentGameState.turn()) {
                colorMap.put(square.coordinate, square.getFill());
                square.setFill(Color.ROYALBLUE);
            }
        } else {
            List<Set<Coordinate>> lines = currentGameState.getLines();
            Set<Coordinate> relatedLine = lines.stream().filter(line -> line.contains(square.coordinate)).findFirst().orElse(null);
            if (relatedLine != null) {
                for (Coordinate coordinate : relatedLine) {
                    colorMap.put(coordinate, square.appControllerInstance.hexSquares.get(coordinate).getFill());
                    square.appControllerInstance.hexSquares.get(coordinate).setFill(Color.RED);
                }
            }
        }

    }

    @Override
    public void exited(MouseEvent ev, HexSquare square) {
        if (currentLineSelection != null) {
            if (square.appControllerInstance.hexSquares.get(square.coordinate).getFill() == Color.ROYALBLUE) {
                colorMap.put(square.coordinate, square.getFill());
                square.setFill(Color.LIGHTBLUE);
            }
        } else {
            for (Coordinate coordinate : colorMap.keySet()) {
                square.appControllerInstance.hexSquares.get(coordinate).setFill(colorMap.get(coordinate));
            }
        }
    }
}
