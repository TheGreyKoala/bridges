package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import static org.junit.Assert.*;

/**
 * @author Tim Gremplewski
 */
public abstract class AbstractDeserializerTest {

    protected void validateIsland(
            Puzzle puzzle,
            int column,
            int row,
            int requiredBridges,
            Island northNeighbour,
            Island eastNeighbour,
            Island southNeighbour,
            Island westNeighbour
    ) {
        Island island = puzzle.getIslandAt(column, row);
        assertNotNull("Expected island not found", island);

        assertEquals("Island at wrong column.", column, island.getColumn());
        assertEquals("Island at wrong row.", row, island.getRow());
        assertEquals("Wrong number of required islands", requiredBridges, island.getRequiredBridges());

        validateNeighbour(northNeighbour, island.getNorthNeighbour());
        validateNeighbour(eastNeighbour, island.getEastNeighbour());
        validateNeighbour(southNeighbour, island.getSouthNeighbour());
        validateNeighbour(westNeighbour, island.getWestNeighbour());
    }

    protected void validateNeighbour(Island expectedNeighbour, Island actualNeighbour) {
        if (expectedNeighbour == null) {
            assertNull("Neighbour not null.", actualNeighbour);
        } else {
            assertNotNull("Neighbour is null", actualNeighbour);
            assertEquals("Neighbour at wrong column", expectedNeighbour.getColumn(), actualNeighbour.getColumn());
            assertEquals("Neighbour at wrong row", expectedNeighbour.getRow(), actualNeighbour.getRow());
        }
    }
}
