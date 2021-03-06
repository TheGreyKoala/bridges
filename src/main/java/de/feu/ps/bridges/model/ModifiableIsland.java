package de.feu.ps.bridges.model;

/**
 * {@link Island} that can be modified.
 * @author Tim Gremplewski
 */
interface ModifiableIsland extends Island {

    /**
     * Add the given {@link Bridge} to this island.
     * @param bridge {@link Bridge} to add to this island.
     * @throws NullPointerException if bridge is null.
     * @throws IllegalStateException if this island does not require any more bridges.
     * @throws IllegalArgumentException if {@link Bridge#getBridgedIslands()}
     *  on the given bridge does not contain this island.
     */
    void addBridge(Bridge bridge);

    /**
     * Removes all bridges from this island.
     */
    void removeAllBridges();

    /**
     * Removes the given bridge from this island.
     * @param bridge {@link Bridge} to remove.
     * @throws NullPointerException if bridge is null.
     */
    void removeBridge(Bridge bridge);

    /**
     * Set the neighbour in the given {@link Direction}.
     * @param neighbour neighbour to set
     * @param direction direction to set the neighbour
     * @throws NullPointerException if neighbour or direction is null
     */
    void setNeighbour(Island neighbour, Direction direction);

    /**
     * Set the amount of required {@link Bridge}es of this neighbour.
     * @param requiredBridges amount of required {@link Bridge}es of this neighbour.
     * @throws IllegalArgumentException if requiredBridges is less than 1.
     */
    void setRequiredBridges(int requiredBridges);
}
