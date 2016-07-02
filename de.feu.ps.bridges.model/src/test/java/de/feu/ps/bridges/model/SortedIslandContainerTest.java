package de.feu.ps.bridges.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Comparator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
public class SortedIslandContainerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testComparatorNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Parameter 'comparator' must not be null.");

        new DummySortedIslandContainer(null);
    }

    @Test
    public void testAddNullIsland() throws Exception {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Parameter 'island' must not be null.");

        SortedIslandContainer container = new DummySortedIslandContainer((o1, o2) -> o1.getColumnIndex() - o2.getColumnIndex());
        container.addIsland(null);
    }

    @Test
    public void getIslands() throws Exception {
        SortedIslandContainer container = new DummySortedIslandContainer((o1, o2) -> o1.getColumnIndex() - o2.getColumnIndex());
        ModifiableIsland island = new DefaultIsland(0, 0, 1);
        container.addIsland(island);

        Set<Island> islands = container.getIslands();
        assertNotNull("Islands is null.", islands);
        assertEquals("Expected one island.", 1, islands.size());
        assertTrue("Expected island not found", islands.contains(island));
    }

    private class DummySortedIslandContainer extends SortedIslandContainer {

        DummySortedIslandContainer(Comparator<Island> comparator) {
            super(comparator);
        }

        @Override
        protected void linkToPrecedingIsland(ModifiableIsland island, ModifiableIsland precedingIsland) {

        }

        @Override
        protected void linkToFollowingIsland(ModifiableIsland island, ModifiableIsland followingIsland) {

        }
    }
}