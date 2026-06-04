package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.AI.MiniMax;
import but.info.sae2_12.AI.MinimaxAI;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.actions.Action;
import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.actions.RemoveLine;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.ui.AppControllerBase;
import but.info.sae2_12.ui.interaction.PlayMode;
import coordinates.Coordinate;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class IAController extends AppControllerBase implements Initializable {

    @FXML private ProgressBar evaluationBar;

    @FXML private TextField winScoreField;
    @FXML private TextField ringRemovedWeightField;
    @FXML private TextField pawnWeightField;
    @FXML private TextField nearLineWeightField;

    private final MiniMax miniMax = new MiniMax();
    private final MinimaxAI minimaxAI = new MinimaxAI(Team.BLACK, 3);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        winScoreField.setText("100000.0");
        ringRemovedWeightField.setText("1000.0");
        pawnWeightField.setText("10.0");
        nearLineWeightField.setText("50.0");

        Platform.runLater(() -> winScoreField.getScene().getRoot().requestFocus());
    }

    @FXML
    public void onWinScoreChanged() {
        try {
            minimaxAI.setWinScore(Double.parseDouble(winScoreField.getText()));
        } catch (NumberFormatException e) {
            winScoreField.setText("100000.0");
        }
        winScoreField.getScene().getRoot().requestFocus();
    }

    @FXML
    public void onRingRemovedWeightChanged() {
        try {
            minimaxAI.setRingRemovedWeight(Double.parseDouble(ringRemovedWeightField.getText()));
        } catch (NumberFormatException e) {
            ringRemovedWeightField.setText("1000.0");
        }
        ringRemovedWeightField.getScene().getRoot().requestFocus();
    }

    @FXML
    public void onPawnWeightChanged() {
        try {
            minimaxAI.setPawnWeight(Double.parseDouble(pawnWeightField.getText()));
        } catch (NumberFormatException e) {
            pawnWeightField.setText("10.0");
        }
        pawnWeightField.getScene().getRoot().requestFocus();
    }

    @FXML
    public void onNearLineWeightChanged() {
        try {
            minimaxAI.setNearLineWeight(Double.parseDouble(nearLineWeightField.getText()));
        } catch (NumberFormatException e) {
            nearLineWeightField.setText("50.0");
        }
        nearLineWeightField.getScene().getRoot().requestFocus();
    }

    public void updateEvaluationBar(IState state) {
        double score = miniMax.evaluate(state, Team.BLACK);
        double ratio = (score + 5_000.0) / 10_000.0;
        ratio = Math.max(0.0, Math.min(1.0, ratio));
        evaluationBar.setProgress(ratio);
        System.out.println("ProgressBar : " + ratio);
    }

    @FXML
    public void onBestMoveClicked() {
        IState state = getController().state.getValue();
        MinimaxAI ai = new MinimaxAI(state.turn(), 3);
        Action bestMove = ai.chooseMove(state);

        if (bestMove == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Meilleur coup");
            alert.setHeaderText("Aucun coup disponible");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Meilleur coup");
        alert.setHeaderText("L'IA recommande :");
        alert.setContentText(bestMove.toString());
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Jouer ce coup");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (bestMove instanceof Move move) {
                    ((SimpleObjectProperty<IState>) getController().state).set(state.move(move));
                } else if (bestMove instanceof RemoveLine removeLine) {
                    ((SimpleObjectProperty<IState>) getController().state).set(state.removeLine(removeLine));
                }
            }
        });
    }

    public void playAI(AppController controller) {
        IState state = controller.state.getValue();
        MinimaxAI ai = new MinimaxAI(state.turn(), 3);
        Action bestMove = ai.chooseMove(state);

        if (bestMove == null) {
            return;
        }

        if (bestMove instanceof Move move) {
            ((SimpleObjectProperty<IState>) controller.state).set(state.move(move));
        } else if (bestMove instanceof RemoveLine removeLine) {
            ((SimpleObjectProperty<IState>) controller.state).set(state.removeLine(removeLine));
        }
    }
}
