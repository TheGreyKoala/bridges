package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Default implementation of {@link PuzzleAnalyser}.
 * @author Tim Gremplewski
 */
class DefaultPuzzleAnalyser implements PuzzleAnalyser {

    private final MoveAnalyser moveAnalyser;
    private final StatusAnalyser statusAnalyser;
    private final NewIslandAnalyser newIslandAnalyser;

    /**
     * Creates a new instance.
     */
    DefaultPuzzleAnalyser(final MoveAnalyser moveAnalyser, final StatusAnalyser statusAnalyser, final NewIslandAnalyser newIslandAnalyser) {
        this.moveAnalyser = Objects.requireNonNull(moveAnalyser, "Parameter 'moveAnalyser' must not be null");
        this.statusAnalyser = Objects.requireNonNull(statusAnalyser, "Parameter 'statusAnalyser' must not be null.");
        this.newIslandAnalyser = Objects.requireNonNull(newIslandAnalyser, "Parameter 'newIslandAnalyser' must not be null.");
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
    public List<Position> getValidNeighbourPositions(final Island island, final Direction direction) {
        return newIslandAnalyser.getValidNeighbourPositions(island, direction);
    }

    @Override
    public Set<Island> getSafeBridgeDestinations(final Island island) {
        return moveAnalyser.getSafeBridgeDestinations(island);
    }

    @Override
    public boolean isAnyBridgeCrossing(final Position start, final Position end) {
        return moveAnalyser.isAnyBridgeCrossing(start, end);
    }

    @Override
    public boolean isEnoughSpaceToAddNeighbour(final Island island, final Direction direction) {
        return newIslandAnalyser.isEnoughSpaceToAddNeighbour(island, direction);
    }

    @Override
    public boolean isValidIslandPosition(final Position position) {
        return newIslandAnalyser.isValidIslandPosition(position);
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
