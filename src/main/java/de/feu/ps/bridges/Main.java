package de.feu.ps.bridges;

import de.feu.ps.bridges.gui.controller.MainController;
import de.feu.ps.bridges.gui.model.GameState;
import de.feu.ps.bridges.gui.listeners.*;
import de.feu.ps.bridges.gui.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        registerEventListeners(gameState, mainController, bundle);

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/feu/ps/bridges/gui/MainFrame.fxml"), bundle);
        fxmlLoader.<MainController>setController(mainController);

        final Parent root = fxmlLoader.load();
        primaryStage.setTitle(bundle.getString("mainFrame.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private static void registerEventListeners(final GameState gameState, final MainController mainController, final ResourceBundle bundle) {
        EventAlert eventAlert = new EventAlert(bundle);
        PuzzleStatusAlert puzzleStatusAlert = new PuzzleStatusAlert(bundle, gameState);
        ErrorAlert errorAlert = new ErrorAlert(bundle);
        PuzzleStatusLabelUpdate puzzleStatusLabelUpdate = new PuzzleStatusLabelUpdate(bundle, mainController::setStatusBarLabel, gameState);
        AutomatedSolvingStatusUpdate automatedSolvingStatusUpdate = new AutomatedSolvingStatusUpdate(mainController::setNonAutomatedSolvingControlsDisabled);
        PuzzleRedraw puzzleRedraw = new PuzzleRedraw(gameState, mainController::setVisiblePuzzle);

        gameState.addAutomatedSolvingEventListener(automatedSolvingStatusUpdate);
        gameState.addAutomatedSolvingEventListener(puzzleStatusAlert);
        gameState.addGameStateEventListener(puzzleRedraw);
        gameState.addGameStateEventListener(eventAlert);
        gameState.addGameStateEventListener(puzzleStatusAlert);
        gameState.addPuzzleEventListener(puzzleRedraw);
        gameState.addPuzzleEventListener(puzzleStatusAlert);
        gameState.addPuzzleEventListener(puzzleStatusLabelUpdate);
        gameState.addErrorEventListener(errorAlert);
        gameState.addErrorEventListener(automatedSolvingStatusUpdate);
    }
}
