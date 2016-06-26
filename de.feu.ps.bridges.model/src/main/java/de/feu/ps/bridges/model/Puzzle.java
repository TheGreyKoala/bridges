package de.feu.ps.bridges.model;

import java.util.Optional;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public interface Puzzle {
    int getColumnsCount();
    int getRowsCount();
    Set<Island> getIslands();
    Set<Bridge> getBridges();
    Bridge buildBridge(Island island1, Island island2, boolean doubleBridge);
    Island getIslandAt(int column, int row);
    void removeAllBridges();
    boolean isAnyBridgeCrossing(final Position otherBridgeStart, final Position otherBridgeEnd);
    Optional<Bridge> tearDownBridge(Island island1, Island island2);
    Set<Island> getUnfinishedIslands();
}
