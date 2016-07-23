package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.analyser.PuzzleAnalyser;
import de.feu.ps.bridges.analyser.PuzzleAnalyserFactory;
import de.feu.ps.bridges.model.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Default implementation of {@link PuzzleSolver}.
 * @author Tim Gremplewski
 */
class DefaultPuzzleSolver implements PuzzleSolver {

    private final Puzzle puzzle;
    private final PuzzleAnalyser puzzleAnalyser;

    /**
     * Create a new instance.
     * @param puzzle the puzzle to be solved.
     */
    DefaultPuzzleSolver(final Puzzle puzzle) {
        this.puzzle = Objects.requireNonNull(puzzle, "Parameter 'puzzle' must not be null.");
        this.puzzleAnalyser = PuzzleAnalyserFactory.createPuzzleAnalyserFor(puzzle);
    }

    @Override
    public void solve() {
        Optional<Move> nextMove;
        do {
            nextMove = getNextMove();
            if (nextMove.isPresent()) {
                Move nextBridge = nextMove.get();
                nextBridge.apply();
            }
        } while (nextMove.isPresent());
    }

    @Override
    public Optional<Move> getNextMove() {
        Optional<Move> safeMove = getSafeMove();
        return safeMove.isPresent() ? safeMove : findSoleNonErrorCausingMove();
    }

    private Optional<Move> findSoleNonErrorCausingMove() {
        Set<Island> islands = puzzleAnalyser.getUnfinishedIslands();

        Move nextMove =null;

        for (Island island : islands) {
            Set<Island> destinations = puzzleAnalyser.getValidBridgeDestinations(island, false);

            for (Island destination : destinations) {
                puzzle.buildBridge(island, destination);

                boolean causesConflict = false;

                Set<Island> islands1 = puzzleAnalyser.getUnfinishedIslands();
                for (Island island1 : islands1) {
                    Set<Island> destinationsTest = puzzleAnalyser.getValidBridgeDestinations(island1, false);
                    if (destinationsTest.isEmpty()) {
                        causesConflict = true;
                        break;
                    }
                }

                puzzle.tearDownBridge(island, destination);
                if (!causesConflict) {
                    if (nextMove == null) {
                        nextMove = Move.create(puzzle, island, destination);
                    } else {
                        // Island has more than one destination that do not lead to a direct conflict
                        nextMove = null;
                        break;
                    }
                }
            }

            if (nextMove != null) {
                break;
            }
        }

        return Optional.ofNullable(nextMove);
    }

    private Optional<Move> getSafeMove() {
        Move safeMove = null;

        for (Island island : puzzleAnalyser.getUnfinishedIslands()) {
            final Set<Island> possibleDestinations = puzzleAnalyser.getValidBridgeDestinations(island, false);

            if (!possibleDestinations.isEmpty()) {
                int remainingBridges = island.getRemainingBridges();
                int existingBridgesToPossibleDestinations = 0;

                for (Island destination : possibleDestinations) {
                    if (island.getBridgeTo(destination).isPresent()) {
                        existingBridgesToPossibleDestinations++;
                    }
                }

                for (Island destination : possibleDestinations) {
                    int existingToDestination = 0;
                    if (island.getBridgeTo(destination).isPresent()) {
                        existingToDestination = 1;
                    }

                    if (isSave(remainingBridges + existingBridgesToPossibleDestinations, possibleDestinations.size(), existingToDestination)) {
                        safeMove = Move.create(puzzle, island, destination);
                        break;
                    }
                }

                if (safeMove != null) {
                    break;
                }
            }
        }

        return Optional.ofNullable(safeMove);
    }

    private boolean isSave(int requiredBridges, int possibleDestinations, int existingInDirection) {
        int saveBridgesInEveryDirection = 0;
        if (requiredBridges == 2 * possibleDestinations) {
            saveBridgesInEveryDirection = 2;
        } else if (requiredBridges == 2 * possibleDestinations - 1) {
            saveBridgesInEveryDirection = 1;
        }

        return existingInDirection < saveBridgesInEveryDirection;
    }
}
