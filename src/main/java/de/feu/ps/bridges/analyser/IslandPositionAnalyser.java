package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.Puzzle;

/**
 * @author Tim Gremplewski
 */
class IslandPositionAnalyser {

    private final Puzzle puzzle;

    IslandPositionAnalyser(final Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    boolean isValidIslandPosition(final Position position) {
        return isInsidePuzzle(position) && !adjacentIslandAt(position);
    }

    private boolean isInsidePuzzle(final Position position) {
        return position.getColumn() < puzzle.getColumnsCount()
                && position.getRow() < puzzle.getRowsCount();
    }

    private boolean adjacentIslandAt(final Position position) {
        final int column = position.getColumn();
        final int row = position.getRow();

        return puzzle.getIslands().stream().anyMatch(island -> {
            final int islandColumn = island.getPosition().getColumn();
            final int islandRow = island.getPosition().getRow();

            return islandColumn == column && islandRow == row - 1 // North
                    || islandColumn == column + 1 && islandRow == row // East
                    || islandColumn == column && islandRow == row + 1 // South
                    || islandColumn == column - 1 && islandRow == row; // West
        });
    }
}
