package de.feu.ps.bridges.generator;

import de.feu.ps.bridges.analyser.PuzzleAnalyser;
import de.feu.ps.bridges.analyser.PuzzleAnalyserFactory;
import de.feu.ps.bridges.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link PuzzleGenerator}.
 * @author Tim Gremplewski
 */
class DefaultPuzzleGenerator implements PuzzleGenerator {

    private final int columns;
    private final int rows;
    private final int islands;
    private final Set<Island> startPoints;
    private final RandomUtil randomUtil;
    private PuzzleAnalyser temporaryPuzzleAnalyser;
    private PuzzleBuilder puzzleBuilder;

    /**
     * Creates a new instance.
     * @param columns Amount of columns generated puzzles should have.
     * @param rows Amount of rows generated puzzles should have.
     * @param islands Amount of islands generated puzzles should have.
     */
    DefaultPuzzleGenerator(final int columns, final int rows, final int islands) {
        this.columns = columns;
        this.rows = rows;
        this.islands = islands;
        startPoints = new HashSet<>();
        randomUtil = new RandomUtil();
    }

    @Override
    public Puzzle generate() {
        Optional<Puzzle> puzzle;
        try {
            puzzle = tryGenerate();
        } catch (final Exception e) {
            throw new GenerationException("Could not generate a puzzle.", e);
        }

        if (puzzle.isPresent()) {
            return puzzle.get();
        } else {
            throw new GenerationException("Did not manage to generate a puzzle with the specified parameters.");
        }
    }

    private Optional<Puzzle> tryGenerate() {
        for (int i = 0; i < 100; i++) {
            initFieldsForNextRun();

            buildInitialIsland();
            int createdIslands = buildIslandsFromStartPoints();

            if (createdIslands == islands) {
                return Optional.of(finalizePuzzle());
            }
        }
        return Optional.empty();
    }

    private void initFieldsForNextRun() {
        puzzleBuilder = PuzzleBuilder.createBuilder(columns, rows, islands);
        temporaryPuzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzleBuilder.getResult());
    }

    private void buildInitialIsland() {
        final Position position = new Position(randomUtil.randomIntBetweenZeroAnd(columns - 1), randomUtil.randomIntBetweenZeroAnd(rows - 1));
        final Island island = puzzleBuilder.addIsland(position, 8);
        startPoints.add(island);
    }

    private int buildIslandsFromStartPoints() {
        int createdIslands = 1;
        while (createdIslands < islands && !startPoints.isEmpty()) {
            final Island startPoint = getRandomStartPoint();
            final Optional<Island> newIsland = buildIslandFromStartPoint(startPoint);
            if (newIsland.isPresent()) {
                createdIslands++;
                startPoints.add(newIsland.get());
            } else {
                startPoints.remove(startPoint);
            }
        }
        return createdIslands;
    }

    private Island getRandomStartPoint() {
        final int randomIndex = randomUtil.randomIntBetweenZeroAnd(startPoints.size() - 1);
        return startPoints.stream().collect(Collectors.toList()).get(randomIndex);
    }

    private Optional<Island> buildIslandFromStartPoint(final Island startPoint) {
        final List<Direction> possibleDirections = getDirectionsForNewNeighbours(startPoint);

        if (!possibleDirections.isEmpty()) {
            do {
                final Direction direction = randomUtil.pickRandomFrom(possibleDirections);
                final List<Position> validIslandPositions = temporaryPuzzleAnalyser.getValidNeighbourPositions(startPoint, direction);

                if (validIslandPositions.isEmpty()) {
                    possibleDirections.remove(direction);
                } else {
                    return Optional.of(generateIslandAtAndBridgeTo(randomUtil.pickRandomFrom(validIslandPositions), startPoint));
                }
            } while (!possibleDirections.isEmpty());
        }

        return Optional.empty();
    }

    private List<Direction> getDirectionsForNewNeighbours(final Island island) {
        final List<Direction> possibleDirections = new ArrayList<>(4);

        for (Direction direction : Direction.values()) {
            if (!island.isBridgedToNeighbour(direction)
                    && temporaryPuzzleAnalyser.isEnoughSpaceToAddNeighbour(island, direction)) {
                possibleDirections.add(direction);
            }
        }

        return possibleDirections;
    }

    private Island generateIslandAtAndBridgeTo(final Position position, final Island otherBridgedIsland) {
        Island nextIsland = puzzleBuilder.addIsland(position, 8);
        puzzleBuilder.addBridge(otherBridgedIsland, nextIsland, randomUtil.getRandom().nextBoolean());
        return nextIsland;
    }

    private Puzzle finalizePuzzle() {
        puzzleBuilder.setRequiredBridgesToCurrentCountOfBridges();
        final Puzzle puzzle = puzzleBuilder.getResult();
        puzzle.removeAllBridges();
        return puzzle;
    }
}
