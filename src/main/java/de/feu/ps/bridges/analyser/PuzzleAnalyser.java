package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;

import java.util.List;
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
     * Get all islands that don't have the required amount of bridges yet.
     * @return all islands that don't have the required amount of bridges yet.
     */
    Set<Island> getUnfinishedIslands();

    /**
     * Get all safe destinations for a bridge starting at the given island.
     * @param island Island to start from.
     * @return all safe destinations for a bridge starting at the given island.
     * @throws NullPointerException if island is null.
     */
    Set<Island> getSafeBridgeDestinations(Island island);

    /**
     * Returns a list of valid positions for new neighbours of the given Island in the given direction.
     * @param island Island to check
     * @param direction Direction to check
     * @return a list of valid positions for new neighbours of the given Island in the given direction.
     */
    List<Position> getValidNeighbourPositions(Island island, Direction direction);

    /**
     * Indicates if any bridge crosses the path from <code>start</code> to <code>end</code>.
     * @param start Start position of the path to be checked for crossing bridges.
     * @param end End position of the path to be checked for crossing bridges.
     * @return true, if a bridge is crossing the path from <code>start</code> to <code>end</code>, false otherwise.
     * @throws NullPointerException if <code>start</code> or <code>end</code> is null.
     */
    boolean isAnyBridgeCrossing(Position start, Position end);

    /**
     * Indicates whether there is enough space in the puzzle to add a neighbour to the given island in the given direction.
     * @param island Island to check
     * @param direction Direction to check
     * @return true, if there is enough space in the puzzle to add a neighbour to the given island in the given direction, false otherwise
     */
    boolean isEnoughSpaceToAddNeighbour(Island island, Direction direction);

    /**
     * Indicates whether it would be valid to build an island at the given position.
     * @param position Position to check
     * @return true, if an island at the given position would be valid, false otherwise.
     */
    boolean isValidIslandPosition(Position position);

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
