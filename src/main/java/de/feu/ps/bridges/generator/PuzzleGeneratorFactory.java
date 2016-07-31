package de.feu.ps.bridges.generator;

/**
 * Factory class that creates a new {@link PuzzleGenerator}.
 * @author Tim Gremplewski
 */
public final class PuzzleGeneratorFactory {

    private static final RandomUtil RANDOM_UTIL = new RandomUtil();

    private PuzzleGeneratorFactory() {
    }

    /**
     * Creates a new {@link PuzzleGenerator} that generates puzzles with a random
     * amount of columns, rows and islands.
     * @return a new {@link PuzzleGenerator}.
     */
    public static PuzzleGenerator createPuzzleGenerator() {
        final int[] dimensions = RANDOM_UTIL.getRandom().ints(2, 4, 25).toArray();
        return createPuzzleGenerator(dimensions[0], dimensions[1]);
    }

    /**
     * Creates a new {@link PuzzleGenerator} that generates puzzles with a defined
     * amount of columns and rows but with a random amount of islands.
     * @param columns Number of columns the {@link PuzzleGenerator} should use.
     * @param rows Number of rows the {@link PuzzleGenerator} should use.
     * @return a new {@link PuzzleGenerator}.
     */
    public static PuzzleGenerator createPuzzleGenerator(final int columns, final int rows) {
        final int islands = randomIslands(columns, rows);
        validateParameters(columns, rows, islands);
        return createPuzzleGenerator(columns, rows, islands);
    }

    private static int randomIslands(final int columns, final int rows) {
        int lowerLimit = Math.min(columns, rows);
        int upperLimit = columns * rows / 5;

        // Lower limit might be less than upper limit in some cases.
        // Swap lower and upper limits. See newsgroup for this valid workaround
        lowerLimit = Math.min(lowerLimit, upperLimit);
        upperLimit = Math.max(lowerLimit, upperLimit);

        int islands = RANDOM_UTIL.randomIntBetween(lowerLimit, upperLimit);
        if (islands < 2) {
            islands = 2;
        }

        return islands;
    }

    /**
     * Creates a new {@link PuzzleGenerator} that generates puzzles with a defined
     * amount of columns, rows and islands.
     * @param columns Number of columns the {@link PuzzleGenerator} should use.
     * @param rows Number of rows the {@link PuzzleGenerator} should use.
     * @param islands Number of islands the {@link PuzzleGenerator} should use.
     * @return a new {@link PuzzleGenerator}.
     */
    public static PuzzleGenerator createPuzzleGenerator(final int columns, final int rows, final int islands) {
        validateParameters(columns, rows, islands);
        return new DefaultPuzzleGenerator(columns, rows, islands);
    }

    private static void validateParameters(final int columns, final int rows, final int islands) {
        if (columns < 4 || columns > 25) {
            throw new IllegalArgumentException("Parameter 'columns' must be between 4 and 25.");
        }

        if (rows < 4 || rows > 25) {
            throw new IllegalArgumentException("Parameter 'rows' must be between 4 and 25.");
        }

        if (islands < 2 || islands > columns * rows * 0.2) {
            throw new IllegalArgumentException("Parameter 'islands' must be between 2 and columns * rows * 0.2");
        }
    }
}
