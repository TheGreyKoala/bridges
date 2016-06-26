package de.feu.ps.bridges.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tim Gremplewski
 */
class DefaultIsland implements ModifiableIsland {

    private final int column;
    private final int row;
    private int requiredBridges;
    private final Set<Bridge> bridges;

    private Island northNeighbour;
    private Island eastNeighbour;
    private Island southNeighbour;
    private Island westNeighbour;

    DefaultIsland(final int column, final int row, final int requiredBridges) {

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
        return requiredBridges - getActualBridgesCount();
    }

    @Override
    public Optional<Island> getNorthNeighbour() {
        return Optional.ofNullable(northNeighbour);
    }

    @Override
    public Optional<Island> getEastNeighbour() {
        return Optional.ofNullable(eastNeighbour);
    }

    @Override
    public Optional<Island> getSouthNeighbour() {
        return Optional.ofNullable(southNeighbour);
    }

    @Override
    public Optional<Island> getWestNeighbour() {
        return Optional.ofNullable(westNeighbour);
    }

    @Override
    public Optional<Island> getNeighbour(Direction direction) {
        switch (direction) {
            case NORTH:
                return getNorthNeighbour();
            case EAST:
                return getEastNeighbour();
            case SOUTH:
                return getSouthNeighbour();
            case WEST:
                return getWestNeighbour();
            default:
                return Optional.empty();
        }
    }

    @Override
    public IslandStatus getStatus() {
        //TODO: Implement de.feu.ps.bridges.model.Island.getStatus
        return null;
    }

    @Override
    public void addBridge(Bridge bridge) {
        //TODO Parameter validation and reject if already enough bridges
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

    @Override
    public Set<Island> getNeighbours() {
        return Arrays.stream(Direction.values())
            .map(this::getNeighbour)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean hasNeighbour(Direction direction) {
        switch (direction) {
            case NORTH:
                return hasNorthNeighbour();
            case EAST:
                return hasEastNeighbour();
            case SOUTH:
                return hasSouthNeighbour();
            case WEST:
                return hasWestNeighbour();
            default:
                return false;
        }
    }

    @Override
    public boolean isBridgedTo(Island island) {
        return bridges.stream().anyMatch(bridge -> bridge.getConnectedIslands().contains(island));
    }

    @Override
    public Optional<Bridge> getBridgeTo(Island island) {
        return bridges.stream().filter(bridge -> bridge.getConnectedIslands().contains(island)).findFirst();
    }

    @Override
    public int getActualBridgesCount() {
        int doubleBridges = (int) bridges.stream().filter(Bridge::isDoubleBridge).count();
        int singleBridges = bridges.size() - doubleBridges;
        return singleBridges + doubleBridges * 2;
    }

    @Override
    public Set<Island> getBridgedNeighbours() {
        return Arrays.stream(Direction.values())
                .filter(this::isBridgedToNeighbour)
                .map(this::getNeighbour)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge);
    }

    @Override
    public String toString() {
        return "DefaultIsland{" +
                "column=" + column +
                ", row=" + row +
                '}';
    }
}
