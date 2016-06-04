package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
public class PuzzleBuilder {


    private int islandsCount;

    public PuzzleBuilder setPuzzleDimensions(final int columns, final int rows) {
        return this;
    }

    public PuzzleBuilder addIsland(final int column, final int row, final int requiredBridges) {
        // TODO implement addIsland
        return this;
    }

    public PuzzleBuilder addBridge(final Island islandA, final Island islandB) {
        // TODO implement addBridge
        return this;
    }

    public Puzzle getResult() {
        return new PuzzleImpl(12, 23);
    }

    public void setIslandsCount(final int islandsCount) {
        this.islandsCount = islandsCount;
    }

    public int getIslandsCount() {
        return islandsCount;
    }
}
