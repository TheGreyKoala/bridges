package de.feu.ps.bridges.model;

import java.util.Optional;

/**
 * A column in a puzzle.
 *
 * {@link Island}s are automatically ordered by their row within a column.
 * Furthermore they are automatically linked to each other so that {@link Island#getNeighbour(Direction)}
 * returns the correct island.
 *
 * @author Tim Gremplewski
 */
class Column extends SortedIslandContainer {

    private final int index;

    /**
     * Creates a new empty column.
     */
    public Column(final int index) {
        super((o1, o2) -> o1.getPosition().getRow() - o2.getPosition().getRow());

        if (index < 0) {
            throw new IllegalArgumentException("Parameter 'index' must not be less than 0.");
        }
        this.index = index;
    }

    /**
     * Does the same as {@link SortedIslandContainer#addIsland(ModifiableIsland)}
     * but checks that the added {@link Island} lies in this column.
     *
     * @param island {@link Island} to be added.
     * @throws NullPointerException if <code>island</code> is null.
     * @throws IllegalArgumentException if <code>island</code> does not lie in this column.
     */
    @Override
    public void addIsland(final ModifiableIsland island) {
        final int islandColumn = island.getPosition().getColumn();
        if (islandColumn != index) {
            throw new IllegalArgumentException("This islands lies in another column: " + islandColumn);
        }
        super.addIsland(island);
    }

    /**
     * Returns the island at the given row in this column.
     * @param row the row of the requested island.
     * @return {@link Optional} containing the requested island, if it exists.
     */
    public Optional<Island> getIslandAtRow(final int row) {
        return getIslands().stream().filter(island -> island.getPosition().getRow() == row).findFirst();
    }

    @Override
    protected void linkToPrecedingIsland(final ModifiableIsland island, final ModifiableIsland precedingIsland) {
        precedingIsland.setNeighbour(island, Direction.SOUTH);
        island.setNeighbour(precedingIsland, Direction.NORTH);
    }

    @Override
    protected void linkToFollowingIsland(final ModifiableIsland island, final ModifiableIsland followingIsland) {
        followingIsland.setNeighbour(island, Direction.NORTH);
        island.setNeighbour(followingIsland, Direction.SOUTH);
    }
}
