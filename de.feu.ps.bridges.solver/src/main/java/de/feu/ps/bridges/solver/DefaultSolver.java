package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.BridgeImpl;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tim Gremplewski
 */
public class DefaultSolver implements Solver {

    private final Puzzle puzzle;

    private DefaultSolver(final Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public static Solver createSolverFor(final Puzzle puzzle) {
        return new DefaultSolver(puzzle);
    }

    public void solve() {
        Optional<Bridge> nextMove;
        do {
            nextMove = getNextMove();
            if (nextMove.isPresent()) {
                puzzle.addBridge(nextMove.get());
            }
        } while (nextMove.isPresent());
    }

    public Optional<Bridge> getNextMove() {
        Optional<Bridge> bridge = getSafeMove();
        return bridge.isPresent() ? bridge : findSoleNonErrorCausingMove();
    }

    private Optional<Bridge> findSoleNonErrorCausingMove() {
        Set<Island> islands = puzzle.getUnfinishedIslands();

        Bridge nextMove = null;

        for (Island island : islands) {
            Set<Island> reachableUnfinishedNeighbours = getReachableUnfinishedNeighbours(puzzle, island);
            Set<Island> destinations = reachableUnfinishedNeighbours.parallelStream().filter(island1 -> causesNoIsolation(puzzle, island, island1)).collect(Collectors.toSet());

            for (Island destination : destinations) {
                Bridge tryBridge = new BridgeImpl(island, destination, false);
                puzzle.addBridge(tryBridge);

                boolean causesConflict = false;

                Set<Island> islands1 = puzzle.getUnfinishedIslands();
                for (Island island1 : islands1) {
                    Set<Island> reachableTest = getReachableUnfinishedNeighbours(puzzle, island1);
                    Set<Island> destinationsTest = reachableTest.parallelStream().filter(island_1 -> causesNoIsolation(puzzle, island1, island_1)).collect(Collectors.toSet());
                    if (destinationsTest.isEmpty()) {
                        causesConflict = true;
                        break;
                    }
                }

                puzzle.removeBridge(tryBridge);
                if (!causesConflict) {
                    if (nextMove == null) {
                        nextMove = tryBridge;
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

    private Optional<Bridge> getSafeMove() {
        // TODO check if already solved

        Bridge safeMove = null;

        for (Island island : puzzle.getUnfinishedIslands()) {
            final Set<Island> reachableUnfinishedNeighbours = getReachableUnfinishedNeighbours(puzzle, island);
            final Set<Island>possibleDestinations = reachableUnfinishedNeighbours.parallelStream().filter(island1 -> causesNoIsolation(puzzle, island, island1)).collect(Collectors.toSet());

            if (!possibleDestinations.isEmpty()) {
                int remainingBridges = island.getRemainingBridges();
                int existingBridgesToPossibleDestinations = 0;

                for (Island destination : possibleDestinations) {
                    if (island.isBridgedTo(destination)) {
                        existingBridgesToPossibleDestinations++;
                    }
                }

                for (Island destination : possibleDestinations) {
                    int existingToDestination = 0;
                    if (island.isBridgedTo(destination)) {
                        existingToDestination = 1;
                    }

                    if (isSave(remainingBridges + existingBridgesToPossibleDestinations, possibleDestinations.size(), existingToDestination)) {
                        safeMove = new BridgeImpl(island, destination, false);
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

    private boolean causesNoIsolation(Puzzle puzzle, Island island1, Island island2) {

        Set<Island> visitedIslands = new HashSet<>();
        Queue<Island> islandsToVisit = new LinkedList<>();

        if (island1.getRemainingBridges() > 1) {
            // If remainingBridges == 1, island1 will only be able to reach island2
            Set<Island> reachable = getReachableUnfinishedNeighbours(puzzle, island1);
            islandsToVisit.addAll(reachable);
        }

        islandsToVisit.addAll(island1.getBridgedNeighbours());
        visitedIslands.add(island1);

        if (island2.getRemainingBridges() > 1) {
            // If remainingBridges == 1, island2 will only be able to reach island1
            Set<Island> reachable = getReachableUnfinishedNeighbours(puzzle, island2);
            islandsToVisit.addAll(reachable);
        }

        islandsToVisit.addAll(island2.getBridgedNeighbours());
        visitedIslands.add(island2);

        while (!islandsToVisit.isEmpty()) {
            Island island = islandsToVisit.remove();
            if (!visitedIslands.contains(island)) {
                if (island.getRemainingBridges() > 0) {
                    islandsToVisit.addAll(getReachableUnfinishedNeighbours(puzzle, island));
                }
                islandsToVisit.addAll(island.getBridgedNeighbours());
            }
            visitedIslands.add(island);
        }

        return visitedIslands.containsAll(puzzle.getIslands());
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

    private Set<Island> getReachableUnfinishedNeighbours(Puzzle puzzle, final Island island) {
        return island
            .getNeighbours().stream()
            .filter(this::islandNeedsMoreBridges)
            .filter(neighbour -> !island.isBridgedTo(neighbour) || !island.getBridgeTo(neighbour).isDoubleBridge())
            .filter(neighbour -> noIntersectingBridge(puzzle, island, neighbour))
            .collect(Collectors.toSet());
    }

    private boolean noIntersectingBridge(Puzzle puzzle, Island island1, Island island2) {
        return !puzzle.isAnyBridgeCrossing(island1.getPosition(), island2.getPosition());
    }

    private boolean islandNeedsMoreBridges(Island island) {
        // TODO Use getStatus?
        return island.getRemainingBridges() > 0;
    }
}
