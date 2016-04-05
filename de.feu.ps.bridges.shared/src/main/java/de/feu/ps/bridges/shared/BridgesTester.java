package de.feu.ps.bridges.shared;

/**
 * Test.
 */
public interface BridgesTester {
    void testGeneratePuzzle(String filePath, int width, int height, int isles);
    void testSolvePuzzle(String puzzlePath, String solutionPath);
}
