package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.analyser.Analyser;
import de.feu.ps.bridges.analyser.DefaultAnalyser;
import de.feu.ps.bridges.model.*;

import java.util.Optional;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class DefaultSolver implements Solver {

    private final Puzzle puzzle;
    private final Analyser analyser;

    private DefaultSolver(final Puzzle puzzle) {
        this.puzzle = puzzle;
        this.analyser = DefaultAnalyser.createAnalyserFor(puzzle);
    }

    public static Solver createSolverFor(final Puzzle puzzle) {
        return new DefaultSolver(puzzle);
    }

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

    public Optional<Move> getNextMove() {
        Optional<Move> safeMove = getSafeMove();
        return safeMove.isPresent() ? safeMove : findSoleNonErrorCausingMove();
    }

    private Optional<Move> findSoleNonErrorCausingMove() {
        Set<Island> islands = puzzle.getUnfinishedIslands();

        Move nextMove =null;

        for (Island island : islands) {
            Set<Island> destinations = analyser.getValidBridgeDestinations(island);

            for (Island destination : destinations) {
                puzzle.buildBridge(island, destination, false);

                boolean causesConflict = false;

                Set<Island> islands1 = puzzle.getUnfinishedIslands();
                for (Island island1 : islands1) {
                    Set<Island> destinationsTest = analyser.getValidBridgeDestinations(island1);
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
        // TODO check if already solved

        Move safeMove = null;

        for (Island island : puzzle.getUnfinishedIslands()) {
            final Set<Island> possibleDestinations = analyser.getValidBridgeDestinations(island);

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
