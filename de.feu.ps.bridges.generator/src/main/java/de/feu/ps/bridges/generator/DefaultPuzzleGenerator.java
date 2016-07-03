package de.feu.ps.bridges.generator;

import de.feu.ps.bridges.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static de.feu.ps.bridges.model.Direction.*;

/**
 * Default implementation of {@link PuzzleGenerator}.
 * @author Tim Gremplewski
 */
class DefaultPuzzleGenerator implements PuzzleGenerator {

    private final int columns;
    private final int rows;
    private final int islands;
    private Set<Island> startPoints;
    private RandomUtil randomUtil;

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
            final PuzzleBuilder puzzleBuilder = PuzzleBuilder.createBuilder(columns, rows, islands);
            buildInitialIsland(puzzleBuilder);

            int createdIslands = 1;

            while (createdIslands < islands && !startPoints.isEmpty()) {
                final Island startPoint = getRandomStartPoint();
                final Optional<Island> newIsland = buildIslandFromStartPoint(startPoint, puzzleBuilder);
                if (newIsland.isPresent()) {
                    createdIslands++;
                    startPoints.add(newIsland.get());
                } else {
                    startPoints.remove(startPoint);
                }
            }

            if (createdIslands == islands) {
                puzzleBuilder.setRequiredBridgesToCurrentCountOfBridges();
                final Puzzle puzzle = puzzleBuilder.getResult();
                puzzle.removeAllBridges();
                return Optional.of(puzzle);
            }
        }

        return Optional.empty();
    }

    private void buildInitialIsland(PuzzleBuilder puzzleBuilder) {
        Position position = new Position(randomUtil.randomIntBetweenZeroAnd(columns - 1), randomUtil.randomIntBetweenZeroAnd(rows - 1));
        Island island = puzzleBuilder.addIsland(position, 8);
        startPoints.add(island);
    }

    private Island getRandomStartPoint() {
        final int randomIndex = randomUtil.randomIntBetweenZeroAnd(startPoints.size() - 1);
        return startPoints.stream().collect(Collectors.toList()).get(randomIndex);
    }

    private Optional<Island> buildIslandFromStartPoint(final Island startPoint, final PuzzleBuilder puzzleBuilder) {
        final List<Direction> possibleDirections
            = getDirectionsWithEnoughSpaceToAddAnIsland(startPoint)
                .stream()
                .filter(direction -> !startPoint.isBridgedToNeighbour(direction))
                .collect(Collectors.toList());

        if (!possibleDirections.isEmpty()) {
            do {
                Direction direction = randomUtil.pickRandomDirectionFrom(possibleDirections);
                List<Position> validIslandPositions = getValidIslandPositions(startPoint, direction, puzzleBuilder);

                if (validIslandPositions.isEmpty()) {
                    possibleDirections.remove(direction);
                } else {
                    Position position = validIslandPositions.get(randomUtil.randomIntBetweenZeroAnd(validIslandPositions.size() - 1));
                    Island nextIsland = puzzleBuilder.addIsland(position, 8);
                    puzzleBuilder.addBridge(startPoint, nextIsland, randomUtil.getRandom().nextBoolean());
                    return Optional.of(nextIsland);
                }
            } while (!possibleDirections.isEmpty());
        }

        return Optional.empty();
    }

    private List<Position> getValidIslandPositions(Island start, Direction direction, PuzzleBuilder puzzleBuilder) {

        List<Position> validStartPoints = new ArrayList<>();

        if (direction == NORTH) {
            Optional<Island> optionalNorthNeighbour = start.getNeighbour(NORTH);
            int border = optionalNorthNeighbour.isPresent() ? optionalNorthNeighbour.get().getPosition().getRow() + 2 : 0;
            for (int row = start.getPosition().getRow() - 2; row >= border; row--) {
                Position position = new Position(start.getPosition().getColumn(), row);
                if (puzzleBuilder.adjacentIslandAt(position)
                        || puzzleBuilder.isAnyBridgeCrossing(start.getPosition(), position)) {
                    break;
                } else {
                    validStartPoints.add(position);
                }
            }
        } else if (direction == EAST) {
            Optional<Island> optionalEastNeighbour = start.getNeighbour(EAST);
            int border = optionalEastNeighbour.isPresent() ? optionalEastNeighbour.get().getPosition().getColumn() - 2 : columns - 1;
            for (int column = start.getPosition().getColumn() + 2; column <= border; column++) {
                Position position = new Position(column, start.getPosition().getRow());
                if (puzzleBuilder.adjacentIslandAt(position)
                        || puzzleBuilder.isAnyBridgeCrossing(start.getPosition(), position)) {
                    break;
                } else {
                    validStartPoints.add(position);
                }
            }
        } else if (direction == SOUTH) {
            Optional<Island> optionalSouthNeighbour = start.getNeighbour(SOUTH);
            int border = optionalSouthNeighbour.isPresent() ? optionalSouthNeighbour.get().getPosition().getRow() - 2 : rows - 1;
            for (int row = start.getPosition().getRow() + 2; row <= border; row++) {
                Position position = new Position(start.getPosition().getColumn(), row);
                if (puzzleBuilder.adjacentIslandAt(position)
                        || puzzleBuilder.isAnyBridgeCrossing(start.getPosition(), position)) {
                    break;
                } else {
                    validStartPoints.add(position);
                }
            }
        } else {
            Optional<Island> optionalWestNeighbour = start.getNeighbour(WEST);
            int border = optionalWestNeighbour.isPresent() ? optionalWestNeighbour.get().getPosition().getColumn() - 2 : 0;
            for (int column = start.getPosition().getColumn() - 2; column >= border; column--) {
                Position position = new Position(column, start.getPosition().getRow());
                if (puzzleBuilder.adjacentIslandAt(position)
                        || puzzleBuilder.isAnyBridgeCrossing(start.getPosition(), position)) {
                    break;
                } else {
                    validStartPoints.add(position);
                }
            }
        }

        return validStartPoints;
    }

    private List<Direction> getDirectionsWithEnoughSpaceToAddAnIsland(final Island island) {
        final List<Direction> possibleDirections = new ArrayList<>(4);

        // >= 2 so it is possible to have at least one free field between this island and the new one
        if (island.getPosition().getRow() >= 2) {
            if (island.getNeighbour(NORTH).isPresent()) {
                // Does a new island fit between the current island and its north neighbour
                if (island.getDistanceToNeighbour(NORTH) >= 4) {
                    possibleDirections.add(NORTH);
                }
            } else {
                possibleDirections.add(NORTH);
            }
        }

        /*
         * <= columns - 3
         * -1 because column numbering starts at 0
         * -2 so it is possible to have at least one free field between this island and the new one
         */
        if (island.getPosition().getColumn() <= columns - 3) {
            if (island.getNeighbour(EAST).isPresent()) {
                if (island.getDistanceToNeighbour(EAST) >= 4) {
                    possibleDirections.add(EAST);
                }
            } else {
                possibleDirections.add(EAST);
            }
        }

        if (island.getPosition().getRow() <= rows - 3) {
            if (island.getNeighbour(SOUTH).isPresent()) {
                if (island.getDistanceToNeighbour(SOUTH) >= 4) {
                    possibleDirections.add(SOUTH);
                }
            } else {
                possibleDirections.add(SOUTH);
            }
        }

        if (island.getPosition().getColumn() >= 2) {
            if (island.getNeighbour(WEST).isPresent()) {
                if (island.getDistanceToNeighbour(WEST) >= 4) {
                    possibleDirections.add(WEST);
                }
            } else {
                possibleDirections.add(WEST);
            }
        }

        return possibleDirections;
    }
}
