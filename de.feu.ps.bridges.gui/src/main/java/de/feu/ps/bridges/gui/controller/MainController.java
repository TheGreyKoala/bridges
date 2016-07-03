package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.components.GraphicalPuzzle;
import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.gui.gamestate.GameStateEvent;
import de.feu.ps.bridges.gui.gamestate.GameStateListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Tim Gremplewski
 */
public class MainController implements Initializable, GameStateListener {

    @FXML
    private Pane mainPanel;

    @FXML
    private MenuBar menuBar;

    @FXML
    private CheckBox showRemainingBridgesCheckBox;

    @FXML
    private Button nextMoveButton;

    @FXML
    private Label statusLabel;

    private ResourceBundle bundle;
    private FileChooser fileChooser;
    private final GameState gameState;
    private final Stage stage;

    public MainController(final GameState gameState, final Stage stage) {
        this.gameState = Objects.requireNonNull(gameState, "Parameter 'gameState' must not be null.");
        this.stage = Objects.requireNonNull(stage, "Parameter 'stage' must not be null.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(bundle.getString("puzzleExtensionFilter.description"), "*.bgs"));
        gameState.addGameStateListener(this);
    }

    public void newPuzzle(ActionEvent actionEvent) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(bundle.getString("newPuzzleDialog.title"));
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);
        dialogStage.initOwner(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/feu/ps/bridges/gui/NewPuzzleDialog.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> new NewPuzzleController(gameState, dialogStage));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public void restartPuzzle(ActionEvent actionEvent) {
        gameState.restartPuzzle();
    }

    public void loadPuzzle(ActionEvent actionEvent) {
        fileChooser.setTitle(bundle.getString("loadDialog.title"));
        File sourceFile = fileChooser.showOpenDialog(mainPanel.getScene().getWindow());

        if (sourceFile != null) {
            gameState.loadPuzzle(sourceFile);
            fileChooser.setInitialDirectory(sourceFile.getParentFile());
        }
    }

    public void savePuzzle(ActionEvent actionEvent) {
        gameState.savePuzzle();
    }

    public void savePuzzleAs(ActionEvent actionEvent) {
        fileChooser.setTitle(bundle.getString("saveAsDialog.title"));
        File destinationFile = fileChooser.showSaveDialog(mainPanel.getScene().getWindow());

        if (destinationFile != null) {
            gameState.savePuzzleAs(destinationFile);
            fileChooser.setInitialDirectory(destinationFile.getParentFile());
        }
    }

    public void exit(ActionEvent actionEvent) {
        stage.close();
    }

    @Override
    public void handleGameStateEvent(GameStateEvent event) {
        switch (event.getGameStateEventType()) {
            case NEW_PUZZLE_LOADED:
            case PUZZLE_RESTARTED:
                drawPuzzle();
                break;
            case AUTOMATIC_SOLVING_STARTED:
                getNodesToLock().forEach(node -> node.setDisable(true));
                break;
            case AUTOMATIC_SOLVING_STOPPED:
                getNodesToLock().forEach(node -> node.setDisable(false));
                showInfoAfterAutomaticPuzzleSolving();
                break;
            case AUTOMATIC_SOLVING_CANCELLED_BY_USER:
                getNodesToLock().forEach(node -> node.setDisable(false));
                break;
        }
        updateStatus();
    }

    private void showInfoAfterAutomaticPuzzleSolving() {
        PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("autoSolveDialog.title"));
        switch (puzzleStatus) {
            case SOLVED:
                alert.setContentText(bundle.getString("autoSolveDialog.solved.contentText"));
                break;
            case UNSOLVED:
                // TODO: When user presses solve button again, this message will be show too
                alert.setContentText(bundle.getString("autoSolveDialog.unsolved.contentText"));
                break;
            case UNSOLVABLE:
                alert.setContentText(bundle.getString("autoSolveDialog.unsolvable.contentText"));
                break;
        }
        alert.showAndWait();
    }

    private void updateStatus() {
        PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();
        String labelKey = "";
        switch (puzzleStatus) {
            case SOLVED:
                labelKey = "puzzle.status.solved";
                break;
            case UNSOLVED:
                labelKey = "puzzle.status.unsolved";
                break;
            case UNSOLVABLE:
                labelKey = "puzzle.status.unsolvable";
                break;
        }
        statusLabel.setText(bundle.getString(labelKey));
    }

    private void drawPuzzle() {
        mainPanel.getChildren().clear();
        Node puzzleNode = GraphicalPuzzle.createPuzzle(gameState.getPuzzle(), gameState, showRemainingBridgesCheckBox.isSelected());
        mainPanel.getChildren().addAll(puzzleNode);
    }

    private List<Node> getNodesToLock() {
        return Arrays.asList(menuBar, showRemainingBridgesCheckBox, nextMoveButton);
    }

    public void nextMove(ActionEvent actionEvent) {
        boolean bridgeAdded = gameState.nextMove();
        showInfoWhenSolvedOrNoNextMove(bridgeAdded);
    }

    private void showInfoWhenSolvedOrNoNextMove(boolean bridgeAdded) {
        PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();

        if (puzzleStatus == PuzzleStatus.SOLVED || !bridgeAdded) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("autoSolveDialog.title"));

            if (puzzleStatus == PuzzleStatus.SOLVED) {
                alert.setContentText(bundle.getString("autoSolveDialog.solved.contentText"));
            } else {
                switch (puzzleStatus) {
                    case UNSOLVED:
                        alert.setContentText(bundle.getString("noNextMoveDialog.contentText"));
                        break;
                    case UNSOLVABLE:
                        alert.setContentText(bundle.getString("autoSolveDialog.unsolvable.contentText"));
                        break;
                }
            }
            alert.showAndWait();
        }
    }

    public void solve(ActionEvent actionEvent) {
        gameState.solve();
    }

    public void showRemainingBridgesClicked(ActionEvent actionEvent) {
        drawPuzzle();
    }
}
