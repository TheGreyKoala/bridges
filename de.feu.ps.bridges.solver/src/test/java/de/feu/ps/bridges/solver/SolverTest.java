package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.serialization.Deserializer;
import de.feu.ps.bridges.serialization.Serializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
@RunWith(Parameterized.class)
public class SolverTest {

    private static final Logger LOGGER = Logger.getLogger(SolverTest.class.getName());

    private final String puzzleFile;
    private final String solutionFile;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<String[]> testCases() {
        return Arrays.asList(new String[][] {
            { "bsp_5x5.bgs", "bsp_5x5.sol.bgs" },
            { "bsp_6x6.bgs", "bsp_6x6.sol.bgs" },
            { "bsp_8x8.bgs", "bsp_8x8.sol.bgs" },
            { "bsp_14x14.bgs", "bsp_14x14.sol.bgs" },
            { "bsp_25x25.bgs", "bsp_25x25.sol.bgs" },
            { "bsp_abb2.bgs", "bsp_abb2.sol.bgs" },
            { "bsp_abb22.bgs", "bsp_abb22.sol.bgs" },
            { "test_isolation_1.bgs", "test_isolation_1.sol.bgs" },
            { "test_isolation_2.bgs", "test_isolation_2.sol.bgs" },
            { "test_isolation_3.bgs", "test_isolation_3.sol.bgs" },
            { "test_isolation_4.bgs", "test_isolation_4.sol.bgs" },
            { "test_isolation_5.bgs", "test_isolation_5.sol.bgs" },
        });
    }

    public SolverTest(final String puzzleFile, final String solutionFile) {
        LOGGER.log(Level.INFO, "puzzleFile=" + puzzleFile + ", solutionFile=" + solutionFile);
        this.puzzleFile = puzzleFile;
        this.solutionFile = solutionFile;
    }

    @Test
    public void solve() throws Exception {
        final Deserializer deserializer = new Deserializer();

        final String puzzlePath = getClass().getResource(puzzleFile).toURI().getPath();
        final Puzzle puzzle = deserializer.loadPuzzle(puzzlePath);

        final Solver solver = new Solver();
        solver.solve(puzzle);

        final String solutionPath = getClass().getResource(solutionFile).toURI().getPath();
        final Puzzle solution = deserializer.loadPuzzle(solutionPath);

        final Set<Bridge> expectedBridges = solution.getBridges();
        final Set<Bridge> actualBridges = puzzle.getBridges();

        Serializer s = new Serializer();
        s.storePuzzle(puzzle, "/home/tim/solutions/" + puzzleFile);

        assertEquals("Unexpected number of bridges", expectedBridges.size(), actualBridges.size());

        expectedBridges.forEach(expectedBridge -> {
            boolean match = actualBridges.stream().anyMatch(actualBridge -> {
                Position expectedStart = expectedBridge.getIsland1().getPosition();
                Position expectedEnd = expectedBridge.getIsland2().getPosition();

                Position actualStart = actualBridge.getIsland1().getPosition();
                Position actualEnd = actualBridge.getIsland2().getPosition();

                boolean connectSamePositions =
                        expectedStart.equals(actualStart) && expectedEnd.equals(actualEnd)
                                || expectedStart.equals(actualEnd) && expectedEnd.equals(actualStart);

                return connectSamePositions && expectedBridge.isDoubleBridge() == actualBridge.isDoubleBridge();
            });

            assertTrue("Expected bridge not found: " + expectedBridge, match);
        });

        LOGGER.log(Level.INFO, "\n");
    }
}