package de.feu.ps.bridges.model;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class DefaultBridgeTest {

    @DataPoint("sourceIsland")
    public static Island sourceIsland = ModifiableIslandFactory.create(new Position(0, 0), 1);

    @DataPoints("validDestinationIslands")
    public static Island[] validDestinationIslands = new Island[] {
            ModifiableIslandFactory.create(new Position(2, 0), 1),
            ModifiableIslandFactory.create(new Position(0, 2), 1)
    };

    @DataPoints("booleanValues")
    public static boolean[] booleanValues = new boolean[] {
        true, false
    };

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testIsland1Null() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(CoreMatchers.is("Parameter 'island1' must not be null."));

        ModifiableBridgeFactory.createBridge(null, sourceIsland, false);
    }

    @Test
    public void testIsland2Null() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(CoreMatchers.is("Parameter 'island2' must not be null."));

        ModifiableBridgeFactory.createBridge(sourceIsland, null, false);
    }

    @Test
    public void testBridgeBetweenEqualIsland() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(CoreMatchers.is("Bridged islands must not be equal."));

        ModifiableBridgeFactory.createBridge(sourceIsland, sourceIsland, false);
    }

    @Test
    public void testUnbridgeableIslands() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(CoreMatchers.is("Bridged islands must either lie in the same row or the same column"));

        final Island island2 = ModifiableIslandFactory.create(new Position(2, 2), 1);
        ModifiableBridgeFactory.createBridge(sourceIsland, island2, false);
    }

    @Theory
    public void testGetBridgedIslands(
            @FromDataPoints("sourceIsland") final Island island1,
            @FromDataPoints("validDestinationIslands") final Island island2) {

        final Bridge bridge = ModifiableBridgeFactory.createBridge(island1, island2, false);
        final Set<Island> bridgedIslands = bridge.getBridgedIslands();

        assertNotNull("Bridged islands set is null.", bridgedIslands);
        assertEquals("Expected two islands to be bridged.", 2, bridgedIslands.size());
        assertTrue("Expected island1 to be among the bridged islands.", bridgedIslands.contains(island1));
        assertTrue("Expected island2 to be among the bridged islands.", bridgedIslands.contains(island2));
    }

    @Test
    public void testGetIslands1() {
        final Island island1 = sourceIsland;
        final Island island2 = validDestinationIslands[0];
        final Bridge bridge = ModifiableBridgeFactory.createBridge(island1, island2, false);

        final Island bridgedIsland1 = bridge.getIsland1();
        assertNotNull("First bridged island is null.", bridgedIsland1);
        assertEquals("Unexpected first bridged island.", island1, bridgedIsland1);
    }

    @Test
    public void testGetIslands2() {
        final Island island1 = sourceIsland;
        final Island island2 = validDestinationIslands[0];
        final Bridge bridge = ModifiableBridgeFactory.createBridge(island1, island2, false);

        final Island bridgedIsland2 = bridge.getIsland2();
        assertNotNull("Second bridged island is null.", bridgedIsland2);
        assertEquals("Unexpected second bridged island.", island2, bridgedIsland2);
    }

    @Theory
    public void testIsDoubleBridge(@FromDataPoints("booleanValues") final boolean doubleBridge) {
        final Island island1 = sourceIsland;
        final Island island2 = validDestinationIslands[0];
        final Bridge bridge = ModifiableBridgeFactory.createBridge(island1, island2, doubleBridge);

        assertEquals("Expected bridge to be double bridge: " + doubleBridge, doubleBridge, bridge.isDoubleBridge());
    }

    @Test
    public void testIsHorizontal() {
        final Island island1 = sourceIsland;
        final Island island2 = validDestinationIslands[0];
        final Bridge bridge = ModifiableBridgeFactory.createBridge(island1, island2, false);

        assertTrue("Expected bridge to be horizontal.", bridge.isHorizontal());
    }

    @Test
    public void testIsVertical() {
        final Island island1 = sourceIsland;
        final Island island2 = validDestinationIslands[1];
        final Bridge bridge = ModifiableBridgeFactory.createBridge(island1, island2, false);

        assertTrue("Expected bridge to be vertical.", bridge.isVertical());
    }

    @Test
    public void testSetDoubleBridge() {
        final Island island1 = sourceIsland;
        final Island island2 = validDestinationIslands[0];
        final ModifiableBridge bridge = ModifiableBridgeFactory.createBridge(island1, island2, false);

        bridge.setDoubleBridge(true);
        assertTrue("Expected bridge to be double bridge.", bridge.isDoubleBridge());

        bridge.setDoubleBridge(false);
        assertFalse("Expected bridge to be single bridge.", bridge.isDoubleBridge());
    }
}