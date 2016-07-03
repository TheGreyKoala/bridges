package de.feu.ps.bridges.facade;

import de.feu.ps.bridges.analyser.Analyser;
import de.feu.ps.bridges.analyser.DefaultAnalyser;
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
import java.util.Optional;

/**
 * @author Tim Gremplewski
 */
public class Facade {

    public static Puzzle loadPuzzle(final File sourceFile) {
        return Deserializer.loadPuzzle(sourceFile);
    }

    public static void savePuzzle(final Puzzle puzzle, final File destinationFile) {
        Serializer.savePuzzle(puzzle, destinationFile);
    }

    public static void solvePuzzle(final Puzzle puzzle) {
        PuzzleSolver puzzleSolver = PuzzleSolverFactory.createPuzzleSolverFor(puzzle);
        puzzleSolver.solve();
    }

    public static Puzzle newPuzzle() {
        return PuzzleGeneratorFactory.createPuzzleGenerator().generate();
    }

    public static Puzzle newPuzzle(final int columns, final int rows) {
        return PuzzleGeneratorFactory.createPuzzleGenerator(columns, rows).generate();
    }

    public static Puzzle newPuzzle(final int columns, final int rows, final int islands) {
        return PuzzleGeneratorFactory.createPuzzleGenerator(columns, rows, islands).generate();
    }

    public static Optional<Bridge> nextMove(final Puzzle puzzle) {
        PuzzleSolver puzzleSolver = PuzzleSolverFactory.createPuzzleSolverFor(puzzle);
        Optional<Move> nextMove = puzzleSolver.getNextMove();
        Optional<Bridge> optionalBridge;
        if (nextMove.isPresent()) {
            optionalBridge = Optional.of(nextMove.get().apply());
        } else {
            optionalBridge = Optional.empty();
        }
        return optionalBridge;
    }

    public static Optional<Bridge> tryAddBridge(Puzzle puzzle, Island island, Direction direction) {
        Optional<Bridge> optionalBridge;
        Analyser analyser = DefaultAnalyser.createAnalyserFor(puzzle);
        if (analyser.isValidMove(island, direction)) {
            // TODO: Can .get() without isPresent lead to an error here?
            Bridge bridge = puzzle.buildBridge(island, island.getNeighbour(direction).get(), false);
            optionalBridge = Optional.of(bridge);
        } else {
            optionalBridge = Optional.empty();
        }
        return  optionalBridge;
    }

    public static Optional<Bridge> removeBridge(Puzzle puzzle, Island island, Direction direction) {
        Optional<Island> neighbour = island.getNeighbour(direction);
        if (neighbour.isPresent()) {
            return puzzle.tearDownBridge(island, neighbour.get());
        }
        return Optional.empty();
    }

    public static PuzzleStatus getPuzzleStatus(Puzzle puzzle) {
        DefaultAnalyser analyserFor = DefaultAnalyser.createAnalyserFor(puzzle);
        return analyserFor.getStatus();
    }
}
