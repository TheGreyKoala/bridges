package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.gui.components.GraphicalPuzzle;
import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.gui.gamestate.GameStateEvent;
import de.feu.ps.bridges.gui.gamestate.GameStateEventType;
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

import static de.feu.ps.bridges.gui.gamestate.GameStateEventType.INVALID_MOVE;
import static de.feu.ps.bridges.gui.gamestate.GameStateEventType.SOLVING_FAILED;

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
    private AlertHelper alertHelper;
    private StatusBarHelper statusBarHelper;

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
        alertHelper = new AlertHelper(bundle, gameState);
        statusBarHelper = new StatusBarHelper(bundle, statusLabel, gameState);

        gameState.addGameStateListener(this);
        gameState.addGameStateListener(statusBarHelper);
        gameState.addGameStateListener(alertHelper);

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

    /**
     * Invoked when the user clicked the show remaining bridges checkbox.
     * @param actionEvent the event.
     */
    public void showRemainingBridgesClicked(final ActionEvent actionEvent) {
        drawPuzzle();
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
     * Invoked when the user clicks the nex move button.
     * @param actionEvent the event.
     */
    public void nextMove(ActionEvent actionEvent) {
        if (gameState.getPuzzle().isPresent()) {
            gameState.nextMove();
        }
    }

    @Override
    public void handleGameStateEvent(GameStateEvent event) {
        GameStateEventType gameStateEventType = event.getGameStateEventType();
        if (gameStateEventType.isErrorState() || gameStateEventType == INVALID_MOVE) {
            if (gameStateEventType == SOLVING_FAILED) {
                getNodesToLock().forEach(node -> node.setDisable(false));
            }
        } else {
            switch (gameStateEventType) {
                case PUZZLE_CHANGED:
                    drawPuzzle();
                    break;
                case AUTOMATIC_SOLVING_STARTED:
                    getNodesToLock().forEach(node -> node.setDisable(true));
                    break;
                case AUTOMATIC_SOLVING_FINISHED:
                    getNodesToLock().forEach(node -> node.setDisable(false));
                    break;
                case AUTOMATIC_SOLVING_CANCELLED_BY_USER:
                    getNodesToLock().forEach(node -> node.setDisable(false));
                    break;
            }
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
}
