package de.feu.ps.bridges.shared;

import de.feu.ps.bridges.facade.Facade;
import de.feu.ps.bridges.generator.PuzzleGenerator;
import de.feu.ps.bridges.model.Puzzle;

import java.io.File;
import java.io.FileNotFoundException;
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could generate and store puzzle.", e);
        }
    }

    private void generatePuzzle(String filePath, int width, int height, int isles) throws FileNotFoundException {
        Puzzle puzzle = PuzzleGenerator.generatePuzzle(width, height, isles);
        Facade.savePuzzle(puzzle, new File(filePath));
    }

    @Override
    public void testSolvePuzzle(String puzzlePath, String solutionPath) {
        try {
            solvePuzzle(puzzlePath, solutionPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could solve and store puzzle.", e);
        }
    }

    private void solvePuzzle(String puzzlePath, String solutionPath) throws Exception {
        Puzzle puzzle = Facade.loadPuzzle(new File(puzzlePath));
        Facade.solvePuzzle(puzzle);
        Facade.savePuzzle(puzzle, new File(solutionPath));
    }
}
