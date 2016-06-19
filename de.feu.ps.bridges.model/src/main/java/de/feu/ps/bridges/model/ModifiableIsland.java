package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
interface ModifiableIsland extends Island {
    void addBridge(Bridge bridge);
    void setSouthNeighbour(Island island);
    void setNorthNeighbour(Island northNeighbour);
    void setEastNeighbour(Island island);
    void setWestNeighbour(Island westNeighbour);

    void removeAllBridges();
    void setRequiredBridges(int requiredBridges);
    void removeBridge(Bridge bridge);
}
