package de.feu.ps.bridges.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to construct a new puzzle.
 * @author Tim Gremplewski
 */
public class PuzzleBuilder {

    private int islandsCount;
    private List<ModifiableIsland> islands;
    private ModifiablePuzzle puzzle;

    private PuzzleBuilder(final int columns, final int rows, final int islandsCount) {
        this.islandsCount = islandsCount;
        islands = new ArrayList<>(islandsCount);
        puzzle = ModifiablePuzzleFactory.createPuzzle(columns, rows);
    }

    /**
     * Creates a new instance.
     * @param columns Amount of columns of the puzzle to be build.
     * @param rows Amount of rows of the puzzle to be build.
     * @param islands Amount of islands of the puzzle to be build.
     * @return a new PuzzleBuilder instance.
     * @throws IllegalArgumentException if columns, rows or islands is less than 1.
     */
    public static PuzzleBuilder createBuilder(final int columns, final int rows, final int islands) {
        return new PuzzleBuilder(columns, rows, islands);
    }

    /**
     * Add a new bridge between the given islands.
     * @param island1 Island to be bridged.
     * @param island2 Another island to be bridged.
     * @param doubleBridge indicates whether the new bridge should be a double bridge.
     */
    public void addBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        puzzle.buildBridge(island1, island2);
        if (doubleBridge) {
            puzzle.buildBridge(island1, island2);
        }
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

        // TODO This cast is ugly
        final ModifiableIsland island = ((ModifiableIsland) puzzle.buildIsland(position, requiredBridges));
        islands.add(island);
        return island;
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
     * For each island, set the amount of required bridges to the current amount of existing bridges.
     */
    public void setRequiredBridgesToCurrentCountOfBridges() {
        islands.forEach(island -> island.setRequiredBridges(island.getActualBridgesCount()));
    }
}
