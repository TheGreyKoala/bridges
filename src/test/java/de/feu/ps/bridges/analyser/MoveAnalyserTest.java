package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.PuzzleBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tim Gremplewski
 */
public class MoveAnalyserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private MoveAnalyser analyser;
    private Island island;
    private PuzzleBuilder puzzleBuilder;

    @Before
    public void setUp() {
        puzzleBuilder = PuzzleBuilder.createBuilder(10, 10, 5);
        island = puzzleBuilder.addIsland(new Position(5, 5), 5);
        analyser = new MoveAnalyser(puzzleBuilder.getResult());
    }

    @Test
    public void testPuzzleNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'puzzle' must not be null."));
        new MoveAnalyser(null);
    }

    @Test
    public void testIsValidMoveIslandNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'island' must not be null."));
        analyser.isValidMove(null, Direction.NORTH, false);
    }

    @Test
    public void testIsValidMoveDirectionNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'direction' must not be null."));
        Direction direction = null;
        analyser.isValidMove(island, direction, false);
    }

    @Test
    public void testIsValidMoveNoNeighbourInDirection() {
        boolean validMove = analyser.isValidMove(island, Direction.NORTH, false);
        assertFalse("Expected move to be invalid", validMove);
    }

    @Test
    public void testIsValidMoveIslandNeedsNoBridge() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 1);
        assertTrue("Expected move to be valid.", analyser.isValidMove(island2, island, false));

        puzzleBuilder.addBridge(island, island2, false);
        assertFalse("Expected move to be invalid.", analyser.isValidMove(island2, island, false));
    }

    @Test
    public void testIsValidMoveIslandNeedsNoDoubleBridge() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 2);
        assertTrue("Expected move to be valid.", analyser.isValidMove(island2, island, false));

        puzzleBuilder.addBridge(island, island2, false);
        assertFalse("Expected move to be invalid.", analyser.isValidMove(island2, island, true));
    }

    @Test
    public void testIsValidMoveNeighbourNeedsNoBridge() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 1);
        assertTrue("Expected move to be valid.", analyser.isValidMove(island, island2, false));

        puzzleBuilder.addBridge(island, island2, false);
        assertFalse("Expected move to be invalid.", analyser.isValidMove(island, island2, false));
    }

    @Test
    public void testIsValidMoveNeighbourNeedsNoDoubleBridge() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 2);
        assertTrue("Expected move to be valid.", analyser.isValidMove(island, island2, false));

        puzzleBuilder.addBridge(island, island2, false);
        assertFalse("Expected move to be invalid.", analyser.isValidMove(island, island2, true));
    }

    @Test
    public void testIsValidMoveDoubleBridgeExists() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 5);
        puzzleBuilder.addBridge(island, island2, true);
        assertFalse("Expected move to be invalid.", analyser.isValidMove(island, island2, true));
    }

    @Test
    public void testIsValidMoveIntersectingBridge() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 5);
        Island island3 = puzzleBuilder.addIsland(new Position(3, 6), 5);
        Island island4 = puzzleBuilder.addIsland(new Position(9, 6), 5);

        puzzleBuilder.addBridge(island3, island4, false);
        assertFalse("Expected move to be invalid.", analyser.isValidMove(island, island2, true));
    }

    @Test
    public void testIsValidMoveSingleBridgeExists() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 5);
        puzzleBuilder.addBridge(island, island2, false);
        assertTrue("Expected move to be valid.", analyser.isValidMove(island, island2, false));
    }

    @Test
    public void testIsValidMoveNoBridgeExists() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 5);
        assertTrue("Expected move to be valid.", analyser.isValidMove(island, island2, false));
        assertTrue("Expected move to be valid.", analyser.isValidMove(island, island2, true));
    }

    @Test
    public void testGetUnfinishedIslands() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 2);
        Island island3 = puzzleBuilder.addIsland(new Position(7, 5), 1);
        Island island4 = puzzleBuilder.addIsland(new Position(8, 7), 5);

        puzzleBuilder.addBridge(island, island2, true);
        puzzleBuilder.addBridge(island, island3, false);

        Set<Island> unfinishedIslands = analyser.getUnfinishedIslands();
        assertNotNull("Unfinished islands is unexpectedly null.", unfinishedIslands);
        assertEquals("Unexpected number of unfinished islands.", 2, unfinishedIslands.size());
        assertTrue("Unexpected unfinished islands.", unfinishedIslands.containsAll(Arrays.asList(island, island4)));
    }

    @Test
    public void testGetSafeBridgeDestinations() {
        Island island2 = puzzleBuilder.addIsland(new Position(5, 8), 3);
        Island island3 = puzzleBuilder.addIsland(new Position(7, 5), 1);
        Island island4 = puzzleBuilder.addIsland(new Position(7, 8), 1);

        Set<Island> destinations = analyser.getSafeBridgeDestinations(island);
        assertEquals("Unexpected number of destinations.", 2, destinations.size());
        assertTrue("Unexpected destinations.", destinations.containsAll(Arrays.asList(island2, island3)));

        destinations = analyser.getSafeBridgeDestinations(island2);
        assertEquals("Unexpected number of destinations.", 2, destinations.size());
        assertTrue("Unexpected destinations.", destinations.containsAll(Arrays.asList(island, island4)));

        destinations = analyser.getSafeBridgeDestinations(island3);
        assertEquals("Unexpected number of destinations.", 1, destinations.size());
        assertTrue("Unexpected destinations.", destinations.contains(island));

        destinations = analyser.getSafeBridgeDestinations(island4);
        assertEquals("Unexpected number of destinations.", 1, destinations.size());
        assertTrue("Unexpected destinations.", destinations.contains(island2));
    }
}