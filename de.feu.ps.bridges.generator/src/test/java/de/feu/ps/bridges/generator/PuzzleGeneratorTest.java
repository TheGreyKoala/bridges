package de.feu.ps.bridges.generator;

import de.feu.ps.bridges.model.Puzzle;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class PuzzleGeneratorTest {

    private static final int availableProcessors;
    private static final int loadPerProcessor;

    static {
        availableProcessors = Runtime.getRuntime().availableProcessors();
        loadPerProcessor = 128 / availableProcessors;
    }

    @DataPoints("validDimensions")
    public static int[] validDimensions() {
        return IntStream.range(4, 26).toArray();
    }

    @Test
    public void testGenerate() throws Exception {
        for (int i = 0; i < 1024; i++) {
            final Puzzle puzzle = PuzzleGenerator.generatePuzzle();
            assertNotNull("Puzzle is null.", puzzle);

            int columnsCount = puzzle.getColumnsCount();
            assertTrue("Puzzle has too few columns: " + columnsCount, columnsCount >= 4);
            assertTrue("Puzzle has too many columns: " + columnsCount, columnsCount <= 25);

            int rowsCount = puzzle.getRowsCount();
            assertTrue("Puzzle has too few rows: " + rowsCount, rowsCount >= 4);
            assertTrue("Puzzle has too many rows: " + rowsCount, rowsCount <= 25);

            assertValidNumberOfIslands(columnsCount, rowsCount, puzzle.getIslands().size());

            assertTrue("Puzzle already contains bridges: " + puzzle.getBridges().size(), puzzle.getBridges().isEmpty());
        }
    }

    @Theory
    public void testFixedDimensions(
        @FromDataPoints("validDimensions") final int columns,
        @FromDataPoints("validDimensions") final int rows) throws InterruptedException {

        ExecutorService executorService = Executors.newWorkStealingPool();

        for (int j = 0; j < availableProcessors; j++) {
            executorService.submit(() -> {
                for (int i = 0; i < loadPerProcessor; i++) {
                    final Puzzle puzzle = PuzzleGenerator.generatePuzzle(columns, rows);
                    assertValidPuzzle(columns, rows, puzzle);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);
    }

    @Theory
    public void testFixedDimensionsAndIslands(
        @FromDataPoints("validDimensions") final int columns,
        @FromDataPoints("validDimensions") final int rows) throws InterruptedException {

        int islandsLowerLimit = getIslandsLowerLimit(columns, rows);
        int islandsUpperLimit = getIslandsUpperLimit(columns, rows);
        int[] validIslands = IntStream.range(islandsLowerLimit, islandsUpperLimit).toArray();

        ExecutorService executorService = Executors.newWorkStealingPool();

        for (int p = 0; p < 4; p++) {
            executorService.submit(() -> {
                for (int islands : validIslands) {
                    Puzzle puzzle = PuzzleGenerator.generatePuzzle(columns, rows, islands);
                    assertValidPuzzle(columns, rows, puzzle);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);
    }

    private int getIslandsLowerLimit(final int columns, final int rows) {
        int lowerLimit = Math.min(columns, rows);
        int upperLimit = columns * rows / 5;
        return Math.min(lowerLimit, upperLimit);
    }

    private int getIslandsUpperLimit(final int columns, final int rows) {
        int lowerLimit = Math.min(columns, rows);
        int upperLimit = columns * rows / 5;
        return Math.max(lowerLimit, upperLimit);
    }

    private void assertValidPuzzle(int expectedColumns, int expectedRows, Puzzle puzzle) {
        assertNotNull("Puzzle is null.", puzzle);

        int columnsCount = puzzle.getColumnsCount();
        assertEquals("Puzzle has wrong number of columns.", expectedColumns, columnsCount);

        int rowsCount = puzzle.getRowsCount();
        assertEquals("Puzzle has wrong number of rows.", expectedRows, rowsCount);

        assertValidNumberOfIslands(expectedColumns, expectedRows, puzzle.getIslands().size());
        assertTrue("Puzzle already contains bridges: " + puzzle.getBridges().size(), puzzle.getBridges().isEmpty());
    }

    private void assertValidNumberOfIslands(final int columns, final int rows, final int actualIslands) {
        int islandsLowerLimit = getIslandsLowerLimit(columns, rows);
        int islandsUpperLimit = getIslandsUpperLimit(columns, rows);

        assertTrue("Puzzle has too few islands: [columns=" + columns + ", rows=" + rows + ", islands=" + actualIslands, actualIslands >= islandsLowerLimit);
        assertTrue("Puzzle has too many islands: [columns=" + columns + ", rows=" + rows + ", islands=" + actualIslands, actualIslands <= islandsUpperLimit);
    }
}