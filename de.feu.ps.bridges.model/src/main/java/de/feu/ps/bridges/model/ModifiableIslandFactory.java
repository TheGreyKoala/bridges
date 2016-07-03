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
     * @throws IllegalArgumentException if one of the following is true
     *  <ul>
     *      <li>position.getColumn is less than 0</li>
     *      <li>position.getRow is less then 0</li>
     *      <li>requiredBridges is less than 1</li>
     *  </ul>
     */
    static ModifiableIsland create(final Position position, final int requiredBridges) {
        return new DefaultIsland(position, requiredBridges);
    }
}
