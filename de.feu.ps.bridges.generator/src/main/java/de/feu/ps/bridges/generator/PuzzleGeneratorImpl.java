package de.feu.ps.bridges.generator;

import de.feu.ps.bridges.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tim Gremplewski
 */
public class PuzzleGeneratorImpl {

    private final int columns;
    private final int rows;
    private final int islands;
    private static Random random;
    private Set<Island> startPoints;

    static {
        random = new Random();
    }

    private PuzzleGeneratorImpl(final int columns, final int rows, final int islands) {
        this.columns = columns;
        this.rows = rows;
        this.islands = islands;
        startPoints = new HashSet<>();
    }

    public static Puzzle generatePuzzle() {
        final int[] dimensions = random.ints(2, 4, 25).toArray();
        return generatePuzzle(dimensions[0], dimensions[1]);
    }

    public static Puzzle generatePuzzle(final int columns, final int rows) {
        if (columns * rows < 3) {
            throw new IllegalArgumentException("Puzzle must consist of at least 3 fields.");
        }

        int lowerLimit = Math.min(columns, rows);
        int upperLimit = columns * rows / 5;

        // Lower limit might be less than upper limit in some cases.
        // Swap lower and upper limits. See newsgroup for this valid workaround
        lowerLimit = Math.min(lowerLimit, upperLimit);
        upperLimit = Math.max(lowerLimit, upperLimit);

        int islands = randomIntBetween(lowerLimit, upperLimit);
        if (islands < 2) {
            islands = 2;
        }

        return generatePuzzle(columns, rows, islands);
    }

    public static Puzzle generatePuzzle(final int columns, final int rows, final int islands) {
        return new PuzzleGeneratorImpl(columns, rows, islands).generate();
    }

    private Puzzle generate() {
        boolean puzzleCreated = false;
        int runs = 0;
        PuzzleBuilder puzzleBuilder = null;

        do {
            puzzleBuilder = new PuzzleBuilder();
            puzzleBuilder.setPuzzleDimensions(columns, rows);
            puzzleBuilder.setIslandsCount(islands);
            buildInitialIsland(puzzleBuilder);

            runs++;

            int createdIslands = 1;

            while (createdIslands < islands && !startPoints.isEmpty()) {
                int randomIndex = randomIntBetweenZeroAnd(startPoints.size() - 1);
                Island island1 = startPoints.stream().collect(Collectors.toList()).get(randomIndex);
                Island newIsland = buildNextIsland(island1, puzzleBuilder);

                if (newIsland == null) {
                    startPoints.remove(island1);
                } else {
                    startPoints.add(newIsland);
                    createdIslands++;
                }
            }

            if (createdIslands == islands) {
                puzzleCreated = true;
            }
        } while (!puzzleCreated && runs <= 100);

        if (puzzleCreated) {
            puzzleBuilder.setRequiredBridgesToCurrentCountOfBridges();
            puzzleBuilder.clearBridges();
            return puzzleBuilder.getResult();
        } else {
            // TODO do something
            return null;
        }
    }

    private void buildInitialIsland(PuzzleBuilder puzzleBuilder) {
        Island island = puzzleBuilder.addIsland(randomIntBetweenZeroAnd(columns), randomIntBetweenZeroAnd(rows), 8);
        startPoints.add(island);
    }

