package de.feu.ps.bridges.model;

import java.util.Optional;
import java.util.Set;

/**
 * Interface for an island in the puzzle.
 * @author Tim Gremplewski
 */
public interface Island {

    /**
     * Get the actual amount of bridges of this island.
     * Despite {@link #getBridges()}.size() this method takes into account
     * that {@link Bridge}s can be double bridges.
     *
     * @return the actual amount of bridges of this island.
     */
    int getActualBridgesCount();

    /**
     * Get all bridged neighbours of this island.
     * @return all bridged neighbours of this island.
     */
    Set<Island> getBridgedNeighbours();

    /**
     * Get all {@link Bridge}es of this island.
     * @return all {@link Bridge}es of this island.
     */
    Set<Bridge> getBridges();

    /**
     * Get the {@link Bridge} to the given island.
     * A {@link Bridge} might not exist.
     *
     * @param island Another possibly bridged island.
     * @return {@link Optional} containing the {@link Bridge} to the given island.
     * @throws NullPointerException if island is null.
     */
    Optional<Bridge> getBridgeTo(Island island);

    /**
     * Get the distance to the neighbour in the given {@link Direction}.
     * @param direction {@link Direction} to check.
     * @return distance to the neighbour in the given {@link Direction}.
     * @throws UnsupportedOperationException if this island does not have a neighbour in the given {@link Direction}.
     */
    int getDistanceToNeighbour(Direction direction);

    /**
     * Get the neighbour in the given {@link Direction} of this island.
     * A neighbour in the given {@link Direction} might not exist.
     *
     * @param direction {@link Direction} to check.
     * @return {@link Optional} containing the neighbour in the given {@link Direction}, if one exist.
     * @throws NullPointerException if direction is null.
     */
    Optional<Island> getNeighbour(Direction direction);

    /**
     * Get all neighbours of this island.
     * @return all neighbours of this island.
     */
    Set<Island> getNeighbours();

    /**
     * Get the position of this island.
     * @return the position of this island.
     */
    Position getPosition();

    /**
     * Get the amount of remaining {@link Bridge}es of this island.
     * @return the amount of remaining {@link Bridge}es of this island.
     */
    int getRemainingBridges();

    /**
     * Get the amount of required {@link Bridge}es of this island.
     * @return the amount of required {@link Bridge}es of this island.
     */
    int getRequiredBridges();

    /**
     * Indicates whether this islands is bridged to its neighbour in the given {@link Direction}.
     *
     * @param direction {@link Direction} to check.
     * @return <code>true</code> if this island has a neighbour in the given {@link Direction} and is bridged to it,
     *  <code>false</code> otherwise
     * @throws NullPointerException if direction is null.
     */
    boolean isBridgedToNeighbour(Direction direction);
}
