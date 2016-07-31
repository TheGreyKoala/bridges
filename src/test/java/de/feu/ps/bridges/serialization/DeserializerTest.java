package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.Puzzle;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class DeserializerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @DataPoints("syntaxErrors")
    public static File[] syntaxErrorPuzzles() throws URISyntaxException {
        final URI syntaxErrorsDirectoryUri = DeserializerTest.class.getResource("syntax_errors").toURI();
        final File syntaxErrorsDirectory = new File(syntaxErrorsDirectoryUri);
        return syntaxErrorsDirectory.listFiles();
    }

    @DataPoints("semanticErrors")
    public static File[] semanticErrorPuzzles() throws URISyntaxException {
        final URI semanticErrorsDirectoryUri = DeserializerTest.class.getResource("semantic_errors").toURI();
        final File semanticErrorsDirectory = new File(semanticErrorsDirectoryUri);
        return semanticErrorsDirectory.listFiles();
    }

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

    @Theory
    public void testSyntaxError(@FromDataPoints("syntaxErrors") final File sourceFile) {
        expectedException.expect(SerializationException.class);
        Deserializer.loadPuzzle(sourceFile);
    }

    @Theory
    public void testSemanticError(@FromDataPoints("semanticErrors") final File sourceFile) {
        expectedException.expect(SerializationException.class);
        Deserializer.loadPuzzle(sourceFile);
    }
}