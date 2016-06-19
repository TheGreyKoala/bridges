package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.DefaultIsland;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
public class DeserializerTest_Abb2_Loesung extends AbstractDeserializerTest {

    @Test
    public void loadPuzzle() throws Exception {
        final String filePath = getClass().getResource("bsp_abb2_loesung.bgs").toURI().getPath();
        Deserializer deserializer = new Deserializer();
        Puzzle puzzle = deserializer.loadPuzzle(filePath);

        assertEquals("Wrong amount of columns.", 7, puzzle.getColumnsCount());
        assertEquals("Wrong amount of rows.", 7, puzzle.getRowsCount());

        Set<Island> islands = puzzle.getIslands();
        assertEquals("Wrong amount of islands.", 12, islands.size());

        validateIsland(
            puzzle, 0, 0, 3,
            null,
            new DefaultIsland(4, 0, 5),
            new DefaultIsland(0, 3, 4),
            null);

        validateIsland(
            puzzle, 0, 3, 4,
            new DefaultIsland(0, 0, 3),
            new DefaultIsland(3, 3, 3),
            new DefaultIsland(0, 5, 2),
            null);

        validateIsland(
                puzzle, 0, 5, 2,
                new DefaultIsland(0, 3, 4),
                new DefaultIsland(3, 5, 1),
                null,
                null);

        validateIsland(
                puzzle, 1, 1, 2,
                null,
                new DefaultIsland(3, 1, 3),
                new DefaultIsland(1, 6, 3),
                null);

        validateIsland(
                puzzle, 1, 6, 3,
                new DefaultIsland(1, 1, 2),
                new DefaultIsland(4, 6, 5),
                null,
                null);

        validateIsland(
                puzzle, 3, 1, 3,
                null,
                null,
                new DefaultIsland(3, 3, 3),
                new DefaultIsland(1, 1, 2));

        validateIsland(
                puzzle, 3, 3, 3,
                new DefaultIsland(3, 1, 3),
                null,
                new DefaultIsland(3, 5, 1),
                new DefaultIsland(0, 3, 4));

        validateIsland(
                puzzle, 3, 5, 1,
                new DefaultIsland(3, 3, 3),
                null,
                null,
                new DefaultIsland(0, 5, 2));

        validateIsland(
                puzzle, 4, 0, 5,
                null,
                new DefaultIsland(6, 0, 4),
                new DefaultIsland(4, 6, 5),
                new DefaultIsland(0, 0, 3));

        validateIsland(
                puzzle, 4, 6, 5,
                new DefaultIsland(4, 0, 5),
                new DefaultIsland(6, 6, 3),
                null,
                new DefaultIsland(1, 6, 3));

        validateIsland(
                puzzle, 6, 0, 4,
                null,
                null,
                new DefaultIsland(6, 6, 3),
                new DefaultIsland(4, 0, 5));

        validateIsland(
                puzzle, 6, 6, 3,
                new DefaultIsland(6, 0, 4),
                null,
                null,
                new DefaultIsland(4, 6, 5));


        Set<Bridge> bridges = puzzle.getBridges();
        assertEquals("Wrong number of bridges", 12, bridges.size());

        Island index0 = new DefaultIsland(0, 0, 3);
        Island index1 = new DefaultIsland(0, 3, 4);
        Island index2 = new DefaultIsland(0, 5, 2);
        Island index3 = new DefaultIsland(1, 1, 2);
        Island index4 = new DefaultIsland(1, 6, 3);
        Island index5 = new DefaultIsland(3, 1, 3);
        Island index6 = new DefaultIsland(3, 3, 3);
        Island index7 = new DefaultIsland(3, 5, 1);
        Island index8 = new DefaultIsland(4, 0, 5);
        Island index9 = new DefaultIsland(4, 6, 5);
        Island index10 = new DefaultIsland(6, 0, 4);
        Island index11 = new DefaultIsland(6, 6, 3);

        validateBridge(bridges, index0, index1, true);
        validateBridge(bridges, index0, index8, false);
        validateBridge(bridges, index1, index2, true);
        validateBridge(bridges, index3, index4, false);
        validateBridge(bridges, index3, index5, false);
        validateBridge(bridges, index4, index9, true);
        validateBridge(bridges, index5, index6, true);
        validateBridge(bridges, index6, index7, false);
        validateBridge(bridges, index8, index9, true);
        validateBridge(bridges, index8, index10, true);
        validateBridge(bridges, index9, index11, false);
        validateBridge(bridges, index10, index11, true);
    }

    private void validateBridge(Set<Bridge> bridges, Island island1, Island island2, boolean doubleBridge) {
        Stream<Bridge> stream = bridges.stream();
        Optional<Bridge> matchingBridge = stream.filter(bridge -> bridge.getIsland1().getColumn() == island1.getColumn() && bridge.getIsland1().getRow() == island1.getRow())
                .filter(bridge -> bridge.getIsland2().getColumn() == island2.getColumn() && bridge.getIsland2().getRow() == island2.getRow())
                .findFirst();

        assertTrue("Bridge not found.", matchingBridge.isPresent());
        assertEquals("Property doubleBridge is incorrect.", doubleBridge, matchingBridge.get().isDoubleBridge());
    }
}
