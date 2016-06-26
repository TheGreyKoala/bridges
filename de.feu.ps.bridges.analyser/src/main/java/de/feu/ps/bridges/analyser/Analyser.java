package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;

import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public interface Analyser {
    PuzzleStatus getStatus();
    Set<Island> getValidBridgeDestinations(final Island island);
    boolean hasValidBridgeDestinations(final Island island);
    boolean isValidMove(Island island1, Island island2);
    boolean isValidMove(Island island, Direction direction);
}
