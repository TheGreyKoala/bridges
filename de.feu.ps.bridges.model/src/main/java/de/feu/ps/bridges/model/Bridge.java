package de.feu.ps.bridges.model;

import java.util.Set;

/**
 * Interface for a bridge that bridges two {@link Island}s.
 * A bridge can be a double bridge, meaning that actually two bridged exists between the islands.
 * Bridged islands must lie in the same row or the same column.
 *
 * @author Tim Gremplewski
 */
public interface Bridge {

    /**
     * Get all {@link Island}s that are bridged by this {@link Bridge}.
     * Namely this will be the islands returned by {@link #getIsland1()} and {@link #getIsland2()}.
     * @return {@link Set} including the bridged {@link Island}s.
     */
    Set<Island> getBridgedIslands();

    /**
     * The {@link Island} that is connected to the {@link Island} returned by {@link #getIsland2()}.
     * @return An {@link Island}.
     */
    Island getIsland1();

    /**
     * The {@link Island} that is connected to the {@link Island} returned by {@link #getIsland1()}.
     * @return An {@link Island}.
     */
    Island getIsland2();

    /**
     * Indicates whether the this bridge is a double bridge.
     * @return <code>true</code>, if this bridge is a double bridge, <code>false</code> otherwise.
     */
    boolean isDoubleBridge();

    /**
     * Indicates whether this bridge is aligned horizontally.
     * The value returned by this method will always be complementary to the value returned by {@link #isVertical()}.
     * @return <code>true</code>, if this bridge is aligned horizontally, <code>false</code> otherwise.
     */
    boolean isHorizontal();

    /**
     * Indicates whether this bridge is aligned vertically.
     * The value returned by this method will always be complementary to the value returned by {@link #isHorizontal()} ()}.
     * @return <code>true</code>, if this bridge is aligned vertically, <code>false</code> otherwise.
     */
    boolean isVertical();
}
