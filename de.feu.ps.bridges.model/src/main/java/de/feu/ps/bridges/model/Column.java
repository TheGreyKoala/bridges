package de.feu.ps.bridges.model;

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
        super((o1, o2) -> o1.getRowIndex() - o2.getRowIndex());

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
        if (island.getColumnIndex() != index) {
            throw new IllegalArgumentException("This islands lies in another column: " + island.getColumnIndex());
        }
        super.addIsland(island);
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
