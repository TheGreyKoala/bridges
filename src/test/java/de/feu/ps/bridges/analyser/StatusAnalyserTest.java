package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.PuzzleBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Tim Gremplewski
 */
public class StatusAnalyserTest {
    private PuzzleBuilder puzzleBuilder;
    private Island[] islands = new Island[9];

    @Before
    public void setUp() throws Exception {
        puzzleBuilder = PuzzleBuilder.createBuilder(5, 5, 9);
        islands[0] = puzzleBuilder.addIsland(new Position(0, 0), 3);
        islands[1] = puzzleBuilder.addIsland(new Position(0, 2), 4);
        islands[2] = puzzleBuilder.addIsland(new Position(0, 4), 2);
        islands[3] = puzzleBuilder.addIsland(new Position(2, 0), 3);
        islands[4] = puzzleBuilder.addIsland(new Position(2, 3), 2);
        islands[5] = puzzleBuilder.addIsland(new Position(3, 2), 1);
        islands[6] = puzzleBuilder.addIsland(new Position(3, 4), 1);
        islands[7] = puzzleBuilder.addIsland(new Position(4, 0), 3);
        islands[8] = puzzleBuilder.addIsland(new Position(4, 3), 3);
    }

    @Test
    public void getStatusUnsolved() {
        puzzleBuilder.addBridge(islands[0], islands[1], true);
        puzzleBuilder.addBridge(islands[1], islands[2], false);
        puzzleBuilder.addBridge(islands[2], islands[6], false);
        puzzleBuilder.addBridge(islands[7], islands[8], false);

        PuzzleAnalyser puzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzleBuilder.getResult());
        assertEquals("Unexpected status.", PuzzleStatus.UNSOLVED, puzzleAnalyser.getStatus());
    }

    @Test
    public void getStatusSolved() {
        puzzleBuilder.addBridge(islands[0], islands[1], true);
        puzzleBuilder.addBridge(islands[0], islands[3], false);
        puzzleBuilder.addBridge(islands[1], islands[2], false);
        puzzleBuilder.addBridge(islands[1], islands[5], false);
        puzzleBuilder.addBridge(islands[2], islands[6], false);
        puzzleBuilder.addBridge(islands[3], islands[7], true);
        puzzleBuilder.addBridge(islands[4], islands[8], true);
        puzzleBuilder.addBridge(islands[7], islands[8], false);

        PuzzleAnalyser puzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzleBuilder.getResult());
        assertEquals("Unexpected status.", PuzzleStatus.SOLVED, puzzleAnalyser.getStatus());
    }

    @Test
    public void getStatusUnsolvable() {
        puzzleBuilder.addBridge(islands[0], islands[1], true);
        puzzleBuilder.addBridge(islands[0], islands[3], false);
        puzzleBuilder.addBridge(islands[1], islands[2], true);
        puzzleBuilder.addBridge(islands[3], islands[7], true);
        puzzleBuilder.addBridge(islands[5], islands[6], false);
        puzzleBuilder.addBridge(islands[7], islands[8], false);

        PuzzleAnalyser puzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzleBuilder.getResult());
        assertEquals("Unexpected status.", PuzzleStatus.UNSOLVABLE, puzzleAnalyser.getStatus());
    }

    @Test
    public void getStatusTwoNetworks() {
        PuzzleBuilder builder = PuzzleBuilder.createBuilder(5, 5, 4);
        Island[] islands = new Island[4];
        islands[0] = builder.addIsland(new Position(0, 0), 1);
        islands[1] = builder.addIsland(new Position(0, 4), 1);
        islands[2] = builder.addIsland(new Position(4, 0), 1);
        islands[3] = builder.addIsland(new Position(4, 4), 1);
        builder.addBridge(islands[0], islands[1], false);
        builder.addBridge(islands[2], islands[3], false);

        PuzzleAnalyser puzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(builder.getResult());
        assertEquals("Unexpected status.", PuzzleStatus.UNSOLVABLE, puzzleAnalyser.getStatus());
    }
}