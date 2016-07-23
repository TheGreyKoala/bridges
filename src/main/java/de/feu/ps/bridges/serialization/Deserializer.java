package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.analyser.PuzzleAnalyser;
import de.feu.ps.bridges.analyser.PuzzleAnalyserFactory;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.model.PuzzleBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static de.feu.ps.bridges.serialization.Keyword.*;

/**
 * Class that can be used to load a {@link Puzzle} from a file.
 * @author Tim Gremplewski
 */
public class Deserializer {

    private static final String END_OF_FILE = "EOF";
    private final List<Island> createdIslands;
    private PuzzleBuilder puzzleBuilder;
    private Puzzle puzzle;
    private PuzzleAnalyser puzzleAnalyser;

    private Deserializer() {
        createdIslands = new ArrayList<>();
    }

    /**
     * Load a puzzle from the given file.
     * @param source the source file.
     * @return the puzzle loaded from the given file.
     * @throws SerializationException if the puzzle could not be loaded.
     */
    public static Puzzle loadPuzzle(final File source) {
        Objects.requireNonNull(source, "Parameter 'source' must not be null.");

        if (!source.exists()) {
            throw new IllegalArgumentException("Source file does not exist.");
        }

        try {
            return new Deserializer().parseSourceFile(source).getResult();
        } catch (final Exception e) {
            throw new SerializationException("Could not load puzzle.", e);
        }
    }

    private PuzzleBuilder parseSourceFile(final File source) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(source))) {
            String nextLine = getNextUncommentedLine(bufferedReader);

            while (!END_OF_FILE.equals(nextLine)) {
                if (FIELD.name().equals(nextLine)) {
                    parseFieldSection(bufferedReader);
                } else if (ISLANDS.name().equals(nextLine)) {
                    parseIslandsSection(bufferedReader, puzzleBuilder);
                } else if (BRIDGES.name().equals(nextLine)) {
                    parseBridgesSection(bufferedReader, puzzleBuilder);
                } else {
                    throw new UnsupportedOperationException("Unexpected line: '" + nextLine + "'");
                }
                nextLine = getNextUncommentedLine(bufferedReader);
            }
        }
        return puzzleBuilder;
    }

    private String getNextUncommentedLine(final BufferedReader reader) throws IOException {
        String line = reader.readLine();

        while (line != null) {
            line = line.trim();
            if (line.startsWith("#") || line.isEmpty()) {
                line = reader.readLine();
            } else {
                return line;
            }
        }
        return END_OF_FILE;
    }

    private void parseFieldSection(final BufferedReader reader) throws IOException {
        final String line = getNextUncommentedLine(reader);
        final Scanner scanner = new Scanner(line);
        scanner.findInLine("^(\\d+)[ ]*x[ ]*(\\d+)[ ]*\\|[ ]*(\\d+)$");
        final MatchResult match = scanner.match();

        final int columns = Integer.parseInt(match.group(1));
        final int rows = Integer.parseInt(match.group(2));
        final int islands = Integer.parseInt(match.group(3));

        puzzleBuilder = PuzzleBuilder.createBuilder(columns, rows, islands);
        puzzle = puzzleBuilder.getResult();
        puzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzle);
    }

    private void parseIslandsSection(BufferedReader bufferedReader, final PuzzleBuilder puzzleBuilder) throws IOException {
        final int islandsCount = puzzleBuilder.getIslandsCount();
        final Pattern islandPattern = Pattern.compile("^\\([ ]*(\\d+)[ ]*,[ ]*(\\d+)[ ]*\\|[ ]*(\\d+)[ ]*\\)$");

        for (int i = 0; i < islandsCount; i++) {
            final String line = getNextUncommentedLine(bufferedReader);

            final Scanner scanner = new Scanner(line);
            scanner.findInLine(islandPattern);
            final MatchResult match = scanner.match();

            final int column = Integer.parseInt(match.group(1));
            final int row = Integer.parseInt(match.group(2));
            final int requiredBridges = Integer.parseInt(match.group(3));

            // TODO: Test
            final Position position = new Position(column, row);
            if (puzzleAnalyser.isValidIslandPosition(position)) {
                final Island island = puzzleBuilder.addIsland(position, requiredBridges);
                createdIslands.add(island);
            } else {
                throw new IllegalStateException("Found invalid island position: " + line);
            }
        }
    }

    private void parseBridgesSection(BufferedReader bufferedReader, PuzzleBuilder puzzleBuilder) throws IOException {
        final Pattern bridgePattern = Pattern.compile("^\\([ ]*(\\d+)[ ]*,[ ]*(\\d+)[ ]*\\|[ ]*(true|false)[ ]*\\)$");

        // TODO: Test islandsCount != bridgesCount

        String line = getNextUncommentedLine(bufferedReader);
        while (!END_OF_FILE.equals(line)) {
            final Scanner scanner = new Scanner(line);
            scanner.findInLine(bridgePattern);
            final MatchResult match = scanner.match();

            final Island island1 = createdIslands.get(Integer.parseInt(match.group(1)));
            final Island island2 = createdIslands.get(Integer.parseInt(match.group(2)));
            final boolean doubleBridge = Boolean.parseBoolean(match.group(3));

            if (puzzleAnalyser.isValidMove(island1, island2, doubleBridge)) {
                puzzleBuilder.addBridge(island1, island2, doubleBridge);
                line = getNextUncommentedLine(bufferedReader);
            } else {
                throw new IllegalStateException("Found invalid bridge: " + line);
            }
        }
    }
}