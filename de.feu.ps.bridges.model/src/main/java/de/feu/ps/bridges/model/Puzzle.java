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
     * If <code>doubleBridge</code> is true, the new bridge will be a double bridge.
     *
     * @param island1 Island to be bridged.
     * @param island2 Another island to be bridged.
     * @param doubleBridge indicates whether the new bridge should be a double bridge or not.
     * @return a new {@link Bridge} that bridges the given {@link Island}s.
     * @throws NullPointerException if island1 or island2 is null.
     * @throws IllegalArgumentException if a bridge cannot be build between these islands.
     */
    Bridge buildBridge(Island island1, Island island2, boolean doubleBridge);

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
     * Get all islands that don't have the required amount of bridges yet.
     * @return all islands that don't have the required amount of bridges yet.
     */
    //TODO Maybe move to analyzer as well.
    Set<Island> getUnfinishedIslands();

    /**
     * Indicates of any bridge crosses the path from <code>start</code> to <code>end</code>.
     * @param start Start position of the path to be checked for crossing bridges.
     * @param end End position of the path to be checked for crossing bridges.
     * @return true, if a bridge is crossing the path from <code>start</code> to <code>end</code>, false otherwise.
     * @throws NullPointerException if <code>start</code> or <code>end</code> is null.
     */
    // TODO: Move this method to the analyzer module
    boolean isAnyBridgeCrossing(final Position start, final Position end);

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
