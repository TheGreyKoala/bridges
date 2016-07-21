package de.feu.ps.bridges.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Helper class to construct a new puzzle.
 * @author Tim Gremplewski
 */
public class PuzzleBuilder {

    private int islandsCount;
    private final int columns;
    private final int rows;
    private List<ModifiableIsland> islands;
    private ModifiablePuzzle puzzle;

    private PuzzleBuilder(final int columns, final int rows, final int islandsCount) {
        if (columns < 1) {
            throw new IllegalArgumentException("Parameter 'columns' must not be less than 1.");
        }

        if (rows < 1) {
            throw new IllegalArgumentException("Parameter 'rows' must not be less than 1.");
        }

        if (islandsCount < 2 || islandsCount > columns * rows * 0.2) {
            throw new IllegalArgumentException("Parameter 'islands' must be between 2 and columns * rows * 0.2.");
        }

        this.columns = columns;
        this.rows = rows;
        this.islandsCount = islandsCount;
        islands = new ArrayList<>(islandsCount);
        puzzle = new DefaultPuzzle(columns, rows);
    }

    /**
     * Creates a new instance.
     * @param columns Amount of columns of the puzzle to be build.
     * @param rows Amount of rows of the puzzle to be build.
     * @param islands Amount of islands of the puzzel to be build.
     * @return a new PuzzleBuilder instance.
     * @throws IllegalArgumentException if columns, rows or islands is less than 1.
     */
    public static PuzzleBuilder createBuilder(final int columns, final int rows, final int islands) {
        return new PuzzleBuilder(columns, rows, islands);
    }

    /**
     * Add a new bridge to the puzzle.
     * @param island1Index Index of an island to be bridged.
     * @param island2Index Index of another island to be bridged.
     * @param doubleBridge indicates whether the new bridge should be a double bridge.
     */
    public void addBridge(int island1Index, int island2Index, boolean doubleBridge) {
        final Island island1 = islands.get(island1Index);
        final Island island2 = islands.get(island2Index);
        addBridge(island1, island2, doubleBridge);
    }

    /**
     * Add a new bridge between the given islands.
     * @param island1 Island to be bridged.
     * @param island2 Another island to be bridged.
     * @param doubleBridge indicates whether the new bridge should be a double bridge.
     */
    public void addBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        if (isAnyBridgeCrossing(island1.getPosition(), island2.getPosition())) {
            throw new IllegalStateException("Cannot build bridge. It would cross another bridge.");
        }

        puzzle.buildBridge(island1, island2, doubleBridge);
    }

    /**
     * Add a new island to the puzzle.
     * @param position Position where the island should be created.
     * @param requiredBridges Amount of required bridged of the new island.
     * @return the newly created Island
     * @throws IllegalStateException if the puzzle already has enough islands.
     */
    public Island addIsland(final Position position, final int requiredBridges) {
        if (islands.size() == islandsCount) {
            throw new IllegalStateException("The puzzle already has enough islands.");
        }
        validatePosition(position);

        if (adjacentIslandAt(position)) {
            throw new IllegalStateException("Cannot add island. There is an adjacent island.");
        }

        // TODO This cast is ugly
        final ModifiableIsland island = ((ModifiableIsland) puzzle.buildIsland(position, requiredBridges));
        islands.add(island);
        return island;
    }

    private void validatePosition(Position position) {
        Objects.requireNonNull(position, "Parameter 'position' must not be null.");

        if (position.getColumn() >= columns) {
            throw new IllegalArgumentException("The puzzle does not have this column: " + position.getColumn());
        }

        if (position.getRow() >= rows) {
            throw new IllegalArgumentException("The puzzle does not have this row: " + position.getRow());
        }
    }

    /**
     * Indicates whether the given position has any adjacent islands.
     * @param position Position to check.
     * @return true, if the position has any adjacent islands, false otherwise.
     */
    public boolean adjacentIslandAt(final Position position) {
        validatePosition(position);

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

    /**
     * Get the amount of islands of the puzzle.
     * @return the amount of islands of the puzzle.
     */
    public int getIslandsCount() {
        return islandsCount;
    }

    /**
     * Get the created puzzle.
     * @return the created puzzle.
     */
    public Puzzle getResult() {
        return puzzle;
    }

    /**
     * Indicates whether any bridge is crossing the path between <code>start</code> and <code>end</code>.
     * @param start Start position of the path to be checked.
     * @param end End position of the path to be checked.
     * @return true, if any bridge is crossing the path between <code>start</code> and <code>end</code>.
     */
    public boolean isAnyBridgeCrossing(final Position start, final Position end) {
        validatePosition(start);
        validatePosition(end);
        return puzzle.isAnyBridgeCrossing(start, end);
    }

    /**
     * For each island, set the amount of required bridges to the current amount of existing bridges.
     */
    public void setRequiredBridgesToCurrentCountOfBridges() {
        islands.forEach(island -> island.setRequiredBridges(island.getActualBridgesCount()));
    }
}
