package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.Puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static de.feu.ps.bridges.model.Direction.*;
import static de.feu.ps.bridges.model.Direction.WEST;

/**
 * @author Tim Gremplewski
 */
class NewIslandAnalyser {

    private final Puzzle puzzle;
    private final MoveAnalyser moveAnalyser;

    NewIslandAnalyser(final Puzzle puzzle, final MoveAnalyser moveAnalyser) {
        this.puzzle = Objects.requireNonNull(puzzle, "Parameter 'puzzle' must not be null.");
        this.moveAnalyser = Objects.requireNonNull(moveAnalyser, "Parameter 'moveAnalyser' must not be null.");
    }

    boolean isValidIslandPosition(final Position position) {
        Objects.requireNonNull(position, "Parameter 'position' must not be null.");
        return isInsidePuzzle(position) && !adjacentIslandAt(position);
    }

    private boolean isInsidePuzzle(final Position position) {
        return position.getColumn() < puzzle.getColumnsCount()
                && position.getRow() < puzzle.getRowsCount();
    }

    private boolean adjacentIslandAt(final Position position) {
        final int column = position.getColumn();
        final int row = position.getRow();

        return puzzle.getIslands().stream().anyMatch(island -> {
            final int islandColumn = island.getPosition().getColumn();
            final int islandRow = island.getPosition().getRow();

            return islandColumn == column && islandRow == row - 1 // North
                    || islandColumn == column + 1 && islandRow == row // East
                    || islandColumn == column && islandRow == row + 1 // South
                    || islandColumn == column - 1 && islandRow == row; // West
        });
    }

    boolean isEnoughSpaceToAddNeighbour(final Island island, final Direction direction) {
        Objects.requireNonNull(island, "Parameter 'island' must not be null.");
        Objects.requireNonNull(direction, "Parameter 'direction' must not be null.");

        switch (direction) {
            case NORTH:
                return enoughSpaceToAddNorthNeighbour(island);
            case EAST:
                return enoughSpaceToAddEastNeighbour(island);
            case SOUTH:
                return enoughSpaceToAddSouthNeighbour(island);
            case WEST:
                return enoughSpaceToAddWestNeighbour(island);
            default:
                throw new UnsupportedOperationException("Unknown direction");
        }
    }

    private boolean enoughSpaceToAddNorthNeighbour(final Island island) {
        // >= 2 so it is possible to have at least one free field between this island and the new one
        if (island.getPosition().getRow() >= 2) {
            if (island.getNeighbour(NORTH).isPresent()) {
                // Does a new island fit between the current island and its north neighbour
                if (island.getDistanceToNeighbour(NORTH) >= 4) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean enoughSpaceToAddEastNeighbour(final Island island) {
        /*
         * <= columns - 3
         * -1 because column numbering starts at 0
         * -2 so it is possible to have at least one free field between this island and the new one
         */
        if (island.getPosition().getColumn() <= puzzle.getColumnsCount() - 3) {
            if (island.getNeighbour(EAST).isPresent()) {
                if (island.getDistanceToNeighbour(EAST) >= 4) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean enoughSpaceToAddSouthNeighbour(final Island island) {
        if (island.getPosition().getRow() <= puzzle.getRowsCount() - 3) {
            if (island.getNeighbour(SOUTH).isPresent()) {
                if (island.getDistanceToNeighbour(SOUTH) >= 4) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean enoughSpaceToAddWestNeighbour(final Island island) {
        if (island.getPosition().getColumn() >= 2) {
            if (island.getNeighbour(WEST).isPresent()) {
                if (island.getDistanceToNeighbour(WEST) >= 4) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    List<Position> getValidNeighbourPositions(final Island island, final Direction direction) {
        Objects.requireNonNull(island, "Parameter 'island' must not be null.");
        Objects.requireNonNull(direction, "Parameter 'direction' must not be null.");

        final List<Position> validStartPoints = new ArrayList<>();
        final Optional<Island> optionalNeighbour = island.getNeighbour(direction);

        // For all directions, we have to move away from the start and must not move towards it!
        // Otherwise we could recognize close neighbours to late and break out to late.

        if (direction == NORTH) {
            int border = optionalNeighbour.isPresent() ? optionalNeighbour.get().getPosition().getRow() + 2 : 0;
            for (int row = island.getPosition().getRow() - 2; row >= border; row--) {
                Position position = new Position(island.getPosition().getColumn(), row);
                if (!addIfValid(position, island, validStartPoints)) {
                    break;
                }
            }
        } else if (direction == EAST) {
            int border = optionalNeighbour.isPresent() ? optionalNeighbour.get().getPosition().getColumn() - 2 : puzzle.getColumnsCount() - 1;
            for (int column = island.getPosition().getColumn() + 2; column <= border; column++) {
                Position position = new Position(column, island.getPosition().getRow());
                if (!addIfValid(position, island, validStartPoints)) {
                    break;
                }
            }
        } else if (direction == SOUTH) {
            int border = optionalNeighbour.isPresent() ? optionalNeighbour.get().getPosition().getRow() - 2 : puzzle.getRowsCount() - 1;
            for (int row = island.getPosition().getRow() + 2; row <= border; row++) {
                Position position = new Position(island.getPosition().getColumn(), row);
                if (!addIfValid(position, island, validStartPoints)) {
                    break;
                }
            }
        } else if (direction == WEST) {
            int border = optionalNeighbour.isPresent() ? optionalNeighbour.get().getPosition().getColumn() - 2 : 0;
            for (int column = island.getPosition().getColumn() - 2; column >= border; column--) {
                Position position = new Position(column, island.getPosition().getRow());
                if (!addIfValid(position, island, validStartPoints)) {
                    break;
                }
            }
        }

        return validStartPoints;
    }

    private boolean addIfValid(final Position position, final Island start, final List<Position> validStartPoints) {
        if (!isValidIslandPosition(position) || moveAnalyser.isAnyBridgeCrossing(start.getPosition(), position)) {
            return false;
        } else {
            validStartPoints.add(position);
            return true;
        }
    }
}
