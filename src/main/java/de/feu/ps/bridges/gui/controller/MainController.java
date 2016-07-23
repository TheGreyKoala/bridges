package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.components.GraphicalPuzzle;
import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.gui.gamestate.GameStateEvent;
import de.feu.ps.bridges.gui.gamestate.GameStateListener;
import de.feu.ps.bridges.model.Puzzle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller for the main panel.
 * @author Tim Gremplewski
 */
public class MainController implements Initializable, GameStateListener {

    @FXML
    private StackPane mainPanel;

    @FXML
    private MenuBar menuBar;

    @FXML
    private CheckBox showRemainingBridgesCheckBox;

    @FXML
    private Button nextMoveButton;

    @FXML
    private Label statusLabel;

    @FXML
    private ScrollPane scrollPane;

    private ResourceBundle bundle;
    private FileChooser fileChooser;
    private final GameState gameState;
    private final Stage stage;

    /**
     * Creates a new instance.
     * @param gameState the {@link GameState} to use.
     * @param stage the {@link Stage} to use.
     * @throws NullPointerException if gameState or stage is null.
     */
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

        // Center content in the scroll pane
        mainPanel.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        mainPanel.minHeightProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPane.getViewportBounds().getHeight(), scrollPane.viewportBoundsProperty()));
    }

    /**
     * Invoked when the user clicks the new puzzle menu item.
     * @param actionEvent the event.
     * @throws IOException thrown if the gui for the new puzzle dialog can't be loaded.
     */
    public void newPuzzle(final ActionEvent actionEvent) throws IOException {
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

    /**
     * Invoked when the user clicks the restart puzzle menu item.
     * @param actionEvent the event.
     */
    public void restartPuzzle(final ActionEvent actionEvent) {
        if (gameState.getPuzzle().isPresent()) {
            gameState.restartPuzzle();
        }
    }

    /**
     * Invoked when the user clicks the load puzzle menu item.
     * @param actionEvent the event.
     */
    public void loadPuzzle(final ActionEvent actionEvent) {
        fileChooser.setTitle(bundle.getString("loadDialog.title"));
        File sourceFile = fileChooser.showOpenDialog(mainPanel.getScene().getWindow());

        if (sourceFile != null) {
            gameState.loadPuzzle(sourceFile);
            fileChooser.setInitialDirectory(sourceFile.getParentFile());
        }
    }

    /**
     * Invoked when the user clicks the save puzzle menu item.
     * @param actionEvent the event.
     */
    public void savePuzzle(final ActionEvent actionEvent) {
        if (gameState.isPuzzleSourceFileKnown()) {
            gameState.savePuzzle();
        } else {
            savePuzzleAs(actionEvent);
        }
    }

    /**
     * Invoked when the user clicks the save puzzle as menu item.
     * @param actionEvent the event.
     */
    public void savePuzzleAs(ActionEvent actionEvent) {
        fileChooser.setTitle(bundle.getString("saveAsDialog.title"));
        File destinationFile = fileChooser.showSaveDialog(mainPanel.getScene().getWindow());

        if (destinationFile != null) {
            gameState.savePuzzleAs(destinationFile);
            fileChooser.setInitialDirectory(destinationFile.getParentFile());
        }
    }

    /**
     * Invoked when the user clicks the exit menu item.
     * @param actionEvent the event.
     */
    public void exit(final ActionEvent actionEvent) {
        stage.close();
    }

    @Override
    public void handleGameStateEvent(GameStateEvent event) {
        switch (event.getGameStateEventType()) {
            case PUZZLE_CHANGED:
                drawPuzzle();
                updateStatus();
                break;
            case AUTOMATIC_SOLVING_STARTED:
                getNodesToLock().forEach(node -> node.setDisable(true));
                break;
            case AUTOMATIC_SOLVING_FINISHED:
                getNodesToLock().forEach(node -> node.setDisable(false));
                showInfoIfPuzzleUnsolved();
                break;
            case AUTOMATIC_SOLVING_CANCELLED_BY_USER:
                getNodesToLock().forEach(node -> node.setDisable(false));
                updateStatus();
                break;
            case NO_NEXT_MOVE:
                updateStatus();
                showInfoWhenNoNextMove();
                break;
            case PUZZLE_GENERATION_FAILED:
                showError(bundle.getString("generation.failed"));
                break;
            case LOADING_PUZZLE_FAILED:
                showError(bundle.getString("loading.failed"));
                break;
            case SAVING_PUZZLE_FAILED:
                showError(bundle.getString("saving.failed"));
                break;
            case RESTARTING_PUZZLE_FAILED:
                showError(bundle.getString("restarting.failed"));
                break;
            case NEXT_MOVE_FAILED:
                showError(bundle.getString("nextMove.failed"));
                break;
            case SOLVING_FAILED:
                showError(bundle.getString("solving.failed"));
                getNodesToLock().forEach(node -> node.setDisable(false));
                break;
            case BUILD_BRIDGE_FAILED:
                showError(bundle.getString("buildBridge.failed"));
                break;
            case INVALID_MOVE:
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(bundle.getString("warning.title"));
                alert.setHeaderText(bundle.getString("invalidMoveDialog.headerText"));
                alert.setContentText(bundle.getString("invalidMoveDialog.contentText"));
                alert.showAndWait();
                break;
            case TEAR_DOWN_BRIDGE_FAILED:
                showError(bundle.getString("tearDownBridge.failed"));
                break;
        }
    }

    private void showError(final String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("error.title"));
        alert.setHeaderText(headerText);
        alert.setContentText(bundle.getString("error.content.text"));
        alert.showAndWait();
    }

    private void showInfoIfPuzzleUnsolved() {
        PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();
        if (puzzleStatus == PuzzleStatus.UNSOLVED) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("autoSolveDialog.unsolved.title"));
            alert.setHeaderText(bundle.getString("autoSolveDialog.unsolved.title"));
            alert.setContentText(bundle.getString("autoSolveDialog.unsolved.contentText"));
            alert.showAndWait();
        }
    }

    private void updateStatus() {
        PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (puzzleStatus) {
            case SOLVED:
                statusLabel.setText(bundle.getString("puzzle.status.solved"));
                alert.setTitle(bundle.getString("autoSolveDialog.solved.title"));
                alert.setHeaderText(bundle.getString("autoSolveDialog.solved.title"));
                alert.setContentText(bundle.getString("autoSolveDialog.solved.contentText"));
                alert.showAndWait();
                break;
            case UNSOLVED:
                statusLabel.setText(bundle.getString("puzzle.status.unsolved"));
                break;
            case UNSOLVABLE:
                statusLabel.setText(bundle.getString("puzzle.status.unsolvable"));
                alert.setTitle(bundle.getString("autoSolveDialog.unsolved.title"));
                alert.setHeaderText(bundle.getString("autoSolveDialog.unsolved.title"));
                alert.setContentText(bundle.getString("autoSolveDialog.unsolvable.contentText"));
                alert.showAndWait();
                break;
        }
    }

    private void drawPuzzle() {
        mainPanel.getChildren().clear();
        Optional<Puzzle> puzzle = gameState.getPuzzle();
        if (puzzle.isPresent()) {
            Node puzzleNode = GraphicalPuzzle.createPuzzle(puzzle.get(), gameState, showRemainingBridgesCheckBox.isSelected());
            mainPanel.getChildren().addAll(puzzleNode);
        }
    }

    private List<Node> getNodesToLock() {
        return Arrays.asList(menuBar, showRemainingBridgesCheckBox, nextMoveButton, mainPanel);
    }

    public void nextMove(ActionEvent actionEvent) {
        if (gameState.getPuzzle().isPresent()) {
            gameState.nextMove();
        }
    }

    private void showInfoWhenNoNextMove() {
        PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();
        if (puzzleStatus == PuzzleStatus.UNSOLVED) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("autoSolveDialog.unsolved.title"));
            alert.setHeaderText(bundle.getString("autoSolveDialog.unsolved.title"));
            alert.setContentText(bundle.getString("noNextMoveDialog.contentText"));
            alert.showAndWait();
        }
    }

    /**
     * Invoked when the user clicks the solve puzzle button.
     * @param actionEvent the event.
     */
    public void solve(final ActionEvent actionEvent) {
        if (gameState.getPuzzle().isPresent()) {
            gameState.solve();
        }
    }

    /**
     * Invoked when the user clicked the show remaining bridges checkbox.
     * @param actionEvent the event.
     */
    public void showRemainingBridgesClicked(final ActionEvent actionEvent) {
        drawPuzzle();
    }
}
