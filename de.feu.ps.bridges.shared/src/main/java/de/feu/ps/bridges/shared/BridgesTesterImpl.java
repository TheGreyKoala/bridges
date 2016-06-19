package de.feu.ps.bridges.shared;

import de.feu.ps.bridges.generator.PuzzleGeneratorImpl;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.serialization.Deserializer;
import de.feu.ps.bridges.serialization.Serializer;
import de.feu.ps.bridges.solver.DefaultSolver;
import de.feu.ps.bridges.solver.Solver;

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
        Puzzle puzzle = PuzzleGeneratorImpl.generatePuzzle(width, height, isles);
        Serializer serializer = new Serializer();
        serializer.storePuzzle(puzzle, filePath);
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
        Deserializer deserializer = new Deserializer();
        Puzzle puzzle = deserializer.loadPuzzle(puzzlePath);
        Solver solver = DefaultSolver.createSolverFor(puzzle);
        solver.solve();
        Serializer serializer = new Serializer();
        serializer.storePuzzle(puzzle, solutionPath);
    }
}
