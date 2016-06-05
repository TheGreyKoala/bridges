package de.feu.ps.bridges.model;

import java.util.Set;

/**
 * Interface for bridges that connect two {@link Island}s.
 * A bridge can connect two islands two times.
 *
 * @author Tim Gremplewski
 */
public interface Bridge {

    /**
     * Get all {@link Island}s that are connected by this {@link Bridge}.
     * @return {@link Set} of all {@link Island} that are connected by this {@link Bridge}.
     */
    Set<Island> getConnectedIslands();

    /**
     * The {@link Island} that is connected to the {@link Island} returned by {@link #getIsland2()}.
     * @return An {@link Island}
     */
    Island getIsland1();

    /**
     * The {@link Island} that is connected to the {@link Island} returned by {@link #getIsland1()}.
     * @return An {@link Island}
     */
    Island getIsland2();

    /**
     * Indicates whether this {@link Bridge} connects its {@link Island}s two times.
     * @return <code>true</code> if this {@link Bridge} connects its {@link Island}s two times.
     */
    boolean isDoubleBridge();
}
