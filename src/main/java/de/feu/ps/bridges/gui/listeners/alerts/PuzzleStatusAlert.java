package de.feu.ps.bridges.gui.listeners.alerts;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.listeners.AutomatedSolvingEventListener;
import de.feu.ps.bridges.gui.listeners.PuzzleEventListener;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;
import java.util.function.Function;

import static de.feu.ps.bridges.analyser.PuzzleStatus.UNSOLVED;
import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.FINISHED;
import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.NO_NEXT_MOVE;
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

    private boolean automatedSolvingRunning;
    private PuzzleStatus currentStatus;

    /**
     * Create a new instance.
     * @param resourceBundle {@link ResourceBundle} that will be used to localize the dialog.
     */
    public PuzzleStatusAlert(final ResourceBundle resourceBundle) {
        this(DefaultAlertWrapper::new, resourceBundle);
    }

    /**
     * This constructor is needed for test purposes only.
     * In tests we can not show an alert, so during tests we need to inject a dummy alert,
     * that does not work on a real alert.
     * @param alertWrapperFactory {@link Function} that creates a new {@link AlertWrapper}.
     * @param resourceBundle {@link ResourceBundle} that will be used to localize the dialog.
     */
    PuzzleStatusAlert(final Function<Alert.AlertType, AlertWrapper> alertWrapperFactory, final ResourceBundle resourceBundle) {
        this.alertWrapperFactory = alertWrapperFactory;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void handleEvent(final AutomatedSolvingEvent event) {
        automatedSolvingRunning = event == STARTED;
        if (event == FINISHED || event == NO_NEXT_MOVE) {
            showPuzzleStatusAlert();
        }
    }

    @Override
    public void handleEvent(final PuzzleEvent event, final Object eventParameter) {
        if (event == PUZZLE_STATUS_CHANGED) {
            currentStatus = (PuzzleStatus) eventParameter;
            if (!automatedSolvingRunning && currentStatus != UNSOLVED) {
                showPuzzleStatusAlert();
            }
        }
    }

    private void showPuzzleStatusAlert() {
        final AlertWrapper alert = alertWrapperFactory.apply(INFORMATION);
        switch (currentStatus) {
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
