package de.feu.ps.bridges.generator;

/**
 * Factory class that creates a new {@link PuzzleGenerator}.
 * @author Tim Gremplewski
 */
public class PuzzleGeneratorFactory {

    private static RandomUtil randomUtil = new RandomUtil();

    private PuzzleGeneratorFactory() {
    }

    /**
     * Creates a new {@link PuzzleGenerator} that generates puzzles with a random
     * amount of columns, rows and islands.
     * @return a new {@link PuzzleGenerator}.
     */
    public static PuzzleGenerator createPuzzleGenerator() {
        final int[] dimensions = randomUtil.getRandom().ints(2, 4, 25).toArray();
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
        if (columns * rows < 3) {
            // With only two fields, there would not be an empty cell between two islands.
            throw new IllegalArgumentException("Puzzle must consist of at least 3 fields.");
        }
        return createPuzzleGenerator(columns, rows, randomIslands(columns, rows));
    }

    private static int randomIslands(final int columns, final int rows) {
        int lowerLimit = Math.min(columns, rows);
        int upperLimit = columns * rows / 5;

        // Lower limit might be less than upper limit in some cases.
        // Swap lower and upper limits. See newsgroup for this valid workaround
        lowerLimit = Math.min(lowerLimit, upperLimit);
        upperLimit = Math.max(lowerLimit, upperLimit);

        int islands = randomUtil.randomIntBetween(lowerLimit, upperLimit);
        if (islands < 2) {
            islands = 2;
        }

        return islands;
    }

    /**
     * Creates a new {@link PuzzleGenerator} that generates puzzles with a defiend
     * amount of columns, rows and islands.
     * @param columns Number of columns the {@link PuzzleGenerator} should use.
     * @param rows Number of rows the {@link PuzzleGenerator} should use.
     * @param islands Number of islands the {@link PuzzleGenerator} should use.
     * @return a new {@link PuzzleGenerator}.
     */
    public static PuzzleGenerator createPuzzleGenerator(final int columns, final int rows, final int islands) {
        return new DefaultPuzzleGenerator(columns, rows, islands);
    }
}
