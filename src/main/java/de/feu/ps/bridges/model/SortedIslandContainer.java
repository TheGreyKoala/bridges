package de.feu.ps.bridges.model;

import java.util.*;

/**
 * Abstract class for a sorted container for {@link Island}s.
 *
 * When an {@link Island} is added, it will be linked to its
 * preceding and its following island, so that {@link Island#getNeighbour(Direction)}
 * returns the correct {@link Island}s.
 *
 * @author Tim Gremplewski
 */
abstract class SortedIslandContainer {

    private final TreeSet<ModifiableIsland> islands;

    /**
     * Creates a new container that uses the given {@link Comparator}
     * to sort its {@link Island}s.
     *
     * @param comparator used to sort the {@link Island}s in this container.
     */
    SortedIslandContainer(final Comparator<Island> comparator) {
        Objects.requireNonNull(comparator, "Parameter 'comparator' must not be null.");
        this.islands = new TreeSet<>(comparator);
    }

    /**
     * Add an island to this container and link it to its preceding and following island.
     * If this container already contains <code>island</code>, nothing will change and happen.
     *
     * @param island {@link Island} to be added.
     * @throws NullPointerException if <code>island</code> is null.
     */
    void addIsland(final ModifiableIsland island) {
        Objects.requireNonNull(island, "Parameter 'island' must not be null.");
        if (!islands.contains(island)) {
            islands.add(island);
            linkToPrecedingIsland(island);
            linkToFollowingIsland(island);
        }
    }

    private void linkToPrecedingIsland(final ModifiableIsland newIsland) {
        SortedSet<ModifiableIsland> precedingIslands = islands.headSet(newIsland);
        if (!precedingIslands.isEmpty()) {
            linkToPrecedingIsland(newIsland, precedingIslands.last());
        }
    }

    /**
     * Links a newly added {@link Island} to its preceding {@link Island}.
     *
     * @param island thew newly added {@link Island}.
     * @param precedingIsland the preceding {@link Island}.
     */
    protected abstract void linkToPrecedingIsland(ModifiableIsland island, ModifiableIsland precedingIsland);

    private void linkToFollowingIsland(final ModifiableIsland newIsland) {
        SortedSet<ModifiableIsland> followingIslands = islands.tailSet(newIsland, false);
        if (!followingIslands.isEmpty()) {
            linkToFollowingIsland(newIsland, followingIslands.first());
        }
    }

    /**
     * Links a newly added {@link Island} to its following {@link Island}.
     *
     * @param island thew newly added {@link Island}.
     * @param followingIsland the following {@link Island}.
     */
    protected abstract void linkToFollowingIsland(ModifiableIsland island, ModifiableIsland followingIsland);

    /**
     * Get all {@link Island}s in this container.
     * The returned {@link Set} might not keep the order of the islands in this container.
     * @return All {@link Island}s in this container.
     */
    Set<Island> getIslands() {
        return new HashSet<>(islands);
    }

    public Optional<Island> getFirstUnfinishedIsland(Direction direction) {
        if (islands.isEmpty()) {
            return Optional.empty();
        } else {
            for (ModifiableIsland island : islands) {
                if (island.getRequiredBridges() < 8 && !island.getNeighbour(direction).isPresent()) {
                    return Optional.of(island);
                }
            }
        }
        return Optional.empty();
    }
}
