package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.model.Bridge;

import java.util.Optional;

/**
 * @author Tim Gremplewski
 */
public interface Solver {
    void solve();
    Optional<Bridge> getNextMove();
}
