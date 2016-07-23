package de.feu.ps.bridges.solver;

import java.util.Optional;

/**
 * Interface that defines means to solve a puzzle.
 * @author Tim Gremplewski
 */
public interface PuzzleSolver {

    /**
     * Get a next save move, if any exists.
     * @return {@link Optional} containing a next save move, if one exists.
     */
    Optional<Move> getNextMove();

    /**
     * Apply as many save moves as possible.
     */
    void solve();
}
