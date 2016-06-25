package de.feu.ps.bridges.solver;

import java.util.Optional;

/**
 * @author Tim Gremplewski
 */
public interface Solver {
    void solve();
    Optional<Move> getNextMove();
}
