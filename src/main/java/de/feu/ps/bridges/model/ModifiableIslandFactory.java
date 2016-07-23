package de.feu.ps.bridges.model;

/**
 * Factory class to create an instance of {@link ModifiableIsland}.
 * @author Tim Gremplewski
 */
final class ModifiableIslandFactory {

    private ModifiableIslandFactory() {
    }

    /**
     * Create a new {@link ModifiableIsland} object.
     *
     * @param position Position of the new island.
     * @param requiredBridges Amount of required bridges.
     * @throws NullPointerException if position is null.
     * @throws IllegalArgumentException an island can not be build at the given position.
     */
    static ModifiableIsland create(final Position position, final int requiredBridges) {
        return new DefaultIsland(position, requiredBridges);
    }
}
