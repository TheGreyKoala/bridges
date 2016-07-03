package de.feu.ps.bridges.shared;

import de.feu.ps.bridges.toolkit.PuzzleToolkit;
import de.feu.ps.bridges.toolkit.PuzzleToolkitFactory;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link BridgesTester}.
 * @author Tim Gremplewski
 */
public class BridgesTesterImpl implements BridgesTester {

    private static final Logger logger = Logger.getLogger(BridgesTesterImpl.class.getName());

    @Override
    public void testGeneratePuzzle(String filePath, int width, int height, int isles) {
        try {
            generatePuzzle(filePath, width, height, isles);
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Could generate and store puzzle.", e);
        }
    }

    private void generatePuzzle(String filePath, int width, int height, int isles) {
        final PuzzleToolkit puzzleToolkit = PuzzleToolkitFactory.createForGeneratedPuzzle(width, height, isles);
        puzzleToolkit.savePuzzle(new File(filePath));
    }

    @Override
    public void testSolvePuzzle(String puzzlePath, String solutionPath) {
        try {
            solvePuzzle(puzzlePath, solutionPath);
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Could solve and store puzzle.", e);
        }
    }

    private void solvePuzzle(String puzzlePath, String solutionPath) {
        final PuzzleToolkit puzzleToolkit = PuzzleToolkitFactory.createForLoadedPuzzle(new File(puzzlePath));
        puzzleToolkit.solvePuzzle();
        puzzleToolkit.savePuzzle(new File(solutionPath));
    }
}
