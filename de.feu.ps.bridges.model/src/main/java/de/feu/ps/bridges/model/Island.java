package de.feu.ps.bridges.model;

import java.util.Optional;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public interface Island {
    int getColumn();
    int getRow();
    int getRequiredBridges();
    int getRemainingBridges();
    Optional<Island> getNorthNeighbour();
    Optional<Island> getEastNeighbour();
    Optional<Island> getSouthNeighbour();
    Optional<Island> getWestNeighbour();
    Optional<Island> getNeighbour(Direction direction);
    IslandStatus getStatus();
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
    Set<Island> getNeighbours();
    boolean hasNeighbour(Direction direction);
    boolean isBridgedTo(Island island);
    Optional<Bridge> getBridgeTo(Island island);
    int getActualBridgesCount();
    Set<Island> getBridgedNeighbours();
}
