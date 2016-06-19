package de.feu.ps.bridges.model;

import java.util.*;

/**
 * @author Tim Gremplewski
 */
class Column {

    private final int index;
    private final TreeSet<ModifiableIsland> islands;

    public Column(final int index) {
        // TODO validate index
        this.index = index;
        islands = new TreeSet<>((o1, o2) -> o1.getRow() - o2.getRow());
    }

    public void addIsland(final ModifiableIsland island) {
        // TODO validate island

        islands.add(island);
        SortedSet<ModifiableIsland> precedingIslands = this.islands.headSet(island);
        if (!precedingIslands.isEmpty()) {
            ModifiableIsland northNeighbour = precedingIslands.last();
            northNeighbour.setSouthNeighbour(island);
            island.setNorthNeighbour(northNeighbour);
        }

        NavigableSet<ModifiableIsland> followingIslands = this.islands.tailSet(island, false);
        if (!followingIslands.isEmpty()) {
            ModifiableIsland southNeighbour = followingIslands.first();
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
