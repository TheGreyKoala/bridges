package de.feu.ps.bridges.shared;

import de.feu.ps.bridges.generator.PuzzleGeneratorImpl;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.serialization.Serializer;

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

        // TODO Test

        Puzzle puzzle = PuzzleGeneratorImpl.generatePuzzle(width, height, isles);
        Serializer serializer = new Serializer();
        try {
            serializer.storePuzzle(puzzle, filePath);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Could not store puzzle.", e);
        }
    }

    @Override
    public void testSolvePuzzle(String puzzlePath, String solutionPath) {

    }
}
