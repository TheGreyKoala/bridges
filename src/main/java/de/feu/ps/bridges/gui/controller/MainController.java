package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.gui.events.GameStateEvent;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the main panel.
 * @author Tim Gremplewski
 */
public class MainController implements Initializable {

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
        gameState.broadcastEvent(GameStateEvent.SHOW_REMAINING_BRIDGES_OPTION_CHANGED, showRemainingBridgesCheckBox.isSelected());
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

    /**
     * Set the label text of the status bar.
     * @param statusBarLabel new text for the status bar.
     */
    public void setStatusBarLabel(final String statusBarLabel) {
        statusLabel.setText(statusBarLabel);
    }

    /**
     * Enables or disables all components, that should not be accessible
     * during an automated solving of the puzzle.
     * @param setDisabled if true, the components will be disabled, otherwise they will be enabled.
     */
    public void setNonAutomatedSolvingControlsDisabled(final boolean setDisabled) {
        Arrays.asList(menuBar, showRemainingBridgesCheckBox, nextMoveButton, mainPanel)
                .forEach(component -> component.setDisable(setDisabled));
    }

    /**
     * Set the puzzle component, that should be drawn.
     * @param puzzleNode Node containing all components of the puzzle and that should be displayed.
     */
    public void setVisiblePuzzle(final Optional<Node> puzzleNode) {
        mainPanel.getChildren().clear();
        if (puzzleNode.isPresent()) {
            mainPanel.getChildren().add(puzzleNode.get());
        }
    }
}
