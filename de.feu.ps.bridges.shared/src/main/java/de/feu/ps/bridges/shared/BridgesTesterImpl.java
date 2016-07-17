package de.feu.ps.bridges.shared;

import de.feu.ps.bridges.toolkit.PuzzleToolkit;
import de.feu.ps.bridges.toolkit.PuzzleToolkitFactory;

import java.io.File;

/**
 * Implementation of {@link BridgesTester}.
 * @author Tim Gremplewski
 */
public class BridgesTesterImpl implements BridgesTester {

    /**
     * Creates a new instance.
     */
    public BridgesTesterImpl() {
    }

    @Override
    public void testGeneratePuzzle(String filePath, int width, int height, int isles) {
        final PuzzleToolkit puzzleToolkit = PuzzleToolkitFactory.createForGeneratedPuzzle(width, height, isles);
        puzzleToolkit.savePuzzle(new File(filePath));
    }

    @Override
    public void testSolvePuzzle(String puzzlePath, String solutionPath) {
        final PuzzleToolkit puzzleToolkit = PuzzleToolkitFactory.createForLoadedPuzzle(new File(puzzlePath));
        puzzleToolkit.solvePuzzle();
        puzzleToolkit.savePuzzle(new File(solutionPath));
    }
}
