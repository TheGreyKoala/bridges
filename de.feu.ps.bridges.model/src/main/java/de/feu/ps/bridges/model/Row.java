package de.feu.ps.bridges.model;

import java.util.*;

/**
 * @author Tim Gremplewski
 */
public class Row {

    private final int index;
    private final TreeSet<Island> islands;

    public Row(final int index) {
        // TODO validate index
        this.index = index;
        islands = new TreeSet<>((o1, o2) -> o1.getColumn() - o2.getColumn());
    }

    public void addIsland(final Island island) {
        // TODO validate island

        islands.add(island);
        SortedSet<Island> precedingIslands = this.islands.headSet(island);
        if (!precedingIslands.isEmpty()) {
            Island westNeighbour = precedingIslands.last();
            westNeighbour.setEastNeighbour(island);
            island.setWestNeighbour(westNeighbour);
        }

        NavigableSet<Island> followingIslands = this.islands.tailSet(island, false);
        if (!followingIslands.isEmpty()) {
            Island eastNeighbour = followingIslands.first();
            eastNeighbour.setWestNeighbour(island);
            island.setEastNeighbour(eastNeighbour);
        }
    }

    public Set<Island> getIslands() {
        return new HashSet<>(islands);
    }
}
