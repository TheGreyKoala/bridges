package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import java.io.File;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that can be used to save the current status of a {@link Puzzle} in a file.
 * @author Tim Gremplewski
 */
public class Serializer {

    private Serializer() {
    }

    /**
     * Save the current status of the given puzzle in the given file.
     * @param puzzle Puzzle to be saved.
     * @param destination Destination file.
     * @throws NullPointerException if <code>puzzle</code> or <code>destination</code> is null.
     * @throws SerializationException if the puzzle could not be saved.
     */
    public static void savePuzzle(final Puzzle puzzle, final File destination) {
        Objects.requireNonNull(puzzle, "Parameter 'puzzle' must not be null.");
        Objects.requireNonNull(destination, "Parameter 'destination' must not be null.");

        if (destination.exists()) {
            throw new IllegalArgumentException("Destination file must not exists.");
        }

        final File destinationWithCorrectExtension = assureFileExtension(destination);

        try (final PrintWriter writer = new PrintWriter(destinationWithCorrectExtension)) {
            final List<Island> sortedIslands = sortIslands(puzzle.getIslands());
            writeFieldSection(writer, puzzle.getColumnsCount(), puzzle.getRowsCount(), sortedIslands.size());
            writer.println();
            writeIslandsSection(writer, sortedIslands);
            writer.println();

            Set<Bridge> bridges = puzzle.getBridges();
            if (!bridges.isEmpty()) {
                writeBridgesSection(writer, bridges, sortedIslands);
            }
        } catch (final Exception e) {
            throw new SerializationException("Puzzle could not be saved.", e);
        }
    }

    private static File assureFileExtension(final File destination) {
        return destination.getPath().endsWith(".bgs") ? destination : new File(destination.getAbsolutePath() + ".bgs");
    }

    private static List<Island> sortIslands(final Set<Island> islands) {
        return islands.stream()
                .sorted(Comparator.<Island>comparingInt(island -> island.getPosition().getColumn())
                        .thenComparing(island -> island.getPosition().getRow()))
                .collect(Collectors.toList());
    }

    private static void writeFieldSection(final PrintWriter writer, final int columns, final int rows, final int islands) {
        writer.println(Keyword.FIELD.name());
        writer.print(columns);
        writer.print(" x ");
        writer.print(rows);
        writer.print(" | ");
        writer.println(islands);
    }

    private static void writeIslandsSection(final PrintWriter writer, final List<Island> sortedIslands) {
        writer.println(Keyword.ISLANDS.name());
        sortedIslands.forEach(island -> {
            writer.print("( ");
            writer.print(island.getPosition().getColumn());
            writer.print(", ");
            writer.print(island.getPosition().getRow());
            writer.print(" | ");
            writer.print(island.getRequiredBridges());
            writer.println(" )");
        });
    }

    private static void writeBridgesSection(final PrintWriter writer, final Set<Bridge> bridges, final List<Island> sortedIslands) {
        writer.println(Keyword.BRIDGES.name());

        bridges.stream().sorted(Comparator.<Bridge>comparingInt(value -> {
            int island1Index = sortedIslands.indexOf(value.getIsland1());
            int island2Index = sortedIslands.indexOf(value.getIsland2());
            return Math.min(island1Index, island2Index);
        }).thenComparingInt(value -> {
            int island1Index = sortedIslands.indexOf(value.getIsland1());
            int island2Index = sortedIslands.indexOf(value.getIsland2());
            return Math.max(island1Index, island2Index);
        })).forEach(bridge -> {
            int island1Index = sortedIslands.indexOf(bridge.getIsland1());
            int island2Index = sortedIslands.indexOf(bridge.getIsland2());

            writer.print("( ");
            writer.print(Math.min(island1Index, island2Index));
            writer.print(", ");
            writer.print(Math.max(island1Index, island2Index));
            writer.print(" | ");
            writer.print(bridge.isDoubleBridge());
            writer.println(" )");
        });
    }
}
