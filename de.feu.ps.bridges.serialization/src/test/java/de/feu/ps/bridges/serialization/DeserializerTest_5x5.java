package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.IslandImpl;
import de.feu.ps.bridges.model.Puzzle;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author Tim Gremplewski
 */
public class DeserializerTest_5x5 extends AbstractDeserializerTest {

    @Test
    public void loadPuzzle() throws Exception {
        final String filePath = getClass().getResource("bsp_5x5.bgs").toURI().getPath();
        Deserializer deserializer = new Deserializer();
        Puzzle puzzle = deserializer.loadPuzzle(filePath);

        assertEquals("Wrong amount of columns.", 5, puzzle.getColumnsCount());
        assertEquals("Wrong amount of rows.", 5, puzzle.getRowsCount());

        Set<Island> islands = puzzle.getIslands();
        assertEquals("Wrong amount of islands.", 9, islands.size());

        validateIsland(
            puzzle, 0, 0, 3,
            null,
            new IslandImpl(2, 0, 3),
            new IslandImpl(0, 2, 4),
            null);

        validateIsland(
            puzzle, 0, 2, 4,
            new IslandImpl(0, 0, 3),
            new IslandImpl(3, 2, 1),
            new IslandImpl(0, 4, 2),
            null);

        validateIsland(
            puzzle, 0, 4, 2,
            new IslandImpl(0, 2, 4),
            new IslandImpl(3, 4, 1),
            null,
            null);

        validateIsland(
            puzzle, 2, 0, 3,
            null,
            new IslandImpl(4, 0, 3),
            new IslandImpl(2, 3, 2),
            new IslandImpl(0, 0, 3));

        validateIsland(
            puzzle, 2, 3, 2,
            new IslandImpl(2, 0, 3),
            new IslandImpl(4, 3, 3),
            null,
            null);

        validateIsland(
            puzzle, 3, 2, 1,
            null,
            null,
            new IslandImpl(3, 4, 1),
            new IslandImpl(0, 2, 4));

        validateIsland(
            puzzle, 3, 4, 1,
            new IslandImpl(3, 2, 1),
            null,
            null,
            new IslandImpl(0, 4, 2));

        validateIsland(
            puzzle, 4, 0, 3,
            null,
            null,
            new IslandImpl(4, 3, 3),
            new IslandImpl(2, 0, 3));

        validateIsland(
            puzzle, 4, 3, 3,
            new IslandImpl(4, 0, 3),
            null,
            null,
            new IslandImpl(2, 3, 2));
    }
}