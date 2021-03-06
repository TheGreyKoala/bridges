package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Puzzle;

/**
 * Factory class that creates a new {@link PuzzleAnalyser}.
 * @author Tim Gremplewski
 */
public final class PuzzleAnalyserFactory {

    private PuzzleAnalyserFactory() {
    }

    /**
     * Creates a new {@link PuzzleAnalyser} for the given puzzle.
     * @param puzzle the puzzle to be analysed.
     * @return a new {@link PuzzleAnalyser} for the given puzzle.
     */
    public static PuzzleAnalyser createPuzzleAnalyserFor(final Puzzle puzzle) {
        MoveAnalyser moveAnalyser = new MoveAnalyser(puzzle);
        StatusAnalyser statusAnalyser = new StatusAnalyser(puzzle, moveAnalyser);
        NewIslandAnalyser newIslandAnalyser = new NewIslandAnalyser(puzzle, moveAnalyser);
        return new DefaultPuzzleAnalyser(moveAnalyser, statusAnalyser, newIslandAnalyser);
    }
}
