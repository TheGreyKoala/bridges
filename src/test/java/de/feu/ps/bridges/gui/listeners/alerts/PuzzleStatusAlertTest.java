package de.feu.ps.bridges.gui.listeners.alerts;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import javafx.scene.control.Alert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.ResourceBundle;

import static de.feu.ps.bridges.analyser.PuzzleStatus.*;
import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.*;
import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_CHANGED;
import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_STATUS_CHANGED;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static org.junit.Assert.assertEquals;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class PuzzleStatusAlertTest extends AlertTest {

    @DataPoints("puzzleStatus")
    public static PuzzleStatus[] puzzleStatus = PuzzleStatus.values();

    @Test
    public void testHandleStatusChangedToSolved() {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert();
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED, SOLVED);
        assertSingleAlert(INFORMATION, SOLVED);
    }

    @Test
    public void testHandleStatusChangedToUnsolved() {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert();
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED, UNSOLVED);
        assertEquals("Unexpected alert.", 0, getAlerts().size());
    }

    @Test
    public void testHandleStatusChangedToUnsolvable() {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert();
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED, UNSOLVABLE);
        assertSingleAlert(INFORMATION, UNSOLVABLE);
    }

    @Theory
    public void testNoAlertDuringAutomatedSolving(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert();
        puzzleStatusAlert.handleEvent(STARTED);
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED, status);
        assertEquals("Unexpected alert.", 0, getAlerts().size());
    }

    @Theory
    public void testNoAlertWhenPuzzleChanges(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert();
        puzzleStatusAlert.handleEvent(PUZZLE_CHANGED, status);
        assertEquals("Unexpected alert.", 0, getAlerts().size());
    }

    @Theory
    public void testAlertAfterAutomatedSolvingFinished(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert();
        puzzleStatusAlert.handleEvent(STARTED);
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED, status);
        puzzleStatusAlert.handleEvent(FINISHED);
        assertSingleAlert(INFORMATION, status);
    }

    @Theory
    public void testAlertWhenNoNextMove(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert();
        puzzleStatusAlert.handleEvent(STARTED);
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED, status);
        puzzleStatusAlert.handleEvent(NO_NEXT_MOVE);
        assertSingleAlert(INFORMATION, status);
    }

    @Theory
    public void testNoAlertWhenAutomatesSolvingCancelled(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert();
        puzzleStatusAlert.handleEvent(STARTED);
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED, status);
        puzzleStatusAlert.handleEvent(CANCELLED_BY_USER);
        assertEquals("Unexpected alert.", 0, getAlerts().size());
    }

    private PuzzleStatusAlert createPuzzleStatusAlert() {
        return new PuzzleStatusAlert(DummyAlertWrapper::new, getResourceBundle());
    }

    private void assertSingleAlert(final Alert.AlertType alertType, final PuzzleStatus status) {
        final ResourceBundle resourceBundle = getResourceBundle();
        final String title;
        final String headerText;
        final String contentText;

        if (status == SOLVED) {
            title = resourceBundle.getString("autoSolveDialog.solved.title");
            headerText = resourceBundle.getString("autoSolveDialog.solved.title");
            contentText = resourceBundle.getString("autoSolveDialog.solved.contentText");
        } else if (status == UNSOLVED) {
            title = resourceBundle.getString("autoSolveDialog.unsolved.title");
            headerText = resourceBundle.getString("autoSolveDialog.unsolved.title");
            contentText = resourceBundle.getString("autoSolveDialog.unsolved.contentText");
        } else {
            title = resourceBundle.getString("autoSolveDialog.unsolved.title");
            headerText = resourceBundle.getString("autoSolveDialog.unsolved.title");
            contentText = resourceBundle.getString("autoSolveDialog.unsolvable.contentText");
        }

        assertSingleAlert(alertType, title, headerText, contentText);
    }
}