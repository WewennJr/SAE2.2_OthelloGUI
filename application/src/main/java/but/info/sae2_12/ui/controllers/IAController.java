package but.info.sae2_12.ui.controllers;

import but.info.sae2_12.AI.MiniMax;
import but.info.sae2_12.AI.MinimaxAI;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.ui.AppControllerBase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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
        } catch (NumberFormatException ignored) {
            winScoreField.setText("100000.0");
        }
        winScoreField.getScene().getRoot().requestFocus();
    }

    @FXML
    public void onRingRemovedWeightChanged() {
        try {
            minimaxAI.setRingRemovedWeight(Double.parseDouble(ringRemovedWeightField.getText()));
        } catch (NumberFormatException ignored) {
            ringRemovedWeightField.setText("1000.0");
        }
        ringRemovedWeightField.getScene().getRoot().requestFocus();
    }

    @FXML
    public void onPawnWeightChanged() {
        try {
            minimaxAI.setPawnWeight(Double.parseDouble(pawnWeightField.getText()));
        } catch (NumberFormatException ignored) {
            pawnWeightField.setText("10.0");
        }
        pawnWeightField.getScene().getRoot().requestFocus();
    }

    @FXML
    public void onNearLineWeightChanged() {
        try {
            minimaxAI.setNearLineWeight(Double.parseDouble(nearLineWeightField.getText()));
        } catch (NumberFormatException ignored) {
            nearLineWeightField.setText("50.0");
        }
        nearLineWeightField.getScene().getRoot().requestFocus();
    }

    public void updateEvaluationBar(IState state) {
        double score = miniMax.evaluate(state, Team.BLACK);
        double ratio = (score + 100_000.0) / 200_000.0;
        ratio = Math.max(0.0, Math.min(1.0, ratio));
        evaluationBar.setProgress(ratio);
    }
}