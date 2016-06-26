package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.gui.PuzzleDrawer;
import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.gui.gamestate.GameStateEvent;
import de.feu.ps.bridges.gui.gamestate.GameStateListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Tim Gremplewski
 */
public class MainController implements Initializable, GameStateListener {

    @FXML
    private Pane mainPanel;

    private ResourceBundle bundle;
    private FileChooser fileChooser;
    private final GameState gameState;
    private final Stage stage;
    private PuzzleDrawer puzzleDrawer;

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
        puzzleDrawer = new PuzzleDrawer();
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
                mainPanel.getChildren().clear();
                puzzleDrawer.drawPuzzle(gameState.getPuzzle(), mainPanel);
                break;
        }
    }
}
