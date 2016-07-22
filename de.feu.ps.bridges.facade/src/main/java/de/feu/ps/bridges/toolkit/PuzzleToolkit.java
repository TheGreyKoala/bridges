package de.feu.ps.bridges.toolkit;

import de.feu.ps.bridges.analyser.PuzzleAnalyser;
import de.feu.ps.bridges.analyser.PuzzleAnalyserFactory;
import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.generator.PuzzleGeneratorFactory;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.serialization.Deserializer;
import de.feu.ps.bridges.serialization.Serializer;
import de.feu.ps.bridges.solver.Move;
import de.feu.ps.bridges.solver.PuzzleSolver;
import de.feu.ps.bridges.solver.PuzzleSolverFactory;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * Helper class that aggregates common tasks regarding {@link Puzzle}s.
 * @author Tim Gremplewski
 */
public class PuzzleToolkit {

    private final Puzzle puzzle;
    private final PuzzleSolver puzzleSolver;
    private final PuzzleAnalyser puzzleAnalyser;

    private PuzzleToolkit(final Puzzle puzzle) {
        this.puzzle = Objects.requireNonNull(puzzle, "Parameter 'puzzle' must not be null.");
        puzzleSolver = PuzzleSolverFactory.createPuzzleSolverFor(puzzle);
        puzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzle);
    }

    /**
     * Create a new instance for the given {@link Puzzle}.
     * @param puzzle puzzle to use.
     * @return a new instance.
     */
    public static PuzzleToolkit createFor(final Puzzle puzzle) {
        return new PuzzleToolkit(puzzle);
    }

    /**
     * Load a {@link Puzzle} from the given file and create a new {@link PuzzleToolkit} instance for it.
     * @param sourceFile Source file of the puzzle.
     * @return a new {@link PuzzleToolkit} instance.
     */
    public static PuzzleToolkit createForLoadedPuzzle(final File sourceFile) {
        final Puzzle puzzle = Deserializer.loadPuzzle(sourceFile);
        return createFor(puzzle);
    }

    /**
     * Generate a random puzzle and create a new instance for it.
     * @return a new instance.
     */
    public static PuzzleToolkit createForGeneratedPuzzle() {
        final Puzzle puzzle = PuzzleGeneratorFactory.createPuzzleGenerator().generate();
        return createFor(puzzle);
    }

    /**
     * Generate a new puzzle with a random number of islands and create a new instance for it.
     * @param columns Number of columns of the generated puzzle.
     * @param rows Number of columns of the generated puzzle.
     * @return a new instance.
     */
    public static PuzzleToolkit createForGeneratedPuzzle(final int columns, final int rows) {
        final Puzzle puzzle = PuzzleGeneratorFactory.createPuzzleGenerator(columns, rows).generate();
        return createFor(puzzle);
    }

    /**
     * Generate a new puzzle and create a new instance for it.
     * @param columns Number of columns of the generated puzzle.
     * @param rows Number of rows of the generated puzzle.
     * @param islands Number of islands of the generated puzzle.
     * @return a new instance.
     */
    public static PuzzleToolkit createForGeneratedPuzzle(final int columns, final int rows, final int islands) {
        Puzzle puzzle = PuzzleGeneratorFactory.createPuzzleGenerator(columns, rows, islands).generate();
        return createFor(puzzle);
    }

    /**
     * Get the puzzle of this instance.
     * @return the puzzle of this instance.
     */
    public Puzzle getPuzzle() {
        return puzzle;
    }

    /**
     * Get the current status of the puzzle.
     * @return the current status of the puzzle.
     */
    public PuzzleStatus getPuzzleStatus() {
        return puzzleAnalyser.getStatus();
    }

    /**
     * Apply a next save move on the puzzle.
     * @return {@link Optional} containing the {@link Bridge} that was created when the move was applied,
     *  if any could be found.
     */
    public Optional<Bridge> nextMove() {
        Optional<Move> nextMove = puzzleSolver.getNextMove();
        Optional<Bridge> optionalBridge;
        if (nextMove.isPresent()) {
            optionalBridge = Optional.of(nextMove.get().apply());
        } else {
            optionalBridge = Optional.empty();
        }
        return optionalBridge;
    }

    /**
     * Remove a bridge in the given direction from the given island.
     * @param island Island from which the bridge should be removed.
     * @param direction Direction that specifies the bridge that should be removed.
     * @return {@link Optional} containing the {@link Bridge} that was removed, if any.
     */
    public Optional<Bridge> removeBridge(final Island island, final Direction direction) {
        Optional<Island> neighbour = island.getNeighbour(direction);
        if (neighbour.isPresent()) {
            return puzzle.tearDownBridge(island, neighbour.get());
        }
        return Optional.empty();
    }

    /**
     * Save the puzzle at the given destination.
     * @param destinationFile Location where the puzzle should be saved.
     */
    public void savePuzzle(final File destinationFile) {
        Serializer.savePuzzle(puzzle, destinationFile);
    }

    /**
     * Solve the puzzle.
     */
    public void solvePuzzle() {
        puzzleSolver.solve();
    }

    /**
     * Try to add a bridge in the given direction from the given island.
     * If such a bridge is not valid, the returned {@link Optional} will be empty.
     *
     * @param island Island to which the bridge should be added.
     * @param direction Direction of the new bridge.
     * @return {@link Optional} containing the new {@link Bridge}, if one was created.
     */
    public Optional<Bridge> tryAddBridge(final Island island, final Direction direction) {
        Optional<Bridge> optionalBridge;
        if (puzzleAnalyser.isValidMove(island, direction)) {
            // TODO: Can .get() without isPresent lead to an error here?
            Bridge bridge = puzzle.buildBridge(island, island.getNeighbour(direction).get(), false);
            optionalBridge = Optional.of(bridge);
        } else {
            optionalBridge = Optional.empty();
        }
        return  optionalBridge;
    }
}
