package de.feu.ps.bridges.shared;

/**
 * Interface that defines means to test the implementation.
 */
public interface BridgesTester {

    /**
     * Generate a puzzle with the given parameters and save it in a file under the given path.
     * @param filePath Location where the generated puzzle should be saved.
     * @param width width of the generated puzzle.
     * @param height height of the generated puzzle.
     * @param isles Amount of islands in the generated puzzle.
     */
    void testGeneratePuzzle(String filePath, int width, int height, int isles);

    /**
     * Load a puzzle from the given file, solve it and save the solution in the given file.
     * @param puzzlePath Location from where the puzzle should be loaded.
     * @param solutionPath Location where to save the solution.
     */
    void testSolvePuzzle(String puzzlePath, String solutionPath);
}
