package de.feu.ps.bridges.facade;

import de.feu.ps.bridges.generator.PuzzleGenerator;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.serialization.Deserializer;
import de.feu.ps.bridges.serialization.Serializer;
import de.feu.ps.bridges.solver.DefaultSolver;
import de.feu.ps.bridges.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Tim Gremplewski
 */
public class Facade {

    public static Puzzle loadPuzzle(final File sourceFile) {
        Deserializer deserializer = new Deserializer();
        try {
            return deserializer.loadPuzzle(sourceFile.getPath());
        } catch (Exception e) {
            // TODO exception handling!
            e.printStackTrace();
            return null;
        }
    }

    public static void savePuzzle(final Puzzle puzzle, final File destinationFile) {
        Serializer serializer = new Serializer();
        try {
            serializer.storePuzzle(puzzle, destinationFile.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void solvePuzzle(final Puzzle puzzle) {
        Solver solver = DefaultSolver.createSolverFor(puzzle);
        solver.solve();
    }

    public static Puzzle newPuzzle() {
        return PuzzleGenerator.generatePuzzle();
    }

    public static Puzzle newPuzzle(final int columns, final int rows) {
        return PuzzleGenerator.generatePuzzle(columns, rows);
    }

    public static Puzzle newPuzzle(final int columns, final int rows, final int islands) {
        return PuzzleGenerator.generatePuzzle(columns, rows, islands);
    }
}
