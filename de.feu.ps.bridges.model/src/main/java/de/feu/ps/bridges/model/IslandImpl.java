package de.feu.ps.bridges.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Gremplewski
 */
public class IslandImpl implements Island {

    private final int column;
    private final int row;
    private final int requiredBridges;
    private final List<Bridge> bridges;

    private Island northNeighbour;
    private Island eastNeighbour;
    private Island southNeighbour;
    private Island westNeighbour;

    public IslandImpl(final int column, final int row, final int requiredBridges) {

        // TODO: Parameter validation

        this.column = column;
        this.row = row;
        this.requiredBridges = requiredBridges;
        bridges = new ArrayList<>(requiredBridges);
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getRequiredBridges() {
        return requiredBridges;
    }

    @Override
    public int getRemainingBridges() {
        return requiredBridges - bridges.size();
    }

    @Override
    public Island getNorthNeighbour() {
        return northNeighbour;
    }

    @Override
    public Island getEastNeighbour() {
        return eastNeighbour;
    }

    @Override
    public Island getSouthNeighbour() {
        return southNeighbour;
    }

    @Override
    public Island getWestNeighbour() {
        return westNeighbour;
    }

    @Override
    public IslandStatus getStatus() {
        //TODO: Implement de.feu.ps.bridges.model.Island.getStatus
        return null;
    }

    @Override
    public void addBridge(Bridge bridge) {
        //TODO Parameter validation
        bridges.add(bridge);
    }

    @Override
    public void setSouthNeighbour(Island southNeighbour) {
        // TODO validate island
        this.southNeighbour = southNeighbour;
    }

    @Override
    public void setNorthNeighbour(Island northNeighbour) {
        // TODO validate island
        this.northNeighbour = northNeighbour;
    }

    @Override
    public void setEastNeighbour(Island eastNeighbour) {
        // TODO validate island
        this.eastNeighbour = eastNeighbour;
    }

    @Override
    public void setWestNeighbour(Island westNeighbour) {
        // TODO validate island
        this.westNeighbour = westNeighbour;
    }
}
