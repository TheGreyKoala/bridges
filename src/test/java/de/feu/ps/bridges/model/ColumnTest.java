package de.feu.ps.bridges.model;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static de.feu.ps.bridges.model.Direction.NORTH;
import static de.feu.ps.bridges.model.Direction.SOUTH;
import static org.junit.Assert.*;


/**
 * @author Tim Gremplewski
 */
public class ColumnTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNegativeIndex() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(CoreMatchers.is("Parameter 'index' must not be less than 0."));
        new Column(-1);
    }

    @Test
    public void testAddIslandInAnotherColumn() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(CoreMatchers.is("This islands lies in another column: " + 1));

        final Column column = new Column(0);
        column.addIsland(ModifiableIslandFactory.create(new Position(1, 0), 1));
    }

    @Test
    public void testAddIsland() {
        final ModifiableIsland island1 = ModifiableIslandFactory.create(new Position(0, 0), 1);
        final ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(0, 2), 2);
        final ModifiableIsland island3 = ModifiableIslandFactory.create(new Position(0, 4), 2);

        final Column column = new Column(0);
        column.addIsland(island1);
        assertFalse("Expected no north neighbour.", island1.getNeighbour(NORTH).isPresent());
        assertFalse("Expected no south neighbour.", island1.getNeighbour(SOUTH).isPresent());

        column.addIsland(island3);
        assertFalse("Expected no north neighbour.", island1.getNeighbour(NORTH).isPresent());
        assertTrue("Expected south neighbour.", island1.getNeighbour(SOUTH).isPresent());
        assertEquals("Expected island3 to be the south neighbour of island1.", island3, island1.getNeighbour(SOUTH).get());

        assertTrue("Expected north neighbour.", island3.getNeighbour(NORTH).isPresent());
        assertEquals("Expected island1 to be the north neighbour of island3.", island1, island3.getNeighbour(NORTH).get());
        assertFalse("Expected no south neighbour.", island3.getNeighbour(SOUTH).isPresent());

        column.addIsland(island2);
        assertFalse("Expected no north neighbour.", island1.getNeighbour(NORTH).isPresent());
        assertTrue("Expected south neighbour.", island1.getNeighbour(SOUTH).isPresent());
        assertEquals("Expected island2 to be the south neighbour of island1.", island2, island1.getNeighbour(SOUTH).get());

        assertTrue("Expected north neighbour.", island2.getNeighbour(NORTH).isPresent());
        assertEquals("Expected island1 to be the north neighbour of island2.", island1, island2.getNeighbour(NORTH).get());
        assertTrue("Expected south neighbour.", island2.getNeighbour(SOUTH).isPresent());
        assertEquals("Expected island3 to be the south neighbour of island2.", island3, island2.getNeighbour(SOUTH).get());

        assertTrue("Expected north neighbour.", island3.getNeighbour(NORTH).isPresent());
        assertEquals("Expected island2 to be the north neighbour of island3.", island2, island3.getNeighbour(NORTH).get());
        assertFalse("Expected no south neighbour.", island3.getNeighbour(SOUTH).isPresent());
    }

    @Test
    public void testAddIslandTwice() {
        final ModifiableIsland island1 = ModifiableIslandFactory.create(new Position(0, 0), 1);
        final ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(0, 2), 2);
        final ModifiableIsland island3 = ModifiableIslandFactory.create(new Position(0, 4), 2);

        final Column column = new Column(0);
        column.addIsland(island1);
        column.addIsland(island3);
        column.addIsland(island2);
        column.addIsland(island2);

        assertFalse("Expected no north neighbour.", island1.getNeighbour(NORTH).isPresent());
        assertTrue("Expected south neighbour.", island1.getNeighbour(SOUTH).isPresent());
        assertEquals("Expected island2 to be the south neighbour of island1.", island2, island1.getNeighbour(SOUTH).get());

        assertTrue("Expected north neighbour.", island2.getNeighbour(NORTH).isPresent());
        assertEquals("Expected island1 to be the north neighbour of island2.", island1, island2.getNeighbour(NORTH).get());
        assertTrue("Expected south neighbour.", island2.getNeighbour(SOUTH).isPresent());
        assertEquals("Expected island3 to be the south neighbour of island2.", island3, island2.getNeighbour(SOUTH).get());

        assertTrue("Expected north neighbour.", island3.getNeighbour(NORTH).isPresent());
        assertEquals("Expected island2 to be the north neighbour of island3.", island2, island3.getNeighbour(NORTH).get());
        assertFalse("Expected no south neighbour.", island3.getNeighbour(SOUTH).isPresent());
    }

    @Test
    public void testGetIslandAtRow() {
        final ModifiableIsland island1 = ModifiableIslandFactory.create(new Position(0, 0), 1);
        final ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(0, 2), 2);
        final ModifiableIsland island3 = ModifiableIslandFactory.create(new Position(0, 4), 2);

        final Column column = new Column(0);
        column.addIsland(island1);
        column.addIsland(island3);
        column.addIsland(island2);

        Optional<Island> islandAtRow0 = column.getIslandAtRow(0);
        Optional<Island> islandAtRow2 = column.getIslandAtRow(2);
        Optional<Island> islandAtRow4 = column.getIslandAtRow(4);

        assertTrue("No island at row 0.", islandAtRow0.isPresent());
        assertTrue("No island at row 2.", islandAtRow2.isPresent());
        assertTrue("No island at row 3.", islandAtRow4.isPresent());

        assertEquals("Unexpected island at row 0", island1, islandAtRow0.get());
        assertEquals("Unexpected island at row 2", island2, islandAtRow2.get());
        assertEquals("Unexpected island at row 4", island3, islandAtRow4.get());
    }

    @Test
    public void testGetIslandAtEmptyRow() {
        final ModifiableIsland island1 = ModifiableIslandFactory.create(new Position(0, 0), 1);
        final ModifiableIsland island2 = ModifiableIslandFactory.create(new Position(0, 2), 2);
        final ModifiableIsland island3 = ModifiableIslandFactory.create(new Position(0, 4), 2);

        final Column column = new Column(0);
        column.addIsland(island1);
        column.addIsland(island3);
        column.addIsland(island2);

        Optional<Island> islandAtRow5 = column.getIslandAtRow(5);
        assertFalse("Expected no island at row 5.", islandAtRow5.isPresent());
    }
}