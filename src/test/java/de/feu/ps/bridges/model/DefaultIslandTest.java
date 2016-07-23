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
public class DefaultIslandTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNullPosition() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'position' must not be null."));
        ModifiableIslandFactory.create(null, 4);
    }

    @Test
    public void testRequireZeroBridges() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Parameter 'requiredBridged' must be between 1 and 8."));
        ModifiableIslandFactory.create(new Position(0, 0), 0);
    }

    @Test
    public void testRequireNineBridges() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Parameter 'requiredBridged' must be between 1 and 8."));
        ModifiableIslandFactory.create(new Position(0, 0), 9);
    }

    @Test
    public void testAddNullBridge() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'bridge' must not be null."));
        ModifiableIsland modifiableIsland = ModifiableIslandFactory.create(new Position(12, 37), 5);
        modifiableIsland.addBridge(null);
    }

    @Test
    public void testAddTooManyBridges() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(is("This island does not require any more bridges."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 1);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(12, 39), 2);
        island.setNeighbour(island2, Direction.NORTH);
        island.addBridge(ModifiableBridgeFactory.createBridge(island, island2, false));
        island.addBridge(ModifiableBridgeFactory.createBridge(island, island2, false));
    }

    @Test
    public void testAddBridgeToWrongIsland() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("The given bridge does not bridge this island."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(0, 0), 5);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(12, 39), 5);
        ModifiableIsland island3 = ModifiableIslandFactory.create(new Position(12, 39), 5);
        island.addBridge(ModifiableBridgeFactory.createBridge(island2, island3, false));
    }

    @Test
    public void testAddBridgeToNonNeighbour() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Bridged island is not a neighbour of this island."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 1);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(12, 39), 2);
        island.addBridge(ModifiableBridgeFactory.createBridge(island, island2, false));
    }

    @Test
    public void testAddBridge() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 1);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(12, 39), 2);
        island.setNeighbour(island2, Direction.NORTH);
        ModifiableBridge bridge = ModifiableBridgeFactory.createBridge(island, island2, false);
        island.addBridge(bridge);
        assertTrue("Expected bridge not found.", island.getBridges().contains(bridge));
    }

    @Test
    public void testGetBridgedNeighbours() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        island.setNeighbour(northNeighbour, Direction.NORTH);
        island.addBridge(ModifiableBridgeFactory.createBridge(island, northNeighbour, false));

        Set<Island> bridgedNeighbours = island.getBridgedNeighbours();
        assertNotNull("Bridged neighbours is unexpectedly null.", bridgedNeighbours);
        assertEquals("Unexpected number of bridged neighbours.", 1, bridgedNeighbours.size());
        assertTrue("Expected island to be bridged to north neighbour.", bridgedNeighbours.contains(northNeighbour));
    }

    @Test
    public void testIsBridgedToNeighbourWithNullDirection() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'direction' must not be null."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        island.isBridgedToNeighbour(null);
    }

    @Test
    public void testIsBridgedToNonExistingNeighbour() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        assertFalse("Expected island to not be bridged to south neighbour.", island.isBridgedToNeighbour(Direction.SOUTH));
    }

    @Test
    public void testIsBridgedToUnbridgedNeighbour() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        island.setNeighbour(northNeighbour, Direction.NORTH);
        assertFalse("Expected island to not be bridged to north neighbour.", island.isBridgedToNeighbour(Direction.NORTH));
    }

    @Test
    public void testIsBridgedToNeighbour() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        assertFalse("Expected island to not be bridged to north neighbour.", island.isBridgedToNeighbour(Direction.NORTH));

        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        island.setNeighbour(northNeighbour, Direction.NORTH);
        island.addBridge(ModifiableBridgeFactory.createBridge(island, northNeighbour, false));

        assertTrue("Expected island to be bridged to north neighbour.", island.isBridgedToNeighbour(Direction.NORTH));
    }

    @Test
    public void testGetNeighbourNullDirection() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'direction' must not be null."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        island.getNeighbour(null);
    }

    @Test
    public void getNeighbour() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        ModifiableIsland eastNeighbour = ModifiableIslandFactory.create(new Position(14, 37), 3);
        ModifiableIsland southNeighbour = ModifiableIslandFactory.create(new Position(12, 39), 3);
        ModifiableIsland westNeighbour = ModifiableIslandFactory.create(new Position(10, 37), 3);

        island.setNeighbour(northNeighbour, Direction.NORTH);
        island.setNeighbour(eastNeighbour, Direction.EAST);
        island.setNeighbour(southNeighbour, Direction.SOUTH);
        island.setNeighbour(westNeighbour, Direction.WEST);

        Optional<Island> optionalNorthNeighbour = island.getNeighbour(Direction.NORTH);
        Optional<Island> optionalEastNeighbour = island.getNeighbour(Direction.EAST);
        Optional<Island> optionalSouthNeighbour = island.getNeighbour(Direction.SOUTH);
        Optional<Island> optionalWestNeighbour = island.getNeighbour(Direction.WEST);

        assertTrue("Expected north neighbour to be present.", optionalNorthNeighbour.isPresent());
        assertTrue("Expected east neighbour to be present.", optionalEastNeighbour.isPresent());
        assertTrue("Expected south neighbour to be present.", optionalSouthNeighbour.isPresent());
        assertTrue("Expected west neighbour to be present.", optionalWestNeighbour.isPresent());

        assertEquals("Unexpected north neighbour.", northNeighbour, island.getNeighbour(Direction.NORTH).get());
        assertEquals("Unexpected east neighbour.", eastNeighbour, island.getNeighbour(Direction.EAST).get());
        assertEquals("Unexpected south neighbour.", southNeighbour, island.getNeighbour(Direction.SOUTH).get());
        assertEquals("Unexpected west neighbour.", westNeighbour, island.getNeighbour(Direction.WEST).get());
    }

    @Test
    public void testGetBridges() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        Set<Bridge> bridges = island.getBridges();
        assertNotNull("Bridges is unexpectedly null.", bridges);
        assertTrue("Unexpected number of bridges.", bridges.isEmpty());

        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        island.setNeighbour(northNeighbour, Direction.NORTH);
        ModifiableBridge bridge = ModifiableBridgeFactory.createBridge(island, northNeighbour, false);
        island.addBridge(bridge);

        bridges = island.getBridges();
        assertNotNull("Bridges is unexpectedly null.", bridges);
        assertEquals("Unexpected number of bridges.", 1, bridges.size());
        assertTrue("Expected bridge not found.", bridges.contains(bridge));
    }

    @Test
    public void testGetBridgeToIslandNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'island' must not be null."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        island.getBridgeTo(null);
    }

    @Test
    public void testGetBridgeToNonNeighbour() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(12, 39), 3);
        Optional<Bridge> optionalBridge = island.getBridgeTo(island2);
        assertFalse("Expected bridge to be not present.", optionalBridge.isPresent());
    }

    @Test
    public void testGetBridgeToUnbridgedNeighbour() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(12, 39), 3);
        island.setNeighbour(island2, Direction.NORTH);
        Optional<Bridge> optionalBridge = island.getBridgeTo(island2);
        assertFalse("Expected bridge to be not present.", optionalBridge.isPresent());
    }

    @Test
    public void testGetBridgeTo() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(12, 39), 3);
        island.setNeighbour(island2, Direction.NORTH);
        ModifiableBridge bridge = ModifiableBridgeFactory.createBridge(island, island2, false);
        island.addBridge(bridge);
        Optional<Bridge> optionalBridge = island.getBridgeTo(island2);
        assertTrue("Expected bridge to be present.", optionalBridge.isPresent());
        assertEquals("Unexpected bridge.", bridge, optionalBridge.get());
    }

    @Test
    public void testGetDistanceToNeighbourNullDirection() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'direction' must not be null."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        island.getDistanceToNeighbour(null);
    }

    @Test
    public void testGetDistanceToNonExistingNeighbour() {
        expectedException.expect(UnsupportedOperationException.class);
        expectedException.expectMessage(is("Island has no neighbour in this direction: NORTH"));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        island.getDistanceToNeighbour(Direction.NORTH);
    }

    @Test
    public void testGetDistanceToNeighbour() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 24), 3);
        ModifiableIsland eastNeighbour = ModifiableIslandFactory.create(new Position(18, 37), 3);
        ModifiableIsland southNeighbour = ModifiableIslandFactory.create(new Position(12, 39), 3);
        ModifiableIsland westNeighbour = ModifiableIslandFactory.create(new Position(2, 37), 3);

        island.setNeighbour(northNeighbour, Direction.NORTH);
        island.setNeighbour(eastNeighbour, Direction.EAST);
        island.setNeighbour(southNeighbour, Direction.SOUTH);
        island.setNeighbour(westNeighbour, Direction.WEST);

        assertEquals("Unexpected distance to north neighbour.", 13, island.getDistanceToNeighbour(Direction.NORTH));
        assertEquals("Unexpected distance to east neighbour.", 6, island.getDistanceToNeighbour(Direction.EAST));
        assertEquals("Unexpected distance to south neighbour.", 2, island.getDistanceToNeighbour(Direction.SOUTH));
        assertEquals("Unexpected distance to west neighbour.", 10, island.getDistanceToNeighbour(Direction.WEST));
    }

    @Test
    public void testGetPosition() {
        Position position = new Position(12, 37);
        ModifiableIsland modifiableIsland = ModifiableIslandFactory.create(position, 8);
        assertEquals("Unexpected position.", position, modifiableIsland.getPosition());
    }

    @Test
    public void testGetNeighbours() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 3);
        Set<Island> neighbours = island.getNeighbours();
        assertNotNull("Neighbours unexpectedly null.", neighbours);
        assertTrue("Expected no neighbours.", neighbours.isEmpty());

        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        ModifiableIsland eastNeighbour = ModifiableIslandFactory.create(new Position(14, 37), 3);
        ModifiableIsland southNeighbour = ModifiableIslandFactory.create(new Position(12, 39), 3);
        ModifiableIsland westNeighbour = ModifiableIslandFactory.create(new Position(10, 37), 3);

        island.setNeighbour(northNeighbour, Direction.NORTH);
        island.setNeighbour(eastNeighbour, Direction.EAST);
        island.setNeighbour(southNeighbour, Direction.SOUTH);
        island.setNeighbour(westNeighbour, Direction.WEST);

        neighbours = island.getNeighbours();
        assertNotNull("Neighbours unexpectedly null.", neighbours);
        assertEquals("Unexpected number of neighbours.", 4, neighbours.size());
        assertTrue("Not all neighbours found.", neighbours.containsAll(Arrays.asList(northNeighbour, eastNeighbour, southNeighbour, westNeighbour)));
    }

    @Test
    public void getRemainingBridges() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 7);
        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        ModifiableIsland eastNeighbour = ModifiableIslandFactory.create(new Position(14, 37), 3);
        island.setNeighbour(northNeighbour, Direction.NORTH);
        island.setNeighbour(eastNeighbour, Direction.EAST);

        island.addBridge(ModifiableBridgeFactory.createBridge(island, northNeighbour, true));
        island.addBridge(ModifiableBridgeFactory.createBridge(island, eastNeighbour, false));

        assertEquals("Unexpected number of remaining bridges.", 4, island.getRemainingBridges());
    }

    @Test
    public void testGetActualBridgesCount() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 7);
        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        ModifiableIsland eastNeighbour = ModifiableIslandFactory.create(new Position(14, 37), 3);
        island.setNeighbour(northNeighbour, Direction.NORTH);
        island.setNeighbour(eastNeighbour, Direction.EAST);

        island.addBridge(ModifiableBridgeFactory.createBridge(island, northNeighbour, true));
        island.addBridge(ModifiableBridgeFactory.createBridge(island, eastNeighbour, false));

        assertEquals("Unexpected number of actual bridges.", 3, island.getActualBridgesCount());
    }

    @Test
    public void testGetRequiredBridges() {
        ModifiableIsland modifiableIsland = ModifiableIslandFactory.create(new Position(12, 37), 5);
        assertEquals("Unexpected position.", 5, modifiableIsland.getRequiredBridges());
    }

    @Test
    public void testRemoveAllBridges() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 7);
        ModifiableIsland northNeighbour = ModifiableIslandFactory.create(new Position(12, 35), 3);
        ModifiableIsland eastNeighbour = ModifiableIslandFactory.create(new Position(14, 37), 3);
        island.setNeighbour(northNeighbour, Direction.NORTH);
        island.setNeighbour(eastNeighbour, Direction.EAST);

        island.addBridge(ModifiableBridgeFactory.createBridge(island, northNeighbour, true));
        island.addBridge(ModifiableBridgeFactory.createBridge(island, eastNeighbour, false));

        assertFalse("Bridges unexpectedly empty.", island.getBridges().isEmpty());

        island.removeAllBridges();
        assertTrue("Bridges unexpectedly not empty.", island.getBridges().isEmpty());
    }

    @Test
    public void testRemoveNullBridge() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'bridge' must not be null."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(12, 37), 7);
        island.removeBridge(null);
    }

    @Test
    public void testRemoveUnknownBridge() {
        ModifiableIsland island1 = ModifiableIslandFactory.create(new Position(0, 0), 5);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(0, 5), 5);
        ModifiableIsland island3 = ModifiableIslandFactory.create(new Position(5, 5), 5);

        island1.setNeighbour(island2, Direction.SOUTH);

        ModifiableBridge bridge1_2 = ModifiableBridgeFactory.createBridge(island1, island2, false);
        ModifiableBridge bridge2_3 = ModifiableBridgeFactory.createBridge(island2, island3, false);
        island1.addBridge(bridge1_2);

        island1.removeBridge(bridge2_3);
        assertEquals("Unexpected number of bridges.", 1, island1.getBridges().size());
        assertTrue("Expected bridge not found.", island1.getBridges().contains(bridge1_2));
    }

    @Test
    public void testRemoveBridge() {
        ModifiableIsland island1 = ModifiableIslandFactory.create(new Position(0, 0), 5);
        ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(0, 5), 5);
        ModifiableIsland island3 = ModifiableIslandFactory.create(new Position(5, 0), 5);

        island1.setNeighbour(island2, Direction.SOUTH);
        island1.setNeighbour(island3, Direction.EAST);

        ModifiableBridge bridge1_2 = ModifiableBridgeFactory.createBridge(island1, island2, false);
        ModifiableBridge bridge1_3 = ModifiableBridgeFactory.createBridge(island1, island3, false);
        island1.addBridge(bridge1_2);
        island1.addBridge(bridge1_3);

        assertEquals("Unexpected number of bridges.", 2, island1.getBridges().size());
        island1.removeBridge(bridge1_2);
        assertEquals("Unexpected number of bridges.", 1, island1.getBridges().size());
        assertTrue("Expected bridge not found.", island1.getBridges().contains(bridge1_3));
    }

    @Test
    public void testSetNullNeighbour() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'neighbour' must not be null."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(0, 0), 5);
        island.setNeighbour(null, Direction.NORTH);
    }

    @Test
    public void testSetNeighbourNullDirection() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'direction' must not be null."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(0, 0), 5);
        ModifiableIsland neighbour = ModifiableIslandFactory.create(new Position(0, 5), 5);
        island.setNeighbour(neighbour, null);
    }

    @Test
    public void testSetRequiredBridgesToZero() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Parameter 'requiredBridged' must be between 1 and 8."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(0, 0), 5);
        island.setRequiredBridges(0);
    }

    @Test
    public void testSetRequiredBridgesToNine() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Parameter 'requiredBridged' must be between 1 and 8."));
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(0, 0), 5);
        island.setRequiredBridges(9);
    }

    @Test
    public void testSetRequiredBridges() {
        ModifiableIsland island = ModifiableIslandFactory.create(new Position(0, 0), 5);
        island.setRequiredBridges(3);
        assertEquals("Unexpected number of required bridges.", 3, island.getRequiredBridges());
    }
}