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
     * @param doubleBridge indicates whether destinations for a single or a double bridge should be returned.
     * @return all valid destinations for a bridge starting at the given island.
     * @throws NullPointerException if island is null.
     */
    Set<Island> getValidBridgeDestinations(final Island island, final boolean doubleBridge);

    /**
     * Indicates whether a bridge in the given direction and starting at the given island would be valid.
     * @param island Island to start the bridge at.
     * @param direction Direction to where to build the bridge.
     * @param doubleBridge Indicates whether the move should add a single or a double bridge.
     * @return true, if the bridge would be valid, false otherwise.
     * @throws NullPointerException if island or direction is null.
     */
    boolean isValidMove(Island island, Direction direction, boolean doubleBridge);

    /**
     * Indicates whether a bridge between the given islands would be valid.
     * @param island1 Island to start the bridge at.
     * @param island2 Island to stop the bridge at.
     * @param doubleBridge Indicates whether the move should add a single or a double bridge.
     * @return true, if the bridge would be valid, false otherwise.
     * @throws NullPointerException if island1 or islands is null.
     */
    boolean isValidMove(Island island1, Island island2, boolean doubleBridge);
}
