package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.gui.components.Puzzle;
import de.feu.ps.bridges.gui.model.Model;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import static de.feu.ps.bridges.gui.events.SettingsEvent.SHOW_REMAINING_BRIDGES_CHANGED;

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

    @FXML
    private Button automatedSolvingButton;

    private final Model model;
    private final Stage stage;

    private ResourceBundle bundle;
    private FileChooser fileChooser;
    private Puzzle puzzle;

    /**
     * Creates a new instance.
     * @param model the {@link Model} to use.
     * @param stage the {@link Stage} to use.
     * @throws NullPointerException if model or stage is null.
     */
    public MainController(final Model model, final Stage stage) {
        this.model = Objects.requireNonNull(model, "Parameter 'model' must not be null.");
        this.stage = Objects.requireNonNull(stage, "Parameter 'stage' must not be null.");
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
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
        fxmlLoader.setControllerFactory(param -> new NewPuzzleController(model, dialogStage));
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
        model.restartPuzzle();
    }

    /**
     * Invoked when the user clicks the load puzzle menu item.
     * @param actionEvent the event.
     */
    public void loadPuzzle(final ActionEvent actionEvent) {
        fileChooser.setTitle(bundle.getString("loadDialog.title"));
        File sourceFile = fileChooser.showOpenDialog(mainPanel.getScene().getWindow());

        if (sourceFile != null) {
            model.loadPuzzle(sourceFile);
            fileChooser.setInitialDirectory(sourceFile.getParentFile());
        }
    }

    /**
     * Invoked when the user clicks the save puzzle menu item.
     * @param actionEvent the event.
     */
    public void savePuzzle(final ActionEvent actionEvent) {
        if (model.getGameState().isPuzzleSourceFileKnown()) {
            model.savePuzzle();
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
            model.savePuzzleAs(destinationFile);
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
        model.getGameState().broadcastEvent(SHOW_REMAINING_BRIDGES_CHANGED, showRemainingBridgesCheckBox.isSelected());
    }

    /**
     * Invoked when the user clicks the solve puzzle button.
     * @param actionEvent the event.
     */
    public void solve(final ActionEvent actionEvent) {
        model.solve();
    }

    /**
     * Invoked when the user clicks the nex move button.
     * @param actionEvent the event.
     */
    public void nextMove(ActionEvent actionEvent) {
        model.applyNextMove();
    }

    /**
     * Set the puzzle that is displayed in the panel managed by this controller.
     * @param puzzle puzzle to display.
     */
    public void setPuzzle(final Puzzle puzzle) {
        this.puzzle = puzzle;
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(puzzle);
    }

    /**
     * Get the puzzle that is currently displayed in the panel managed by this controller.
     * @return the puzzle that is currently displayed.
     */
    public Optional<Puzzle> getPuzzle() {
        return Optional.ofNullable(puzzle);
    }

    /**
     * Set the label text of the status bar.
     * @param text new text for the status bar.
     */
    public void setStatusBarText(final String text) {
        statusLabel.setText(text);
    }

    /**
     * Enables or disables all components, that should not be accessible
     * during an automated solving of the puzzle.
     * @param setDisabled if true, the components will be disabled, otherwise they will be enabled.
     */
    public void setNonAutomatedSolvingControlsDisabled(final boolean setDisabled) {
        Arrays.asList(menuBar, nextMoveButton, mainPanel)
                .forEach(component -> component.setDisable(setDisabled));
    }

    /**
     * Set the text of the start/stop automated solving button.
     * @param text text to display in the button.
     */
    public void setAutomatedSolvingButtonText(final String text) {
        automatedSolvingButton.setText(text);
    }
}
