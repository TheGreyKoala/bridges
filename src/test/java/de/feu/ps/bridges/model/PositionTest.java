package de.feu.ps.bridges.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
public class PositionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testColumnNegative() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Parameter 'column' must not be less than 0."));
        new Position(-1, 0);
    }

    @Test
    public void testRowNegative() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Parameter 'row' must not be less than 0."));
        new Position(0, -1);
    }

    @Test
    public void testGetColumn() {
        Position position = new Position(2, 5);
        assertEquals("Unexpected column.", 2, position.getColumn());
    }

    @Test
    public void testGetRow() {
        Position position = new Position(2, 5);
        assertEquals("Unexpected row.", 5, position.getRow());
    }

    @Test
    public void testEquals() {
        Position position = new Position(2, 5);
        Position position1 = new Position(2, 5);
        assertTrue("Expected positions to be equal.", position.equals(position1));
    }
}