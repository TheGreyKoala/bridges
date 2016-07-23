package de.feu.ps.bridges.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tim Gremplewski
 */
public class DefaultPuzzleTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testColumnsZero() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Parameter 'columns' must not be less than 1."));
        ModifiablePuzzleFactory.createPuzzle(0, 5);
    }

    @Test
    public void testRowsZero() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Parameter 'rows' must not be less than 1."));
        ModifiablePuzzleFactory.createPuzzle(5, 0);
    }

    @Test
    public void testBuildBridgeIsland1Null() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'island1' must not be null."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island = puzzle.buildIsland(new Position(0, 0), 5);
        puzzle.buildBridge(null, island);
    }

    @Test
    public void testBuildBridgeIsland1Unknown() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Island1 is not part of this puzzle."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island2 = puzzle.buildIsland(new Position(0, 0), 5);

        ModifiablePuzzle puzzle2 = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle2.buildIsland(new Position(0, 0), 5);

        puzzle.buildBridge(island1, island2);
    }

    @Test
    public void testBuildBridgeIsland2Unknown() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Island2 is not part of this puzzle."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle.buildIsland(new Position(0, 0), 5);

        ModifiablePuzzle puzzle2 = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island2 = puzzle2.buildIsland(new Position(0, 0), 5);

        puzzle.buildBridge(island1, island2);
    }

    @Test
    public void testBuildBridgeIsland2Null() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'island2' must not be null."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island = puzzle.buildIsland(new Position(0, 0), 5);
        puzzle.buildBridge(island, null);
    }

    @Test
    public void testBuildFirstBridge() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        Bridge bridge = puzzle.buildBridge(island1, island2);
        assertNotNull("Built bridge is null.", bridge);
        Set<Island> bridgedIslands = bridge.getBridgedIslands();
        assertEquals("Unexpected number of bridged islands.", 2, bridgedIslands.size());
        assertTrue("Bridge does not bridge expected islands.", bridgedIslands.containsAll(Arrays.asList(island1, island2)));
        assertFalse("Bridge is unexpectedly a double bridge.", bridge.isDoubleBridge());
    }

    @Test
    public void testBuildSecondBridge() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        puzzle.buildBridge(island1, island2);
        Bridge bridge = puzzle.buildBridge(island1, island2);
        assertNotNull("Built bridge is null.", bridge);
        Set<Island> bridgedIslands = bridge.getBridgedIslands();
        assertEquals("Unexpected number of bridged islands.", 2, bridgedIslands.size());
        assertTrue("Bridge does not bridge expected islands.", bridgedIslands.containsAll(Arrays.asList(island1, island2)));
        assertTrue("Bridge is unexpectedly not a double bridge.", bridge.isDoubleBridge());
    }

    @Test
    public void testBuildThirdBridge() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(is("Two bridges already exist between the given islands."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        puzzle.buildBridge(island1, island2);
        puzzle.buildBridge(island1, island2);
        puzzle.buildBridge(island1, island2);
    }

    @Test
    public void testBuildBridgeNullPosition() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'position' must not be null."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        puzzle.buildIsland(null, 5);
    }

    @Test
    public void testBuildBridgeInvalidColumn() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("The puzzle does not have this column: 11"));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        puzzle.buildIsland(new Position(11, 0), 5);
    }

    @Test
    public void testBuildBridgeInvalidRow() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("The puzzle does not have this row: 11"));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        puzzle.buildIsland(new Position(0, 11), 5);
    }

    @Test
    public void testBuildIslandAlreadyExists() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(is("An island already exists at the given position."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        puzzle.buildIsland(new Position(0, 0), 5);
        puzzle.buildIsland(new Position(0, 0), 3);
    }

    @Test
    public void testBuildIsland() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Position position = new Position(0, 0);
        Island island = puzzle.buildIsland(position, 5);
        assertNotNull("Built island is null.", island);
        assertEquals("Island is at wrong position.", position, island.getPosition());
    }

    @Test
    public void getBridges() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        Island island3 = puzzle.buildIsland(new Position(5, 0), 5);
        puzzle.buildBridge(island1, island2);
        Bridge bridge1 = puzzle.buildBridge(island1, island2);
        Bridge bridge2 = puzzle.buildBridge(island1, island3);
        Set<Bridge> bridges = puzzle.getBridges();
        assertNotNull("Bridges is unexpectedly null.", bridges);
        assertEquals("Unexpected amount of bridges.", 2, bridges.size());
        assertTrue("Expected bridge not found.", bridges.containsAll(Arrays.asList(bridge1, bridge2)));
    }

    @Test
    public void getColumnsCount() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 5);
        assertEquals("Unexpected columns count.", 10, puzzle.getColumnsCount());
    }

    @Test
    public void getIslands() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        Island island3 = puzzle.buildIsland(new Position(5, 0), 5);

        Set<Island> islands = puzzle.getIslands();
        assertNotNull("Islands is unexpectedly null.", islands);
        assertEquals("Unexpected amount of islands.", 3, islands.size());
        assertTrue("Expected island not found.", islands.containsAll(Arrays.asList(island1, island2, island3)));
    }

    @Test
    public void getRowsCount() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 5);
        assertEquals("Unexpected rows count.", 5, puzzle.getRowsCount());
    }

    @Test
    public void removeAllBridges() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        Island island3 = puzzle.buildIsland(new Position(5, 0), 5);
        puzzle.buildBridge(island1, island2);
        puzzle.buildBridge(island1, island2);
        puzzle.buildBridge(island1, island3);

        puzzle.removeAllBridges();
        Set<Bridge> bridges = puzzle.getBridges();
        assertNotNull("Bridges is unexpectedly null.", bridges);
        assertTrue("Bridges unexpectedly not empty.", bridges.isEmpty());

        assertTrue("Bridges unexpectedly not empty.", island1.getBridges().isEmpty());
        assertTrue("Bridges unexpectedly not empty.", island2.getBridges().isEmpty());
        assertTrue("Bridges unexpectedly not empty.", island3.getBridges().isEmpty());
    }

    @Test
    public void testTearDownBridgeIsland1Null() {
        // TODO: Refactor to have less duplicate code with testBuildBridge-Counterpart
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'island1' must not be null."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island = puzzle.buildIsland(new Position(0, 0), 5);
        puzzle.tearDownBridge(null, island);
    }

    @Test
    public void testTearDownBridgeIsland1Unknown() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Island1 is not part of this puzzle."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island2 = puzzle.buildIsland(new Position(0, 0), 5);

        ModifiablePuzzle puzzle2 = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle2.buildIsland(new Position(0, 0), 5);

        puzzle.tearDownBridge(island1, island2);
    }

    @Test
    public void testTearDownBridgeIsland2Unknown() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Island2 is not part of this puzzle."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island1 = puzzle.buildIsland(new Position(0, 0), 5);

        ModifiablePuzzle puzzle2 = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island2 = puzzle2.buildIsland(new Position(0, 0), 5);

        puzzle.tearDownBridge(island1, island2);
    }

    @Test
    public void testTearDownBridgeIsland2Null() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'island2' must not be null."));
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island = puzzle.buildIsland(new Position(0, 0), 5);
        puzzle.tearDownBridge(island, null);
    }

    @Test
    public void testTearDownNonExistingBridge() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        Optional<Bridge> optional = puzzle.tearDownBridge(island, island2);
        assertFalse("Expected no bridge to be torn down.", optional.isPresent());
    }

    @Test
    public void testTearDownSingleBridge() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        Island island3 = puzzle.buildIsland(new Position(5, 0), 5);

        Bridge bridge = puzzle.buildBridge(island, island2);
        Bridge bridge2 = puzzle.buildBridge(island, island3);

        Optional<Bridge> optionalBridge = puzzle.tearDownBridge(island, island2);
        assertTrue("Expected bridge to be torn down.", optionalBridge.isPresent());
        assertEquals("Unexpected torn down bridge.", bridge, optionalBridge.get());

        Set<Bridge> bridges = puzzle.getBridges();
        assertEquals("Unexpected number of bridges.", 1, bridges.size());
        assertTrue("Expected bridge not found.", bridges.contains(bridge2));
    }

    @Test
    public void testTearDownDoubleBridge() {
        ModifiablePuzzle puzzle = ModifiablePuzzleFactory.createPuzzle(10, 10);
        Island island = puzzle.buildIsland(new Position(0, 0), 5);
        Island island2 = puzzle.buildIsland(new Position(0, 5), 5);
        Island island3 = puzzle.buildIsland(new Position(5, 0), 5);

        puzzle.buildBridge(island, island2);
        Bridge bridge = puzzle.buildBridge(island, island2);
        Bridge bridge2 = puzzle.buildBridge(island, island3);

        Optional<Bridge> optionalBridge = puzzle.tearDownBridge(island, island2);
        assertTrue("Expected bridge to be torn down.", optionalBridge.isPresent());
        assertEquals("Unexpected torn down bridge.", bridge, optionalBridge.get());

        Set<Bridge> bridges = puzzle.getBridges();
        assertEquals("Unexpected number of bridges.", 2, bridges.size());
        assertTrue("Expected bridge not found.", bridges.containsAll(Arrays.asList(bridge, bridge2)));
        assertFalse("Expected single bridge.", bridge.isDoubleBridge());
    }
}