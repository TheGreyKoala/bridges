package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import javafx.scene.control.Alert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
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
public class PuzzleStatusAlertTest {

    @DataPoints("puzzleStatus")
    public static PuzzleStatus[] puzzleStatus = PuzzleStatus.values();

    private List<DummyAlertWrapper> alerts;
    private ResourceBundle bundle;

    @Before
    public void setUp() throws Exception {
        alerts = new LinkedList<>();
        bundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");
    }

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
        assertEquals("Unexpected alert.", 0, alerts.size());
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
        assertEquals("Unexpected alert.", 0, alerts.size());
    }

    @Theory
    public void testNoAlertWhenPuzzleChanges(@FromDataPoints("puzzleStatus") final PuzzleStatus status) {
        final PuzzleStatusAlert puzzleStatusAlert = createPuzzleStatusAlert(status);
        puzzleStatusAlert.handleEvent(PUZZLE_CHANGED);
        assertEquals("Unexpected alert.", 0, alerts.size());
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
        return new PuzzleStatusAlert(gameState, DummyAlertWrapper::new, bundle);
    }

    private void assertSingleAlert(final Alert.AlertType alertType, final PuzzleStatus status) {
        assertEquals("Unexpected number of alerts", 1, alerts.size());
        final DummyAlertWrapper alert = alerts.get(0);
        final String title;
        final String headerText;
        final String contentText;

        if (status == SOLVED) {
            title = bundle.getString("autoSolveDialog.solved.title");
            headerText = bundle.getString("autoSolveDialog.solved.title");
            contentText = bundle.getString("autoSolveDialog.solved.contentText");
        } else if (status == UNSOLVED) {
            title = bundle.getString("autoSolveDialog.unsolved.title");
            headerText = bundle.getString("autoSolveDialog.unsolved.title");
            contentText = bundle.getString("autoSolveDialog.unsolved.contentText");
        } else {
            title = bundle.getString("autoSolveDialog.unsolved.title");
            headerText = bundle.getString("autoSolveDialog.unsolved.title");
            contentText = bundle.getString("autoSolveDialog.unsolvable.contentText");
        }

        assertEquals("Unexpected alert type.", alertType, alert.getAlertType());
        assertEquals("Unexpected title.", title, alert.getTitle());
        assertEquals("Unexpected header text.", headerText, alert.getHeaderText());
        assertEquals("Unexpected content text.", contentText, alert.getContentText());
    }

    private class DummyAlertWrapper implements AlertWrapper {
        private final Alert.AlertType alertType;
        private String title;
        private String headerText;
        private String contentText;

        DummyAlertWrapper(final Alert.AlertType alertType) {
            this.alertType = alertType;
            alerts.add(this);
        }

        @Override
        public Alert.AlertType getAlertType() {
            return alertType;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setTitle(final String title) {
            this.title = title;
        }

        @Override
        public String getHeaderText() {
            return headerText;
        }

        @Override
        public void setHeaderText(final String headerText) {
            this.headerText = headerText;
        }

        @Override
        public String getContentText() {
            return contentText;
        }

        @Override
        public void setContentText(final String contentText) {
            this.contentText = contentText;
        }

        @Override
        public void showAndWait() {
        }
    }
}