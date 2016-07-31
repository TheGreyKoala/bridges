package de.feu.ps.bridges.gui.listeners;

import org.junit.Before;
import org.junit.Test;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_CHANGED;
import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_STATUS_CHANGED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
public class PuzzleUpdateTest {

    private boolean puzzleUpdated;

    @Before
    public void setUp() {
        puzzleUpdated = false;
    }

    @Test
    public void testHandlePuzzleChangedEvent() {
        final PuzzleUpdate puzzleUpdate = new PuzzleUpdate(() -> this.puzzleUpdated = true);
        puzzleUpdate.handleEvent(PUZZLE_CHANGED);
        assertTrue("Expected to be informed about update.", puzzleUpdated);
    }

    @Test
    public void testHandlePuzzleStatusChangedEvent() {
        final PuzzleUpdate puzzleUpdate = new PuzzleUpdate(() -> this.puzzleUpdated = true);
        puzzleUpdate.handleEvent(PUZZLE_STATUS_CHANGED);
        assertFalse("Expected not to be informed about update.", puzzleUpdated);
    }
}