package de.feu.ps.bridges.model;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class DefaultPuzzleTest {

    private DefaultPuzzle puzzle;
    private Island[] islands;

    @Before
    public void setUp() throws Exception {
        puzzle = new DefaultPuzzle(13, 13);
        islands = new Island[7];
        islands[0] = puzzle.buildIsland(new Position(0, 0), 8);
        islands[1] = puzzle.buildIsland(new Position(0, 4), 8);
        islands[2] = puzzle.buildIsland(new Position(4, 0), 8);
        islands[3] = puzzle.buildIsland(new Position(4, 8), 8);
        islands[4] = puzzle.buildIsland(new Position(8, 0), 8);
        islands[5] = puzzle.buildIsland(new Position(8, 4), 8);
        islands[6] = puzzle.buildIsland(new Position(12, 0), 8);
    }

    @DataPoints("isAnyBridgeCrossingTestCases")
    public static IsAnyBridgeCrossingTestCase[] isAnyBridgeCrossingTestCases() {
        return new IsAnyBridgeCrossingTestCase[] {
            new IsAnyBridgeCrossingTestCase(0, 2, 0, 1, false),
            new IsAnyBridgeCrossingTestCase(0, 2, 4, 6, false),
            new IsAnyBridgeCrossingTestCase(0, 2, 2, 4, false),
            new IsAnyBridgeCrossingTestCase(0, 2, 0, 2, true),
            new IsAnyBridgeCrossingTestCase(0, 2, 0, 4, true),
            new IsAnyBridgeCrossingTestCase(0, 4, 2, 6, true),
            new IsAnyBridgeCrossingTestCase(1, 5, 2, 3, true)
        };
    }

    @Theory
    public void isAnyBridgeCrossing(@FromDataPoints("isAnyBridgeCrossingTestCases") IsAnyBridgeCrossingTestCase testCase) {
        puzzle.buildBridge(islands[testCase.bridgeStartIndex], islands[testCase.bridgeEndIndex], false);
        boolean anyBridgeCrossing = puzzle.isAnyBridgeCrossing(islands[testCase.newBridgeStartIndex].getPosition(), islands[testCase.newBridgeEndIndex].getPosition());
        assertEquals(testCase.expectedResult, anyBridgeCrossing);
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