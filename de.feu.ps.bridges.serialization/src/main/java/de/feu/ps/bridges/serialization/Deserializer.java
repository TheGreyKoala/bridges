package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.model.PuzzleBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Tim Gremplewski
 */
public class Deserializer {

    private static final String END_OF_FILE = "EOF";

    public Puzzle loadPuzzle(final String filePath) throws Exception {
        // TODO Parameter validation

        final PuzzleBuilder puzzleBuilder = new PuzzleBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String nextLine = getNextUncommentedLine(bufferedReader);

            while (!END_OF_FILE.equals(nextLine)) {
                switch (nextLine) {
                    case "FIELD":
                        parseFieldSection(bufferedReader, puzzleBuilder);
                        break;
                    case "ISLANDS":
                        parseIslandsSection(bufferedReader, puzzleBuilder);
                        break;
                    case "BRIDGES":
                        parseBridgesSection(bufferedReader, puzzleBuilder);
                        break;
                    default:
                        // TODO Handle unexpected line
                        //throw new Exception("Unexpected line");
                }
                nextLine = getNextUncommentedLine(bufferedReader);
            }
        }

        return puzzleBuilder.getResult();
    }

    private String getNextUncommentedLine(BufferedReader reader) throws IOException {
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

    private void parseFieldSection(BufferedReader reader, PuzzleBuilder puzzleBuilder) throws IOException {
        final String line = getNextUncommentedLine(reader);
        final Scanner scanner = new Scanner(line);
        scanner.findInLine("^(\\d+)[ ]*x[ ]*(\\d+)[ ]*\\|[ ]*(\\d+)$");
        MatchResult match = scanner.match();

        // TODO: Handle match not found (IllegalStateException)

        int columns = Integer.parseInt(match.group(1));
        int rows = Integer.parseInt(match.group(2));
        int islands = Integer.parseInt(match.group(3));

        // TODO: Handle parse exception

        puzzleBuilder.setPuzzleDimensions(columns, rows);
        puzzleBuilder.setIslandsCount(islands);
    }

    private void parseIslandsSection(BufferedReader bufferedReader, final PuzzleBuilder puzzleBuilder) throws IOException {
        final int islandsCount = puzzleBuilder.getIslandsCount();
        final Pattern islandPattern = Pattern.compile("^\\([ ]*(\\d+)[ ]*,[ ]*(\\d+)[ ]*\\|[ ]*(\\d+)[ ]*\\)$");

        for (int i = 0; i < islandsCount; i++) {
            String line = getNextUncommentedLine(bufferedReader);

            Scanner scanner = new Scanner(line);
            scanner.findInLine(islandPattern);
            MatchResult match = scanner.match();

            // TODO: Handle no match found (IllegalArguementException)

            int column = Integer.parseInt(match.group(1));
            int row = Integer.parseInt(match.group(2));
            int requiredBridges = Integer.parseInt(match.group(3));

            puzzleBuilder.addIsland(column, row, requiredBridges);
        }
    }

    private void parseBridgesSection(BufferedReader bufferedReader, PuzzleBuilder puzzleBuilder) throws IOException {
        final Pattern bridgePattern = Pattern.compile("^\\([ ]*(\\d+)[ ]*,[ ]*(\\d+)[ ]*\\|[ ]*(true|false)[ ]*\\)$");

        // TODO: Test islandsCount != bridgesCount

        String line = getNextUncommentedLine(bufferedReader);
        while (!END_OF_FILE.equals(line)) {
            Scanner scanner = new Scanner(line);
            scanner.findInLine(bridgePattern);
            MatchResult match = scanner.match();

            int islandAIndex = Integer.parseInt(match.group(1));
            int islandBIndex = Integer.parseInt(match.group(2));
            boolean doubleBridge = Boolean.parseBoolean(match.group(3));

            puzzleBuilder.addBridge(islandAIndex, islandBIndex, doubleBridge);
            line = getNextUncommentedLine(bufferedReader);
        }
    }
}
