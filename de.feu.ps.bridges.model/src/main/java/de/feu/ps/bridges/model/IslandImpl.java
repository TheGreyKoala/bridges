package de.feu.ps.bridges.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class IslandImpl implements Island {

    private final int column;
    private final int row;
    private int requiredBridges;
    private final Set<Bridge> bridges;

    private Island northNeighbour;
    private Island eastNeighbour;
    private Island southNeighbour;
    private Island westNeighbour;

    public IslandImpl(final int column, final int row, final int requiredBridges) {

        // TODO: Parameter validation

        this.column = column;
        this.row = row;
        this.requiredBridges = requiredBridges;
        bridges = new HashSet<>(requiredBridges);
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

    @Override
    public Set<Bridge> getBridges() {
        return new HashSet<>(bridges);
    }

    @Override
    public boolean hasNorthNeighbour() {
        return getNorthNeighbour() != null;
    }

    @Override
    public boolean hasEastNeighbour() {
        return getEastNeighbour() != null;
    }

    @Override
    public boolean hasSouthNeighbour() {
        return getSouthNeighbour() != null;
    }

    @Override
    public boolean hasWestNeighbour() {
        return getWestNeighbour() != null;
    }

    @Override
    public Position getPosition() {
        return new Position(column, row);
    }

    @Override
    public int getDistanceToNorthNeighbour() {
        if (hasNorthNeighbour()) {
            return row - northNeighbour.getRow();
        } else {
            throw new UnsupportedOperationException("Island has no north neighbour");
        }
    }

    @Override
    public int getDistanceToEastNeighbour() {
        if (hasEastNeighbour()) {
            return eastNeighbour.getColumn() - column;
        } else {
            throw new UnsupportedOperationException("Island has no east neighbour");
        }
    }

    @Override
    public int getDistanceToSouthNeighbour() {
        if (hasSouthNeighbour()) {
            return southNeighbour.getRow() - row;
        } else {
            throw new UnsupportedOperationException("Island has no south neighbour");
        }
    }

    @Override
    public int getDistanceToWestNeighbour() {
        if (hasWestNeighbour()) {
            return column - westNeighbour.getColumn();
        } else {
            throw new UnsupportedOperationException("Island has no west neighbour");
        }
    }

    @Override
    public boolean isBridgedToNeighbour(Direction direction) {
        switch (direction) {
            case NORTH:
                return hasNorthNeighbour() && bridges.stream().anyMatch(bridge -> bridge.getConnectedIslands().contains(northNeighbour));
            case EAST:
                return hasEastNeighbour() && bridges.stream().anyMatch(bridge -> bridge.getConnectedIslands().contains(eastNeighbour));
            case SOUTH:
                return hasSouthNeighbour() && bridges.stream().anyMatch(bridge -> bridge.getConnectedIslands().contains(southNeighbour));
            case WEST:
                return hasWestNeighbour() && bridges.stream().anyMatch(bridge -> bridge.getConnectedIslands().contains(westNeighbour));
            default:
                return false;
        }
    }

    @Override
    public void removeAllBridges() {
        bridges.clear();
    }

    @Override
    public void setRequiredBridges(final int requiredBridges) {
        this.requiredBridges = requiredBridges;
    }
}
