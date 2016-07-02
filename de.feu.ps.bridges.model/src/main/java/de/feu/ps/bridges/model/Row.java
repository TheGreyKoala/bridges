package de.feu.ps.bridges.model;

/**
 * A row in a puzzle.
 *
 * {@link Island}s are automatically ordered by their column within a row.
 * Furthermore they are automatically linked to each other so that {@link Island#getNeighbour(Direction)}
 * returns the correct island.
 *
 * @author Tim Gremplewski
 */
class Row extends SortedIslandContainer {

    private final int index;

    /**
     * Creates a new empty row.
     */
    public Row(final int index) {
        super((o1, o2) -> o1.getColumnIndex() - o2.getColumnIndex());

        if (index < 0) {
            throw new IllegalArgumentException("Parameter 'index' must not be less than 0.");
        }
        this.index = index;
    }

    /**
     * Does the same as {@link SortedIslandContainer#addIsland(ModifiableIsland)}
     * but checks that the added {@link Island} lies in this row.
     *
     * @param island {@link Island} to be added.
     * @throws NullPointerException if <code>island</code> is null.
     * @throws IllegalArgumentException if <code>island</code> does not lie in this row.
     */
    @Override
    public void addIsland(final ModifiableIsland island) {
        if (island.getRowIndex() != index) {
            throw new IllegalArgumentException("This islands lies in another row: " + island.getRowIndex());
        }
        super.addIsland(island);
    }

    @Override
    protected void linkToPrecedingIsland(final ModifiableIsland island, final ModifiableIsland precedingIsland) {
        precedingIsland.setNeighbour(island, Direction.EAST);
        island.setNeighbour(precedingIsland, Direction.WEST);
    }

    @Override
    protected void linkToFollowingIsland(final ModifiableIsland island, final ModifiableIsland followingIsland) {
        followingIsland.setNeighbour(island, Direction.WEST);
        island.setNeighbour(followingIsland, Direction.EAST);
    }
}
