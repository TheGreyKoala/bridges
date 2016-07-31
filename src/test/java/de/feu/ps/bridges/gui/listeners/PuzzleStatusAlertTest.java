package de.feu.ps.bridges.gui.listeners;

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
import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.FINISHED;
import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.STARTED;
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
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert(SOLVED);
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED);
        assertSingleAlert(INFORMATION, SOLVED);
    }

    @Test
    public void testHandleStatusChangedToUnsolved() {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert(UNSOLVED);
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED);
        assertEquals("Unexpected alert.", 0, getAlerts().size());
    }

    @Test
    public void testHandleStatusChangedToUnsolvable() {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert(UNSOLVABLE);
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED);
        assertSingleAlert(INFORMATION, UNSOLVABLE);
    }

    @Theory
    public void testNoAlertDuringAutomatedSolving(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert(status);
        puzzleStatusAlert.handleEvent(STARTED);
        puzzleStatusAlert.handleEvent(PUZZLE_STATUS_CHANGED);
        assertEquals("Unexpected alert.", 0, getAlerts().size());
    }

    @Theory
    public void testNoAlertWhenPuzzleChanges(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert(status);
        puzzleStatusAlert.handleEvent(PUZZLE_CHANGED);
        assertEquals("Unexpected alert.", 0, getAlerts().size());
    }

    @Theory
    public void testAlertAfterAutomatedSolvingFinished(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert(status);
        puzzleStatusAlert.handleEvent(STARTED);
        puzzleStatusAlert.handleEvent(FINISHED);
        assertSingleAlert(INFORMATION, status);
    }

    private PuzzleStatusAlert createPuzzleStatusAlert(final PuzzleStatus puzzleStatus) {
        final DummyGameState gameState = new DummyGameState(puzzleStatus);
        return new PuzzleStatusAlert(gameState, DummyAlertWrapper::new, getResourceBundle());
    }

    private void assertSingleAlert(final Alert.AlertType alertType, final PuzzleStatus status) {
        assertEquals("Unexpected number of alerts", 1, getAlerts().size());
        final DummyAlertWrapper alert = getAlerts().get(0);
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

        assertEquals("Unexpected alert type.", alertType, alert.getAlertType());
        assertEquals("Unexpected title.", title, alert.getTitle());
        assertEquals("Unexpected header text.", headerText, alert.getHeaderText());
        assertEquals("Unexpected content text.", contentText, alert.getContentText());
    }
}