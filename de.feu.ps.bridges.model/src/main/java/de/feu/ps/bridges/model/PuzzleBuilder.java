package de.feu.ps.bridges.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Gremplewski
 */
public class PuzzleBuilder {

    private int islandsCount;
    private int columns;
    private int rows;
    private List<ModifiableIsland> islands;
    private List<Bridge> bridges;
    private ModifiablePuzzle puzzle;

    public PuzzleBuilder() {
        islands = new ArrayList<>();
        bridges = new ArrayList<>();
    }

    public PuzzleBuilder setPuzzleDimensions(final int columns, final int rows) {

        // TODO validate dimension

        this.columns = columns;
        this.rows = rows;
        puzzle = new DefaultPuzzle(columns, rows);

        return this;
    }

    public Island addIsland(final Position position, final int requiredBridges) {
        final int column = position.getColumn();
        final int row = position.getRow();

        if (islands.size() == islandsCount) {
            // TODO throw error if already all islands
        }

        if (column < 0 || column >= columns) {
            // TODO throw error if illegal column
        }

        if (row < 0 || row >= rows) {
            // TODO throw error if illegal row
        }

        // TODO This cast is ugly
        final ModifiableIsland island = ((ModifiableIsland) puzzle.buildIsland(position, requiredBridges));
        islands.add(island);
        return island;
    }

    public Puzzle getResult() {

        return puzzle;
    }

    public PuzzleBuilder setIslandsCount(final int islandsCount) {

        // TODO validate size

        this.islandsCount = islandsCount;
        return this;
    }

    public int getIslandsCount() {
        return islandsCount;
    }

    public PuzzleBuilder addBridge(int islandAIndex, int islandBIndex, boolean doubleBridge) {
        // TODO: addBridge should only take cols and rows of both islands to be more independent (index comes from serialization)

        Island island1 = islands.get(islandAIndex);
        Island island2 = islands.get(islandBIndex);
        return addBridge(island1, island2, doubleBridge);
    }

    public PuzzleBuilder addBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        // TODO: Validate islands
        Bridge bridge = puzzle.buildBridge(island1, island2, doubleBridge);
        bridges.add(bridge);
        return this;
    }

    public boolean adjacentIslandAt(final Position position) {
        final int column = position.getColumn();
        final int row = position.getRow();

        return islands.stream().anyMatch(island -> {
            final int islandColumn = island.getPosition().getColumn();
            final int islandRow = island.getPosition().getRow();

            return islandColumn == column && islandRow == row - 1 // North
                || islandColumn == column + 1 && islandRow == row // East
                || islandColumn == column && islandRow == row + 1 // South
                || islandColumn == column - 1 && islandRow == row; // West
        });
    }

    public boolean isAnyBridgeCrossing(final Position otherBridgeStart, final Position otherBridgeEnd) {
        return puzzle.isAnyBridgeCrossing(otherBridgeStart, otherBridgeEnd);
    }

    public void clearBridges() {
        puzzle.removeAllBridges();
        bridges.clear();
    }

    public void setRequiredBridgesToCurrentCountOfBridges() {
        islands.forEach(island -> island.setRequiredBridges(island.getActualBridgesCount()));
    }
}