    private List<Position> getValidIslandPositions(Island start, Direction direction, PuzzleBuilder puzzleBuilder) {

        List<Position> validStartPoints = new ArrayList<>();

        if (direction == Direction.NORTH) {
            int border = start.hasNorthNeighbour() ? start.getNorthNeighbour().getRow() + 2 : 0;
            for (int row = start.getRow() - 2; row >= border; row--) {
                Position position = new Position(start.getColumn(), row);
                if (puzzleBuilder.adjacentIslandAt(position)
                    || puzzleBuilder.isAnyBridgeCrossing(start.getPosition(), position)) {
                    break;
                } else {
                    validStartPoints.add(position);
                }
            }
        } else if (direction == Direction.EAST) {
            int border = start.hasEastNeighbour() ? start.getEastNeighbour().getColumn() - 2 : columns - 1;
            for (int column = start.getColumn() + 2; column <= border; column++) {
                Position position = new Position(column, start.getRow());
                if (puzzleBuilder.adjacentIslandAt(position)
                    || puzzleBuilder.isAnyBridgeCrossing(start.getPosition(), position)) {
                    break;
                } else {
                    validStartPoints.add(position);
                }
            }
        } else if (direction == Direction.SOUTH) {
            int border = start.hasSouthNeighbour() ? start.getSouthNeighbour().getRow() - 2 : rows - 1;
            for (int row = start.getRow() + 2; row <= border; row++) {
                Position position = new Position(start.getColumn(), row);
                if (puzzleBuilder.adjacentIslandAt(position)
                        || puzzleBuilder.isAnyBridgeCrossing(start.getPosition(), position)) {
                    break;
                } else {
                    validStartPoints.add(position);
                }
            }
        } else {
            int border = start.hasWestNeighbour() ? start.getWestNeighbour().getColumn() - 2 : 0;
            for (int column = start.getColumn() - 2; column >= border; column--) {
                Position position = new Position(column, start.getRow());
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

    private Island buildNextIsland(Island start, PuzzleBuilder puzzleBuilder) {
        List<Direction> possibleDirections = getDirectionsWithEnoughSpaceToAddAnIsland(start);

        if (possibleDirections.isEmpty()) {
            return null;
        }

        do {
            Direction direction = pickRandomDirectionFrom(possibleDirections);
            if (start.isBridgedToNeighbour(direction)) {
                possibleDirections.remove(direction);
            } else {
                List<Position> validIslandPositions = getValidIslandPositions(start, direction, puzzleBuilder);

                if (validIslandPositions.isEmpty()) {
                    possibleDirections.remove(direction);
                } else {
                    Position position = validIslandPositions.get(randomIntBetweenZeroAnd(validIslandPositions.size() - 1));
                    Island nextIsland = puzzleBuilder.addIsland(position.getColumn(), position.getRow(), 8);
                    puzzleBuilder.addBridge(start, nextIsland, random.nextBoolean());
                    return nextIsland;
                }
            }
        } while (!possibleDirections.isEmpty());

        return null;
    }

    private List<Direction> getDirectionsWithEnoughSpaceToAddAnIsland(final Island island) {
        final List<Direction> possibleDirections = new ArrayList<>(4);

        // >= 2 so it is possible to have at least one free field between this island and the new one
        if (island.getRow() >= 2) {
            if (island.hasNorthNeighbour()) {
                // Does a new island fit between the current island and its north neighbour
                if (island.getDistanceToNorthNeighbour() >= 4) {
                    possibleDirections.add(Direction.NORTH);
                }
            } else {
                possibleDirections.add(Direction.NORTH);
            }
        }

        /*
         * <= columns - 3
         * -1 because column numbering starts at 0
         * -2 so it is possible to have at least one free field between this island and the new one
         */
        if (island.getColumn() <= columns - 3) {
            if (island.hasEastNeighbour()) {
                if (island.getDistanceToEastNeighbour() >= 4) {
                    possibleDirections.add(Direction.EAST);
                }
            } else {
                possibleDirections.add(Direction.EAST);
            }
        }

        if (island.getRow() <= rows - 3) {
            if (island.hasSouthNeighbour()) {
                if (island.getDistanceToSouthNeighbour() >= 4) {
                    possibleDirections.add(Direction.SOUTH);
                }
            } else {
                possibleDirections.add(Direction.SOUTH);
            }
        }

        if (island.getColumn() >= 2) {
            if (island.hasWestNeighbour()) {
                if (island.getDistanceToWestNeighbour() >= 4) {
                    possibleDirections.add(Direction.WEST);
                }
            } else {
                possibleDirections.add(Direction.WEST);
            }
        }

        return possibleDirections;
    }

    private Direction pickRandomDirectionFrom(List<Direction> directions) {
        int index = randomIntBetweenZeroAnd(directions.size() - 1);
        return directions.get(index);
    }

    private static int randomIntBetween(int lowerLimit, int upperLimit) {
        if (lowerLimit == upperLimit) {
            return lowerLimit;
        }
        return random.ints(1, lowerLimit, upperLimit + 1).findFirst().getAsInt();
    }

    private int randomIntBetweenZeroAnd(int bound) {
        return random.nextInt(bound + 1);
    }
}