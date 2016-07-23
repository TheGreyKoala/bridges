package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class IsAnyBridgeCrossingTest {

    private Puzzle puzzle;
    private Island[] islands;
    private PuzzleAnalyser analyser;

    @Before
    public void setUp() throws Exception {
        PuzzleBuilder builder = PuzzleBuilder.createBuilder(13, 13, 7);
        puzzle = builder.getResult();
        analyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzle);
        islands = new Island[7];
        islands[0] = builder.addIsland(new Position(0, 0), 8);
        islands[1] = builder.addIsland(new Position(0, 4), 8);
        islands[2] = builder.addIsland(new Position(4, 0), 8);
        islands[3] = builder.addIsland(new Position(4, 8), 8);
        islands[4] = builder.addIsland(new Position(8, 0), 8);
        islands[5] = builder.addIsland(new Position(8, 4), 8);
        islands[6] = builder.addIsland(new Position(12, 0), 8);
    }

    @DataPoints("isAnyBridgeCrossingTestCases")
    public static IsAnyBridgeCrossingTestCase[] isAnyBridgeCrossingTestCases() {
        return new IsAnyBridgeCrossingTestCase[] {
            new IsAnyBridgeCrossingTestCase(0, 2, 0, 1, false),
            new IsAnyBridgeCrossingTestCase(0, 2, 4, 6, false),
            new IsAnyBridgeCrossingTestCase(0, 2, 2, 4, false),
            new IsAnyBridgeCrossingTestCase(0, 2, 0, 2, false),
            // These state should not be possible
            // new IsAnyBridgeCrossingTestCase(0, 2, 0, 4, true),
            // new IsAnyBridgeCrossingTestCase(0, 4, 2, 6, true),
            new IsAnyBridgeCrossingTestCase(1, 5, 2, 3, true)
        };
    }

    @Theory
    public void isAnyBridgeCrossing(@FromDataPoints("isAnyBridgeCrossingTestCases") IsAnyBridgeCrossingTestCase testCase) {
        Island island1 = islands[testCase.bridgeStartIndex];
        Island island2 = islands[testCase.bridgeEndIndex];
        puzzle.buildBridge(island1, island2, false);
        boolean anyBridgeCrossing = analyser.isAnyBridgeCrossing(islands[testCase.newBridgeStartIndex].getPosition(), islands[testCase.newBridgeEndIndex].getPosition());
        assertEquals("Unexpected result.", testCase.expectedResult, anyBridgeCrossing);
    }

    @Test
    public void testIsAnyBridgeCrossingPreventsTripleBridge() {
        puzzle.buildBridge(islands[0], islands[2], true);
        boolean anyBridgeCrossing = analyser.isAnyBridgeCrossing(islands[0].getPosition(), islands[2].getPosition());
        assertTrue("Expected a crossing bridge.", anyBridgeCrossing);
    }

    private static class IsAnyBridgeCrossingTestCase {
        private int bridgeStartIndex;
        private int bridgeEndIndex;
        private int newBridgeStartIndex;
        private int newBridgeEndIndex;
        private boolean expectedResult;

        public IsAnyBridgeCrossingTestCase(int bridgeStartIndex, int bridgeEndIndex, int newBridgeStartIndex, int newBridgeEndIndex, boolean expectedResult) {
            this.bridgeStartIndex = bridgeStartIndex;
            this.bridgeEndIndex = bridgeEndIndex;
            this.newBridgeStartIndex = newBridgeStartIndex;
            this.newBridgeEndIndex = newBridgeEndIndex;
            this.expectedResult = expectedResult;
        }
    }
}