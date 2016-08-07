package de.feu.ps.bridges;

import de.feu.ps.bridges.gui.controller.MainController;
import de.feu.ps.bridges.gui.listeners.alerts.ErrorAlert;
import de.feu.ps.bridges.gui.listeners.alerts.InvalidMoveAlert;
import de.feu.ps.bridges.gui.listeners.alerts.PuzzleStatusAlert;
import de.feu.ps.bridges.gui.listeners.game.AutomatedSolvingStartStopMarker;
import de.feu.ps.bridges.gui.listeners.labels.AutomatedSolvingButtonTextSetter;
import de.feu.ps.bridges.gui.listeners.labels.IslandTextUpdater;
import de.feu.ps.bridges.gui.listeners.labels.PuzzleStatusTextSetter;
import de.feu.ps.bridges.gui.listeners.game.BridgesUpdater;
import de.feu.ps.bridges.gui.listeners.game.PuzzleSetter;
import de.feu.ps.bridges.gui.model.GameState;
import de.feu.ps.bridges.gui.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class that starts the gui application.
 */
public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Creates a new instance.
     */
    public Main() {
    }

    /**
     * Main entry point of the gui application.
     * @param args arguments - not used.
     */
    public static void main(final String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
        }
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
        final ResourceBundle bundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");
        final GameState gameState = new GameState();
        final Model model = new Model(gameState);
        final MainController mainController = new MainController(model, primaryStage);

        registerEventListeners(model, gameState, mainController, bundle);

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui/MainFrame.fxml"), bundle);
        fxmlLoader.<MainController>setController(mainController);

        final Parent root = fxmlLoader.load();
        primaryStage.setTitle(bundle.getString("mainFrame.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("bridges.png")));
        primaryStage.show();
    }

    private static void registerEventListeners(final Model model, final GameState gameState, final MainController mainController, final ResourceBundle bundle) {
        final AutomatedSolvingStartStopMarker automatedSolvingStartStopMarker = new AutomatedSolvingStartStopMarker(mainController::setNonAutomatedSolvingControlsDisabled);
        gameState.addAutomatedSolvingEventListener(automatedSolvingStartStopMarker);
        gameState.addErrorEventListener(automatedSolvingStartStopMarker);

        final AutomatedSolvingButtonTextSetter automatedSolvingButtonTextSetter = new AutomatedSolvingButtonTextSetter(mainController::setAutomatedSolvingButtonText, bundle);
        gameState.addAutomatedSolvingEventListener(automatedSolvingButtonTextSetter);
        gameState.addErrorEventListener(automatedSolvingButtonTextSetter);

        final BridgesUpdater bridgesUpdater = new BridgesUpdater(mainController::getPuzzle);
        gameState.addPuzzleEventListener(bridgesUpdater);

        final ErrorAlert errorAlert = new ErrorAlert(bundle);
        gameState.addErrorEventListener(errorAlert);

        final InvalidMoveAlert invalidMoveAlert = new InvalidMoveAlert(bundle);
        gameState.addPuzzleEventListener(invalidMoveAlert);

        final IslandTextUpdater islandTextUpdater = new IslandTextUpdater(mainController::getPuzzle);
        gameState.addSettingsEventListener(islandTextUpdater);

        final PuzzleSetter puzzleSetter = new PuzzleSetter(mainController::setPuzzle, model);
        gameState.addPuzzleEventListener(puzzleSetter);
        gameState.addSettingsEventListener(puzzleSetter);

        final PuzzleStatusAlert puzzleStatusAlert = new PuzzleStatusAlert(bundle);
        gameState.addAutomatedSolvingEventListener(puzzleStatusAlert);
        gameState.addPuzzleEventListener(puzzleStatusAlert);

        final PuzzleStatusTextSetter puzzleStatusTextSetter = new PuzzleStatusTextSetter(mainController::setStatusBarText, bundle);
        gameState.addPuzzleEventListener(puzzleStatusTextSetter);
    }
}
