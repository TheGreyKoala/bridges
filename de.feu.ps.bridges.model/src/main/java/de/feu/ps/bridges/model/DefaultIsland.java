package de.feu.ps.bridges.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link Island}.
 *
 * @author Tim Gremplewski
 */
class DefaultIsland implements ModifiableIsland {

    private final Position position;
    private int requiredBridges;
    private final Set<Bridge> bridges;
    private final EnumMap<Direction, Island> neighbours;

    /**
     * Creates a new instance.
     *
     * @param position Position of the new island.
     * @param requiredBridges Amount of required bridges.
     * @throws NullPointerException if position is null.
     * @throws IllegalArgumentException if one of the following is true
     *  <ul>
     *      <li>position.getColumn is less than 0</li>
     *      <li>position.getRow is less then 0</li>
     *      <li>requiredBridges is less than 1</li>
     *  </ul>
     */
    DefaultIsland(final Position position, final int requiredBridges) {
        this.position = Objects.requireNonNull(position, "Parameter 'position' must not be null.");

        if (position.getColumn() < 0) {
            throw new IllegalArgumentException("Column must not be less than 0.");
        }

        if (position.getRow() < 0) {
            throw new IllegalArgumentException("Row must not be less than 0.");
        }

        if (requiredBridges < 1) {
            throw new IllegalArgumentException("Parameter 'requiredBridged' must not be less than 1.");
        }

        this.requiredBridges = requiredBridges;
        bridges = new HashSet<>(requiredBridges);
        neighbours = new EnumMap<>(Direction.class);
    }

    @Override
    public void addBridge(final Bridge bridge) {
        Objects.requireNonNull(bridge, "Parameter 'bridge' must not be null.");

        if (getActualBridgesCount() == requiredBridges) {
            throw new IllegalStateException("This island does not require any more bridges.");
        }

        if (!bridge.getBridgedIslands().contains(this)) {
            throw new IllegalArgumentException("The given bridge does not bridge this island.");
        }

        bridges.add(bridge);
    }

    @Override
    public Set<Island> getBridgedNeighbours() {
        return neighbours.entrySet().stream()
                .filter(directionIslandEntry -> isBridgedToNeighbour(directionIslandEntry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isBridgedToNeighbour(final Direction direction) {
        Objects.requireNonNull(direction, "Parameter 'direction' must not be null.");
        final Optional<Island> optionalNeighbour = getNeighbour(direction);

        if (optionalNeighbour.isPresent()) {
            final Island neighbour = optionalNeighbour.get();
            return bridges.stream().anyMatch(bridge -> bridge.getBridgedIslands().contains(neighbour));
        }
        return false;
    }

    @Override
    public Optional<Island> getNeighbour(final Direction direction) {
        Objects.requireNonNull(direction, "Parameter 'direction' must not be null.");
        return Optional.ofNullable(neighbours.get(direction));
    }

    @Override
    public Set<Bridge> getBridges() {
        return new HashSet<>(bridges);
    }

    @Override
    public Optional<Bridge> getBridgeTo(final Island island) {
        Objects.requireNonNull(island, "Parameter 'island' must not be null.");
        return bridges.stream().filter(bridge -> bridge.getBridgedIslands().contains(island)).findFirst();
    }

    @Override
    public int getDistanceToNeighbour(final Direction direction) {
        final Optional<Island> optionalNeighbour = getNeighbour(direction);

        if (optionalNeighbour.isPresent()) {
            final Island neighbour = optionalNeighbour.get();

            switch (direction) {
                case NORTH:
                    return position.getRow() - neighbour.getPosition().getRow();
                case EAST:
                    return neighbour.getPosition().getColumn() - position.getColumn();
                case SOUTH:
                    return neighbour.getPosition().getRow() - position.getRow();
                case WEST:
                    return position.getColumn() - neighbour.getPosition().getColumn();
                default:
                    throw new UnsupportedOperationException("Unsupported direction: " + direction.name());
            }
        } else {
            throw new UnsupportedOperationException("Island has no neighbour in this direction: " + direction.name());
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Set<Island> getNeighbours() {
        return new HashSet<>(neighbours.values());
    }

    @Override
    public int getRemainingBridges() {
        return requiredBridges - getActualBridgesCount();
    }

    @Override
    public int getActualBridgesCount() {
        final int doubleBridges = (int) bridges.stream().filter(Bridge::isDoubleBridge).count();
        final int singleBridges = bridges.size() - doubleBridges;
        return singleBridges + doubleBridges * 2;
    }

    @Override
    public int getRequiredBridges() {
        return requiredBridges;
    }

    @Override
    public void removeAllBridges() {
        bridges.clear();
    }

    @Override
    public void removeBridge(final Bridge bridge) {
        Objects.requireNonNull(bridge, "Parameter 'bridge' must not be null.");
        bridges.remove(bridge);
    }

    @Override
    public void setNeighbour(final Island neighbour, final Direction direction) {
        Objects.requireNonNull(neighbour, "Parameter 'neighbour' must not be null.");
        Objects.requireNonNull(direction, "Parameter 'direction' must not be null.");
        neighbours.put(direction, neighbour);
    }

    @Override
    public void setRequiredBridges(final int requiredBridges) {
        if (requiredBridges < 1) {
            throw new IllegalArgumentException("Parameter 'requiredBridges' must not be less than 1.");
        }
        this.requiredBridges = requiredBridges;
    }
}
