package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.model.Puzzle;

/**
 * Factory class that creates a new {@link PuzzleSolver}.
 * @author Tim Gremplewski
 */
public class PuzzleSolverFactory {

    /**
     * Create a new {@link PuzzleSolver} for the given puzzle.
     * @param puzzle the puzzle to be solved.
     * @return a new {@link PuzzleSolver} for the given puzzle.
     * @throws NullPointerException if puzzle is null.
     */
    public static PuzzleSolver createPuzzleSolverFor(final Puzzle puzzle) {
        return new DefaultPuzzleSolver(puzzle);
    }
}
