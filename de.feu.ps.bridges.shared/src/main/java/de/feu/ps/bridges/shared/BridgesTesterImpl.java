package de.feu.ps.bridges.shared;

import de.feu.ps.bridges.facade.Facade;
import de.feu.ps.bridges.generator.PuzzleGeneratorFactory;
import de.feu.ps.bridges.model.Puzzle;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
        Puzzle puzzle = PuzzleGeneratorFactory.createPuzzleGenerator(width, height, isles).generate();
        Facade.savePuzzle(puzzle, new File(filePath));
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
        Puzzle puzzle = Facade.loadPuzzle(new File(puzzlePath));
        Facade.solvePuzzle(puzzle);
        Facade.savePuzzle(puzzle, new File(solutionPath));
    }
}
