package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;

import java.util.Objects;
import java.util.Set;

/**
 * Default implementation of {@link PuzzleAnalyser}.
 * @author Tim Gremplewski
 */
class DefaultPuzzleAnalyser implements PuzzleAnalyser {

    private final MoveAnalyser moveAnalyser;
    private final StatusAnalyser statusAnalyser;
    private final IslandPositionAnalyser islandPositionAnalyser;

    /**
     * Creates a new instance.
     */
    DefaultPuzzleAnalyser(final MoveAnalyser moveAnalyser, final StatusAnalyser statusAnalyser, final IslandPositionAnalyser islandPositionAnalyser) {
        this.moveAnalyser = Objects.requireNonNull(moveAnalyser, "Parameter 'moveAnalyser' must not be null");
        this.statusAnalyser = Objects.requireNonNull(statusAnalyser, "Parameter 'statusAnalyser' must not be null.");
        this.islandPositionAnalyser = Objects.requireNonNull(islandPositionAnalyser, "Parameter 'islandPositionAnalyser' must not be null.");
    }

    @Override
    public PuzzleStatus getStatus() {
        return statusAnalyser.getStatus();
    }

    @Override
    public Set<Island> getUnfinishedIslands() {
        return moveAnalyser.getUnfinishedIslands();
    }

    @Override
    public Set<Island> getValidBridgeDestinations(final Island island, final boolean doubleBridge) {
        return moveAnalyser.getValidBridgeDestinations(island, doubleBridge);
    }

    @Override
    public boolean isAnyBridgeCrossing(final Position start, final Position end) {
        return moveAnalyser.isAnyBridgeCrossing(start, end);
    }

    @Override
    public boolean isValidIslandPosition(final Position position) {
        return islandPositionAnalyser.isValidIslandPosition(position);
    }

    @Override
    public boolean isValidMove(final Island island, final Direction direction, final boolean doubleBridge) {
        return moveAnalyser.isValidMove(island, direction, doubleBridge);
    }

    @Override
    public boolean isValidMove(final Island island1, final Island island2, final boolean doubleBridge) {
        return moveAnalyser.isValidMove(island1, island2, doubleBridge);
    }
}
