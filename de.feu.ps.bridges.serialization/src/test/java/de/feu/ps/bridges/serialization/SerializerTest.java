package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.model.PuzzleBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Tim Gremplewski
 */
public class SerializerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void storePuzzle() throws Exception {
        PuzzleBuilder builder = new PuzzleBuilder();
        builder.setPuzzleDimensions(7, 7);
        builder.setIslandsCount(12);
        builder.addIsland(0, 0, 3);
        builder.addIsland(0, 3, 4);
        builder.addIsland(0, 5, 2);
        builder.addIsland(1, 1, 2);
        builder.addIsland(1, 6, 3);
        builder.addIsland(3, 1, 3);
        builder.addIsland(3, 3, 3);
        builder.addIsland(3, 5, 1);
        builder.addIsland(4, 0, 5);
        builder.addIsland(4, 6, 5);
        builder.addIsland(6, 0, 4);
        builder.addIsland(6, 6, 3);

        builder.addBridge(0, 1, true);
        builder.addBridge(0, 8, false);
        builder.addBridge(1, 2, true);
        builder.addBridge(3, 4, false);
        builder.addBridge(3, 5, false);
        builder.addBridge(4, 9, true);
        builder.addBridge(5, 6, true);
        builder.addBridge(6, 7, false);
        builder.addBridge(8, 9, true);
        builder.addBridge(8, 10, true);
        builder.addBridge(9, 11, false);
        builder.addBridge(10, 11, true);

        Puzzle puzzle = builder.getResult();

        Serializer serializer = new Serializer();
        File root = temporaryFolder.getRoot();
        String destination = root.getAbsolutePath() + "/bsp_abb2_loesungen.bgs";
        serializer.storePuzzle(puzzle, destination);

        final String expectedResult = getClass().getResource("bsp_abb2_loesung.bgs").toURI().getPath();

        try (BufferedReader expectedResultReader = new BufferedReader(new FileReader(expectedResult));
            BufferedReader actualResultReader = new BufferedReader(new FileReader(destination))) {

            String expectedLine = expectedResultReader.readLine();
            while (expectedLine != null) {
                String actualLine = actualResultReader.readLine();
                assertEquals("Lines mismatch.", expectedLine, actualLine);
                expectedLine = expectedResultReader.readLine();
            }

            assertNull("Actual result has more lines than expected result.", actualResultReader.readLine());
        }
    }
}