package de.feu.ps.bridges.model;

/**
 * {@link Island} that can be modified.
 * @author Tim Gremplewski
 */
interface ModifiableIsland extends Island {

    /**
     * Add the given {@link Bridge} to this island.
     * @param bridge {@link Bridge} to add to this island.
     * @throws IllegalArgumentException if {@link Bridge#getBridgedIslands()}
     *  on the give bridge does not contain this island.
     */
    void addBridge(Bridge bridge);

    /**
     * Removes all bridges from this island.
     */
    void removeAllBridges();

    /**
     * Removes the given bridge from this island.
     * @param bridge {@link Bridge} to remove.
     */
    void removeBridge(Bridge bridge);

    /**
     * Set the neighbour in the given {@link Direction}.
     * @param neighbour neighbour to set
     * @param direction direction to set the neighbour
     */
    void setNeighbour(Island neighbour, Direction direction);

    /**
     * Set the amount of required {@link Bridge}es of this neighbour.
     * @param requiredBridges amount of required {@link Bridge}es of this neighbour.
     */
    void setRequiredBridges(int requiredBridges);
}
