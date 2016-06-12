package de.feu.ps.bridges.model;

import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public interface Puzzle {
    int getColumnsCount();
    int getRowsCount();
    Set<Island> getIslands();
    Set<Bridge> getBridges();
    void addIsland(Island island);
    void addBridge(Bridge bridge);
    PuzzleStatus getStatus();
    Island getIslandAt(int column, int row);
    void removeAllBridges();
    boolean isAnyBridgeCrossing(final Position otherBridgeStart, final Position otherBridgeEnd);
}
