package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.PuzzleBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Tim Gremplewski
 */
public class DefaultAnalyserTest {

    private PuzzleBuilder puzzleBuilder;

    @Before
    public void setUp() throws Exception {
        puzzleBuilder = PuzzleBuilder.createBuilder(5, 5, 9);
        puzzleBuilder.addIsland(new Position(0, 0), 3);
        puzzleBuilder.addIsland(new Position(0, 2), 4);
        puzzleBuilder.addIsland(new Position(0, 4), 2);
        puzzleBuilder.addIsland(new Position(2, 0), 3);
        puzzleBuilder.addIsland(new Position(2, 3), 2);
        puzzleBuilder.addIsland(new Position(3, 2), 1);
        puzzleBuilder.addIsland(new Position(3, 4), 1);
        puzzleBuilder.addIsland(new Position(4, 0), 3);
        puzzleBuilder.addIsland(new Position(4, 3), 3);
    }

    @Test
    public void getStatusUnsolved() {
        puzzleBuilder.addBridge(0, 1, true);
        puzzleBuilder.addBridge(1, 2, false);
        puzzleBuilder.addBridge(2, 6, false);
        puzzleBuilder.addBridge(7, 8, false);

        Analyser analyser = DefaultAnalyser.createAnalyserFor(puzzleBuilder.getResult());
        assertEquals("Unexpected status.", PuzzleStatus.UNSOLVED, analyser.getStatus());
    }

    @Test
    public void getStatusSolved() {
        puzzleBuilder.addBridge(0, 1, true);
        puzzleBuilder.addBridge(0, 3, false);
        puzzleBuilder.addBridge(1, 2, false);
        puzzleBuilder.addBridge(1, 5, false);
        puzzleBuilder.addBridge(2, 6, false);
        puzzleBuilder.addBridge(3, 7, true);
        puzzleBuilder.addBridge(4, 8, true);
        puzzleBuilder.addBridge(7, 8, false);

        Analyser analyser = DefaultAnalyser.createAnalyserFor(puzzleBuilder.getResult());
        assertEquals("Unexpected status.", PuzzleStatus.SOLVED, analyser.getStatus());
    }

    @Test
    public void getStatusUnsolvable() {
        puzzleBuilder.addBridge(0, 1, true);
        puzzleBuilder.addBridge(0, 3, false);
        puzzleBuilder.addBridge(1, 2, true);
        puzzleBuilder.addBridge(3, 7, true);
        puzzleBuilder.addBridge(5, 6, false);
        puzzleBuilder.addBridge(7, 8, false);

        Analyser analyser = DefaultAnalyser.createAnalyserFor(puzzleBuilder.getResult());
        assertEquals("Unexpected status.", PuzzleStatus.UNSOLVABLE, analyser.getStatus());
    }

    @Test
    public void getStatusTwoNetworks() {
        PuzzleBuilder builder = PuzzleBuilder.createBuilder(5, 5, 4);
        builder.addIsland(new Position(0, 0), 1);
        builder.addIsland(new Position(0, 4), 1);
        builder.addIsland(new Position(4, 0), 1);
        builder.addIsland(new Position(4, 4), 1);
        builder.addBridge(0, 1, false);
        builder.addBridge(2, 3, false);

        Analyser analyser = DefaultAnalyser.createAnalyserFor(builder.getResult());
        assertEquals("Unexpected status.", PuzzleStatus.UNSOLVABLE, analyser.getStatus());
    }
}