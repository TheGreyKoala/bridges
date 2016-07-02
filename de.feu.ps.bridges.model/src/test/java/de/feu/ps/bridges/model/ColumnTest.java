package de.feu.ps.bridges.model;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
        column.addIsland(new DefaultIsland(1, 0, 1));
    }

    @Test
    public void testAddIsland() {
        final ModifiableIsland island1 = new DefaultIsland(0, 0, 1);
        final ModifiableIsland island2 = new DefaultIsland(0, 2, 2);
        final ModifiableIsland island3 = new DefaultIsland(0, 4, 2);

        final Column column = new Column(0);
        column.addIsland(island1);
        assertFalse("Expected no north neighbour.", island1.getNorthNeighbour().isPresent());
        assertFalse("Expected no south neighbour.", island1.getSouthNeighbour().isPresent());

        column.addIsland(island3);
        assertFalse("Expected no north neighbour.", island1.getNorthNeighbour().isPresent());
        assertTrue("Expected south neighbour.", island1.getSouthNeighbour().isPresent());
        assertEquals("Expected island3 to be the south neighbour of island1.", island3, island1.getSouthNeighbour().get());

        assertTrue("Expected north neighbour.", island3.getNorthNeighbour().isPresent());
        assertEquals("Expected island1 to be the north neighbour of island3.", island1, island3.getNorthNeighbour().get());
        assertFalse("Expected no south neighbour.", island3.getSouthNeighbour().isPresent());

        column.addIsland(island2);
        assertFalse("Expected no north neighbour.", island1.getNorthNeighbour().isPresent());
        assertTrue("Expected south neighbour.", island1.getSouthNeighbour().isPresent());
        assertEquals("Expected island2 to be the south neighbour of island1.", island2, island1.getSouthNeighbour().get());

        assertTrue("Expected north neighbour.", island2.getNorthNeighbour().isPresent());
        assertEquals("Expected island1 to be the north neighbour of island2.", island1, island2.getNorthNeighbour().get());
        assertTrue("Expected south neighbour.", island2.getSouthNeighbour().isPresent());
        assertEquals("Expected island3 to be the south neighbour of island2.", island3, island2.getSouthNeighbour().get());

        assertTrue("Expected north neighbour.", island3.getNorthNeighbour().isPresent());
        assertEquals("Expected island2 to be the north neighbour of island3.", island2, island3.getNorthNeighbour().get());
        assertFalse("Expected no south neighbour.", island3.getSouthNeighbour().isPresent());
    }

    @Test
    public void testAddIslandTwice() {
        final ModifiableIsland island1 = new DefaultIsland(0, 0, 1);
        final ModifiableIsland island2 = new DefaultIsland(0, 2, 2);
        final ModifiableIsland island3 = new DefaultIsland(0, 4, 2);

        final Column column = new Column(0);
        column.addIsland(island1);
        column.addIsland(island3);
        column.addIsland(island2);
        column.addIsland(island2);

        assertFalse("Expected no north neighbour.", island1.getNorthNeighbour().isPresent());
        assertTrue("Expected south neighbour.", island1.getSouthNeighbour().isPresent());
        assertEquals("Expected island2 to be the south neighbour of island1.", island2, island1.getSouthNeighbour().get());

        assertTrue("Expected north neighbour.", island2.getNorthNeighbour().isPresent());
        assertEquals("Expected island1 to be the north neighbour of island2.", island1, island2.getNorthNeighbour().get());
        assertTrue("Expected south neighbour.", island2.getSouthNeighbour().isPresent());
        assertEquals("Expected island3 to be the south neighbour of island2.", island3, island2.getSouthNeighbour().get());

        assertTrue("Expected north neighbour.", island3.getNorthNeighbour().isPresent());
        assertEquals("Expected island2 to be the north neighbour of island3.", island2, island3.getNorthNeighbour().get());
        assertFalse("Expected no south neighbour.", island3.getSouthNeighbour().isPresent());
    }
}