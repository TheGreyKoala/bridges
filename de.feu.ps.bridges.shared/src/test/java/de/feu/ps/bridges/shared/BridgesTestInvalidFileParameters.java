package de.feu.ps.bridges.shared;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Tim Gremplewski
 */
public class BridgesTestInvalidFileParameters {

    @Test(expected = IllegalArgumentException.class)
    public void testGeneratePuzzle_filePathExists() throws IOException {
        File tempFile = File.createTempFile("my_temp_file", "bgs");
        BridgesTesterImpl tester = new BridgesTesterImpl();
        tester.testGeneratePuzzle(tempFile.getAbsolutePath(), 10, 10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolvePuzzle_puzzlePathNotExists() {
        BridgesTesterImpl tester = new BridgesTesterImpl();
        tester.testSolvePuzzle("my_file.bgs", "my_sol.bgs");
    }
}
