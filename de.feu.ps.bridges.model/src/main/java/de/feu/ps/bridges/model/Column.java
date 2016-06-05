package de.feu.ps.bridges.model;

import java.util.*;

/**
 * @author Tim Gremplewski
 */
public class Column {

    private final int index;
    private final TreeSet<Island> islands;

    public Column(final int index) {
        // TODO validate index
        this.index = index;
        islands = new TreeSet<>((o1, o2) -> o1.getRow() - o2.getRow());
    }

    public void addIsland(final Island island) {
        // TODO validate island

        islands.add(island);
        SortedSet<Island> precedingIslands = this.islands.headSet(island);
        if (!precedingIslands.isEmpty()) {
            Island northNeighbour = precedingIslands.last();
            northNeighbour.setSouthNeighbour(island);
            island.setNorthNeighbour(northNeighbour);
        }

        NavigableSet<Island> followingIslands = this.islands.tailSet(island, false);
        if (!followingIslands.isEmpty()) {
            Island southNeighbour = followingIslands.first();
            southNeighbour.setNorthNeighbour(island);
            island.setSouthNeighbour(southNeighbour);
        }
    }

    public Set<Island> getIslands() {
        return new HashSet<>(islands);
    }

    public Island getIslandAtRow(final int row) {
        // TODO handle not found
        return islands.stream().filter(island -> island.getRow() == row).findFirst().get();
    }
}
