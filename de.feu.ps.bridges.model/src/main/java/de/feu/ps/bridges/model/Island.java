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
    boolean hasNorthNeighbour();
    boolean hasEastNeighbour();
    boolean hasSouthNeighbour();
    boolean hasWestNeighbour();
    Position getPosition();
    int getDistanceToNorthNeighbour();
    int getDistanceToEastNeighbour();
    int getDistanceToSouthNeighbour();
    int getDistanceToWestNeighbour();
    boolean isBridgedToNeighbour(Direction direction);
    void removeAllBridges();
    void setRequiredBridges(int requiredBridges);
}
