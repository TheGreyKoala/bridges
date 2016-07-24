package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.analyser.PuzzleAnalyser;
import de.feu.ps.bridges.analyser.PuzzleAnalyserFactory;
import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.model.*;

import java.util.*;

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

    private Optional<Move> getSafeMove() {
        for (final Island island : puzzleAnalyser.getUnfinishedIslands()) {
            final Set<Island> possibleDestinations = puzzleAnalyser.getSafeBridgeDestinations(island);

            if (!possibleDestinations.isEmpty()) {
                int remainingBridges = island.getRemainingBridges();
                int existingBridgesToPossibleDestinations = getNumberOfExistingBridges(island, possibleDestinations);

                for (Island destination : possibleDestinations) {
                    int existingToDestination = getNumberOfExistingBridges(island, destination);

                    if (isSave(remainingBridges + existingBridgesToPossibleDestinations, possibleDestinations.size(), existingToDestination)) {
                        return Optional.of(Move.create(puzzle, island, destination));
                    }
                }
            }
        }

        return Optional.empty();
    }

    private int getNumberOfExistingBridges(final Island source, final Set<Island> destinations) {
        int numberOfExistingBridges = 0;
        for (Island destination : destinations) {
            if (source.getBridgeTo(destination).isPresent()) {
                // No need to check if bridge is a double bridge
                // because an island with two existing bridges will not be returned by getSafeBridgeDestinations
                numberOfExistingBridges++;
            }
        }
        return numberOfExistingBridges;
    }

    private int getNumberOfExistingBridges(final Island source, final Island destination) {
        Set<Island> destinations = new HashSet<>();
        destinations.add(destination);
        return getNumberOfExistingBridges(source, destinations);
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

    private Optional<Move> findSoleNonErrorCausingMove() {
        Move nextMove = null;

        for (Island island : puzzleAnalyser.getUnfinishedIslands()) {
            for (Island destination : puzzleAnalyser.getSafeBridgeDestinations(island)) {
                if (!causesImmediateConflict(island, destination)) {
                    if (nextMove == null) {
                        nextMove = Move.create(puzzle, island, destination);
                    } else {
                        // Island has more than one destination that does not lead to a direct conflict
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

    private boolean causesImmediateConflict(final Island source, final Island destination) {
        puzzle.buildBridge(source, destination);
        boolean causesConflict = puzzleAnalyser.getStatus() == PuzzleStatus.UNSOLVABLE;
        puzzle.tearDownBridge(source, destination);
        return causesConflict;
    }
}
