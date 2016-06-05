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
    private List<Island> islands;
    private List<Bridge> bridges;

    public PuzzleBuilder() {
        islands = new ArrayList<>();
        bridges = new ArrayList<>();
    }

    public PuzzleBuilder setPuzzleDimensions(final int columns, final int rows) {

        // TODO validate dimension

        this.columns = columns;
        this.rows = rows;
        return this;
    }

    public PuzzleBuilder addIsland(final int column, final int row, final int requiredBridges) {
        if (islands.size() == islandsCount) {
            // TODO throw error if already all islands
        }

        if (column < 0 || column > columns) {
            // TODO throw error if illegal column
        }

        if (row < 0 || row > rows) {
            // TODO throw error if illegal row
        }

        final Island island = new IslandImpl(column, row, requiredBridges);
        islands.add(island);
        return this;
    }

    public Puzzle getResult() {
        PuzzleImpl puzzle = new PuzzleImpl(columns, rows);
        islands.forEach(puzzle::addIsland);
        bridges.forEach(puzzle::addBridge);
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

        Island islandA = islands.get(islandAIndex);
        Island islandB = islands.get(islandBIndex);

        Bridge bridge = new BridgeImpl(islandA, islandB);
        bridges.add(bridge);
        if (doubleBridge) {
            Bridge doubledBridge = new BridgeImpl(islandA, islandB);
            bridges.add(doubledBridge);
        }
        return this;
    }
}
