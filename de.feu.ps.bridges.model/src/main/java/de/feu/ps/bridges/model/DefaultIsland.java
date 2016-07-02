package de.feu.ps.bridges.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tim Gremplewski
 */
class DefaultIsland implements ModifiableIsland {

    private final int column;
    private final int row;
    private int requiredBridges;
    private final Set<Bridge> bridges;
    private Map<Direction, Island> neighbours;

    DefaultIsland(final int column, final int row, final int requiredBridges) {

        // TODO: Parameter validation

        this.column = column;
        this.row = row;
        this.requiredBridges = requiredBridges;
        bridges = new HashSet<>(requiredBridges);
        neighbours = new HashMap<>(4);
    }

    @Override
    public void addBridge(Bridge bridge) {
        //TODO Parameter validation and reject if already enough bridges
        bridges.add(bridge);
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
    public Set<Bridge> getBridges() {
        return new HashSet<>(bridges);
    }

    @Override
    public Optional<Bridge> getBridgeTo(Island island) {
        return bridges.stream().filter(bridge -> bridge.getBridgedIslands().contains(island)).findFirst();
    }

    @Override
    public int getColumnIndex() {
        return column;
    }

    @Override
    public int getDistanceToNeighbour(final Direction direction) {
        final Optional<Island> optionalNeighbour = getNeighbour(direction);

        if (optionalNeighbour.isPresent()) {
            final Island neighbour = optionalNeighbour.get();

            switch (direction) {
                case NORTH:
                    return row - neighbour.getRowIndex();
                case EAST:
                    return neighbour.getColumnIndex() - column;
                case SOUTH:
                    return neighbour.getRowIndex() - row;
                case WEST:
                    return column - neighbour.getColumnIndex();
                default:
                    throw new UnsupportedOperationException("Unsupported direction: " + direction.name());
            }
        } else {
            throw new UnsupportedOperationException("Island has no optionalNeighbour in this direction: " + direction.name());
        }
    }

    @Override
    public Optional<Island> getNeighbour(Direction direction) {
        return Optional.ofNullable(neighbours.get(direction));
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
    public Position getPosition() {
        return new Position(column, row);
    }

    @Override
    public int getRemainingBridges() {
        return requiredBridges - getActualBridgesCount();
    }

    @Override
    public int getRequiredBridges() {
        return requiredBridges;
    }

    @Override
    public int getRowIndex() {
        return row;
    }

    @Override
    public boolean isBridgedToNeighbour(Direction direction) {
        final Optional<Island> optionalNeighbour = getNeighbour(direction);

        if (optionalNeighbour.isPresent()) {
            final Island neighbour = optionalNeighbour.get();
            return bridges.stream().anyMatch(bridge -> bridge.getBridgedIslands().contains(neighbour));
        }
        return false;
    }

    @Override
    public void removeAllBridges() {
        bridges.clear();
    }

    @Override
    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge);
    }

    @Override
    public void setNeighbour(final Island neighbour, final Direction direction) {
        neighbours.put(direction, neighbour);
    }

    @Override
    public void setRequiredBridges(final int requiredBridges) {
        this.requiredBridges = requiredBridges;
    }
}
