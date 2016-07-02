package de.feu.ps.bridges.model;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
public class RowTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNegativeIndex() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(CoreMatchers.is("Parameter 'index' must not be less than 0."));
        new Row(-1);
    }

    @Test
    public void testAddIslandInAnotherRow() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(CoreMatchers.is("This islands lies in another row: " + 1));

        final Row row = new Row(0);
        row.addIsland(new DefaultIsland(0, 1, 1));
    }

    @Test
    public void testAddIsland() {
        final ModifiableIsland island1 = new DefaultIsland(0, 0, 1);
        final ModifiableIsland island2 = new DefaultIsland(2, 0, 2);
        final ModifiableIsland island3 = new DefaultIsland(4, 0, 2);

        final Row row = new Row(0);
        row.addIsland(island1);
        assertFalse("Expected no west neighbour.", island1.getWestNeighbour().isPresent());
        assertFalse("Expected no east neighbour.", island1.getEastNeighbour().isPresent());

        row.addIsland(island3);
        assertFalse("Expected no west neighbour.", island1.getWestNeighbour().isPresent());
        assertTrue("Expected east neighbour.", island1.getEastNeighbour().isPresent());
        assertEquals("Expected island3 to be the east neighbour of island1.", island3, island1.getEastNeighbour().get());

        assertTrue("Expected west neighbour.", island3.getWestNeighbour().isPresent());
        assertEquals("Expected island1 to be the west neighbour of island3.", island1, island3.getWestNeighbour().get());
        assertFalse("Expected no east neighbour.", island3.getEastNeighbour().isPresent());

        row.addIsland(island2);
        assertFalse("Expected no west neighbour.", island1.getWestNeighbour().isPresent());
        assertTrue("Expected east neighbour.", island1.getEastNeighbour().isPresent());
        assertEquals("Expected island2 to be the east neighbour of island1.", island2, island1.getEastNeighbour().get());

        assertTrue("Expected west neighbour.", island2.getWestNeighbour().isPresent());
        assertEquals("Expected island1 to be the west neighbour of island2.", island1, island2.getWestNeighbour().get());
        assertTrue("Expected east neighbour.", island2.getEastNeighbour().isPresent());
        assertEquals("Expected island3 to be the east neighbour of island2.", island3, island2.getEastNeighbour().get());

        assertTrue("Expected west neighbour.", island3.getWestNeighbour().isPresent());
        assertEquals("Expected island2 to be the west neighbour of island3.", island2, island3.getWestNeighbour().get());
        assertFalse("Expected no east neighbour.", island3.getEastNeighbour().isPresent());
    }

    @Test
    public void testAddIslandTwice() {
        final ModifiableIsland island1 = new DefaultIsland(0, 0, 1);
        final ModifiableIsland island2 = new DefaultIsland(2, 0, 2);
        final ModifiableIsland island3 = new DefaultIsland(4, 0, 2);

        final Row row = new Row(0);
        row.addIsland(island1);
        row.addIsland(island3);
        row.addIsland(island2);
        row.addIsland(island2);

        assertFalse("Expected no west neighbour.", island1.getWestNeighbour().isPresent());
        assertTrue("Expected east neighbour.", island1.getEastNeighbour().isPresent());
        assertEquals("Expected island2 to be the east neighbour of island1.", island2, island1.getEastNeighbour().get());

        assertTrue("Expected west neighbour.", island2.getWestNeighbour().isPresent());
        assertEquals("Expected island1 to be the west neighbour of island2.", island1, island2.getWestNeighbour().get());
        assertTrue("Expected east neighbour.", island2.getEastNeighbour().isPresent());
        assertEquals("Expected island3 to be the east neighbour of island2.", island3, island2.getEastNeighbour().get());

        assertTrue("Expected west neighbour.", island3.getWestNeighbour().isPresent());
        assertEquals("Expected island2 to be the west neighbour of island3.", island2, island3.getWestNeighbour().get());
        assertFalse("Expected no east neighbour.", island3.getEastNeighbour().isPresent());
    }
}