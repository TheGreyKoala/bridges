package de.feu.ps.bridges.gui.listeners.labels;

import de.feu.ps.bridges.analyser.PuzzleStatus;
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
public class PuzzleStatusTextSetterTest {

    @DataPoints("puzzleStatus")
    public static PuzzleStatus[] status = PuzzleStatus.values();

    @Theory
    public void testHandleEvent(@FromDataPoints("puzzleStatus") final PuzzleStatus status) throws Exception {
        final ResourceBundle bundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");
        final String expectedText = bundle.getString("puzzle.status." + status.name().toLowerCase());
        final Consumer<String> consumer = (text) -> assertEquals("Unexpected label text.", expectedText, text);
        final PuzzleStatusTextSetter puzzleStatusTextSetter = new PuzzleStatusTextSetter(consumer, bundle);
        puzzleStatusTextSetter.handleEvent(PUZZLE_STATUS_CHANGED, status);
    }
}