package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.model.GameState;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.ResourceBundle;
import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_STATUS_CHANGED;
import static org.junit.Assert.assertEquals;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class PuzzleStatusLabelUpdateTest {

    @DataPoints("puzzleStatus")
    public static PuzzleStatus[] status = PuzzleStatus.values();

    @Theory
    public void testHandleEvent(@FromDataPoints("puzzleStatus") final PuzzleStatus status) throws Exception {
        final ResourceBundle bundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");
        final String expectedText = bundle.getString("puzzle.status." + status.name().toLowerCase());
        final Consumer<String> consumer = (text) -> assertEquals("Unexpected label text.", expectedText, text);
        final PuzzleStatusLabelUpdate puzzleStatusLabelUpdate = new PuzzleStatusLabelUpdate(new DummyGameState(status), consumer, bundle);
        puzzleStatusLabelUpdate.handleEvent(PUZZLE_STATUS_CHANGED);
    }

    class DummyGameState extends GameState {
        private final PuzzleStatus puzzleStatus;

        DummyGameState(final PuzzleStatus puzzleStatus) {
            this.puzzleStatus = puzzleStatus;
        }

        @Override
        public PuzzleStatus getPuzzleStatus() {
            return puzzleStatus;
        }
    }
}