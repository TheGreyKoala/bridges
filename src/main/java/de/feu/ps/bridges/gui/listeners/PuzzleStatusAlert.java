package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.model.GameState;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;
import java.util.function.Function;

import static de.feu.ps.bridges.analyser.PuzzleStatus.UNSOLVED;
import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.STARTED;
import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_STATUS_CHANGED;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Listener that shows an information dialog about the status of a puzzle, when appropriate.
 * @author Tim Gremplewski
 */
public class PuzzleStatusAlert implements AutomatedSolvingEventListener, PuzzleEventListener {

    private final Function<Alert.AlertType, AlertWrapper> alertWrapperFactory;
    private final ResourceBundle resourceBundle;
    private final GameState gameState;
    private boolean automatedSolvingRunning;

    /**
     * Create a new instance that uses the given {@link GameState} to query the puzzle status.
     * @param gameState {@link GameState} to query the puzzle status.
     * @param resourceBundle {@link ResourceBundle} that will be used to localize the dialog.
     */
    public PuzzleStatusAlert(final GameState gameState, final ResourceBundle resourceBundle) {
        this(gameState, DefaultAlertWrapper::new, resourceBundle);
    }

    /**
     * This constructor is needed for test purposes only.
     * In tests we can not show an alert, so during tests we need to inject a dummy alert,
     * that does not work on a real alert.
     * @param gameState {@link GameState} to query the puzzle status.
     * @param alertWrapperFactory {@link Function} that creates a new {@link AlertWrapper}.
     * @param resourceBundle {@link ResourceBundle} that will be used to localize the dialog.
     */
    PuzzleStatusAlert(final GameState gameState, final Function<Alert.AlertType, AlertWrapper> alertWrapperFactory, final ResourceBundle resourceBundle) {
        this.gameState = gameState;
        this.alertWrapperFactory = alertWrapperFactory;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void handleEvent(final AutomatedSolvingEvent event) {
        automatedSolvingRunning = event == STARTED;
        if (event != STARTED) {
            // Show an alert showing the puzzle status when the automated solving was finished or cancelled
            showPuzzleStatusAlert();
        }
    }

    @Override
    public void handleEvent(final PuzzleEvent event) {
        if (event == PUZZLE_STATUS_CHANGED
            && !automatedSolvingRunning
            && gameState.getPuzzleStatus() != UNSOLVED) {

            showPuzzleStatusAlert();
        }
    }

    private void showPuzzleStatusAlert() {
        final AlertWrapper alert = alertWrapperFactory.apply(INFORMATION);
        final PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();
        switch (puzzleStatus) {
            case SOLVED:
                alert.setTitle(resourceBundle.getString("autoSolveDialog.solved.title"));
                alert.setHeaderText(resourceBundle.getString("autoSolveDialog.solved.title"));
                alert.setContentText(resourceBundle.getString("autoSolveDialog.solved.contentText"));
                break;
            case UNSOLVED:
                alert.setTitle(resourceBundle.getString("autoSolveDialog.unsolved.title"));
                alert.setHeaderText(resourceBundle.getString("autoSolveDialog.unsolved.title"));
                alert.setContentText(resourceBundle.getString("autoSolveDialog.unsolved.contentText"));
                break;
            case UNSOLVABLE:
                alert.setTitle(resourceBundle.getString("autoSolveDialog.unsolved.title"));
                alert.setHeaderText(resourceBundle.getString("autoSolveDialog.unsolved.title"));
                alert.setContentText(resourceBundle.getString("autoSolveDialog.unsolvable.contentText"));
                break;
        }
        alert.showAndWait();
    }
}
