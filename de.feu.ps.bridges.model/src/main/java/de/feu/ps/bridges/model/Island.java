package de.feu.ps.bridges.model;

import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public interface Island {
    int getColumn();
    int getRow();
    int getRequiredBridges();
    int getRemainingBridges();
    Island getNorthNeighbour();
    Island getEastNeighbour();
    Island getSouthNeighbour();
    Island getWestNeighbour();
    IslandStatus getStatus();
    void addBridge(Bridge bridge);
    void setSouthNeighbour(Island island);
    void setNorthNeighbour(Island northNeighbour);
    void setEastNeighbour(Island island);
    void setWestNeighbour(Island westNeighbour);
    Set<Bridge> getBridges();
}
