package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;

import java.util.Set;

/**
 * Interface that defines means to analyse a puzzle.
 * @author Tim Gremplewski
 */
public interface PuzzleAnalyser {

    /**
     * Get the current status of the puzzle.
     * @return the current status of the puzzle.
     */
    PuzzleStatus getStatus();

    /**
     * Get all valid destinations for a bridge starting at the given island.
     * @param island Island to start from.
     * @return all valid destinations for a bridge starting at the given island.
     * @throws NullPointerException if island is null.
     */
    Set<Island> getValidBridgeDestinations(final Island island, final boolean doubleBridge);

    /**
     * Indicates whether a bridge in the given direction and starting at the given island would be valid.
     * @param island Island to start the bridge at.
     * @param direction Direction to where to build the bridge.
     * @return true, if the bridge would be valid, false otherwise.
     * @throws NullPointerException if island or direction is null.
     */
    boolean isValidMove(Island island, Direction direction, boolean doubleBridge);

    boolean isValidMove(Island island1, Island island2, boolean doubleBridge);
}
