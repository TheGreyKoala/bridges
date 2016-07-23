package de.feu.ps.bridges.model;

import java.util.Optional;
import java.util.Set;

/**
 * Interface that defines means of a puzzle.
 *
 * @author Tim Gremplewski
 */
public interface Puzzle {

    /**
     * Builds a {@link Bridge} between the given {@link Island}s.
     * If called twice for the same {@link Island}s, a single {@link Bridge} will become a double bridge.
     *
     * @param island1 Island to be bridged.
     * @param island2 Another island to be bridged.
     * @return a new {@link Bridge} that bridges the given {@link Island}s.
     * @throws NullPointerException if island1 or island2 is null.
     * @throws IllegalArgumentException if a bridge cannot be build between these islands.
     * @throws IllegalStateException if a double bridge already exists between the given {@link Island}s.
     */
    Bridge buildBridge(Island island1, Island island2);

    /**
     * Get all bridges in this puzzle.
     * @return all bridges in this puzzle.
     */
    Set<Bridge> getBridges();

    /**
     * Get the amount of columns of this puzzle.
     * @return the amount of columns of this puzzle.
     */
    int getColumnsCount();

    /**
     * Get all islands in this puzzle.
     * @return all islands in this puzzle.
     */
    Set<Island> getIslands();

    /**
     * Get the amount of rows of this puzzle.
     * @return the amount of rows of this puzzle.
     */
    int getRowsCount();

    /**
     * Remove all bridges from this puzzle.
     */
    void removeAllBridges();

    /**
     * Tear down the bridge between island1 and island2
     * @param island1 A bridged island.
     * @param island2 The other bridged island.
     * @return {@link Optional} containing the torn down bridge, if it existed.
     * @throws NullPointerException if <code>island1</code> or <code>island2</code> is null.
     * @throws IllegalArgumentException if <code>island1</code> or <code>island2</code> does not belong to this puzzle.
     */
    Optional<Bridge> tearDownBridge(Island island1, Island island2);
}
