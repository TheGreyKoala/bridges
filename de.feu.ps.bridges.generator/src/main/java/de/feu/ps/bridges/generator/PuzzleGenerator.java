package de.feu.ps.bridges.generator;

import de.feu.ps.bridges.model.Puzzle;

/**
 * Interface that defines means to generate a new puzzle.
 * @author Tim Gremplewski
 */
public interface PuzzleGenerator {

    /**
     * Generate a new puzzle.
     * @return a new puzzle.
     */
    Puzzle generate();
}
