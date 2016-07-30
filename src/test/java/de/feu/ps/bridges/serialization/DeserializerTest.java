package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.Puzzle;
import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
public class DeserializerTest {

    private static Position[] islandPositions = {
        new Position(0, 0),
        new Position(0, 5),
        new Position(0, 7),
        new Position(2, 1),
        new Position(2, 4),
        new Position(4, 1),
        new Position(4, 4),
        new Position(6, 0),
        new Position(6, 4),
        new Position(8, 0),
        new Position(8, 3),
        new Position(9, 1),
        new Position(9, 5),
        new Position(10, 0),
        new Position(10, 4),
        new Position(10, 7)
    };

    @Test
    public void testBridgesDeserialization() throws Exception {
        final File testFile = new File(getClass().getResource("test_01.sol.bgs").toURI());
        final Puzzle puzzle = Deserializer.loadPuzzle(testFile);

        assertNotNull("Puzzle is unexpectedly null.", puzzle);
        Set<Bridge> bridges = puzzle.getBridges();
        assertContainsBridge(bridges, islandPositions[0], islandPositions[1], false);
        assertContainsBridge(bridges, islandPositions[0], islandPositions[7], true);
        assertContainsBridge(bridges, islandPositions[1], islandPositions[2], true);
        assertContainsBridge(bridges, islandPositions[1], islandPositions[12], false);
        assertContainsBridge(bridges, islandPositions[2], islandPositions[15], false);
        assertContainsBridge(bridges, islandPositions[3], islandPositions[4], false);
        assertContainsBridge(bridges, islandPositions[3], islandPositions[5], false);
        assertContainsBridge(bridges, islandPositions[5], islandPositions[6], true);
        assertContainsBridge(bridges, islandPositions[6], islandPositions[8], false);
        assertContainsBridge(bridges, islandPositions[7], islandPositions[8], false);
        assertContainsBridge(bridges, islandPositions[7], islandPositions[9], false);
        assertContainsBridge(bridges, islandPositions[9], islandPositions[10], false);
        assertContainsBridge(bridges, islandPositions[9], islandPositions[13], true);
        assertContainsBridge(bridges, islandPositions[11], islandPositions[12], false);
        assertContainsBridge(bridges, islandPositions[13], islandPositions[14], true);
    }

    private void assertContainsBridge(final Set<Bridge> bridges, final Position start, final Position end, final boolean doubleBridge) {
        boolean anyMatch = bridges.stream().anyMatch(bridge -> {
            Island island1 = bridge.getIsland1();
            Island island2 = bridge.getIsland2();

            boolean positionsMatch
                = island1.getPosition().equals(start) && island2.getPosition().equals(end)
                   || island1.getPosition().equals(end) && island2.getPosition().equals(start);

            return positionsMatch && bridge.isDoubleBridge() == doubleBridge;
        });
        assertTrue("Expected bridge not found.", anyMatch);
    }
}