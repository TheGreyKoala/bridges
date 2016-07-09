package de.feu.ps.bridges.toolkit;

import de.feu.ps.bridges.analyser.PuzzleAnalyser;
import de.feu.ps.bridges.analyser.PuzzleAnalyserFactory;
import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.serialization.Serializer;
import de.feu.ps.bridges.solver.Move;
import de.feu.ps.bridges.solver.PuzzleSolver;
import de.feu.ps.bridges.solver.PuzzleSolverFactory;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * Default implementation of {@link PuzzleToolkit}.
 * @author Tim Gremplewski
 */
class DefaultPuzzleToolkit implements PuzzleToolkit {

    private final Puzzle puzzle;
    private final PuzzleSolver puzzleSolver;
    private final PuzzleAnalyser puzzleAnalyser;

    DefaultPuzzleToolkit(final Puzzle puzzle) {
        this.puzzle = Objects.requireNonNull(puzzle, "Parameter 'puzzle' must not be null.");
        puzzleSolver = PuzzleSolverFactory.createPuzzleSolverFor(puzzle);
        puzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzle);
    }

    @Override
    public Puzzle getPuzzle() {
        return puzzle;
    }

    @Override
    public PuzzleStatus getPuzzleStatus() {
        return puzzleAnalyser.getStatus();
    }

    @Override
    public Optional<Bridge> nextMove() {
        try {
            Optional<Move> nextMove = puzzleSolver.getNextMove();
            Optional<Bridge> optionalBridge;
            if (nextMove.isPresent()) {
                optionalBridge = Optional.of(nextMove.get().apply());
            } else {
                optionalBridge = Optional.empty();
            }
            return optionalBridge;
        } catch (final Exception e) {
            throw new GeneralException("Error while finding a next safe move.", e);
        }
    }

    @Override
    public Optional<Bridge> tearDownBridge(final Island island, final Direction direction) {
        try {
            Optional<Island> neighbour = island.getNeighbour(direction);
            if (neighbour.isPresent()) {
                return puzzle.tearDownBridge(island, neighbour.get());
            }
            return Optional.empty();
        } catch (final Exception e) {
            throw new GeneralException("Error while removing bridge.", e);
        }
    }

    @Override
    public void savePuzzle(final File destinationFile) {
        try {
            Serializer.savePuzzle(puzzle, destinationFile);
        } catch (final Exception e) {
            throw new GeneralException("Error while saving puzzle", e);
        }
    }

    @Override
    public void solvePuzzle() {
        try {
            puzzleSolver.solve();
        } catch (Exception e) {
            throw new GeneralException("Error while solving the puzzle.", e);
        }
    }

    @Override
    public Optional<Bridge> tryBuildBridge(final Island island, final Direction direction) {
        try {
            Optional<Bridge> optionalBridge;
            if (puzzleAnalyser.isValidMove(island, direction)) {
                // TODO: Can .get() without isPresent lead to an error here?
                Bridge bridge = puzzle.buildBridge(island, island.getNeighbour(direction).get(), false);
                optionalBridge = Optional.of(bridge);
            } else {
                optionalBridge = Optional.empty();
            }
            return  optionalBridge;
        } catch (Exception e) {
            throw new GeneralException("Error while adding bridge.", e);
        }
    }
}
