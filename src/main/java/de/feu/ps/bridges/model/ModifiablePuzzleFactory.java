package de.feu.ps.bridges.model;

/**
 * Factory class to create an instance of {@link ModifiablePuzzle}.
 * @author Tim Gremplewski
 */
final class ModifiablePuzzleFactory {

    private ModifiablePuzzleFactory() {
    }

    /**
     * Create a new {@link ModifiablePuzzle} object.
     * @param columns Amount of columns of the puzzle to be build.
     * @param rows Amount of rows of the puzzle to be build.
     * @return A new {@link ModifiablePuzzle} instance,
     * @throws IllegalArgumentException if columns or rows is less than 1.
     */
    static ModifiablePuzzle createPuzzle(final int columns, final int rows) {
        return new DefaultPuzzle(columns, rows);
    }
}
