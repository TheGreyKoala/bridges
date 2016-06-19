package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tim Gremplewski
 */
public class DefaultAnalyser implements Analyser {

    private final Puzzle puzzle;

    private DefaultAnalyser(final Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public static DefaultAnalyser createAnalyserFor(final Puzzle puzzle) {
        return new DefaultAnalyser(puzzle);
    }

    @Override
    public PuzzleStatus getStatus() {
        PuzzleStatus status = PuzzleStatus.UNSOLVABLE;
        final Set<Island> allIslands = puzzle.getIslands();
        final Set<Island> unfinishedIslands = getUnfinishedIslands(allIslands);

        if (unfinishedIslands.isEmpty()) {
            if (allIslandsConnected(allIslands)) {
                status = PuzzleStatus.SOLVED;
            }
        } else {
            if (noIsolatedIslands(unfinishedIslands)) {
                status = PuzzleStatus.UNSOLVED;
            }
        }

        return status;
    }

    private Set<Island> getUnfinishedIslands(final Set<Island> islands) {
        return islands.stream().filter(this::islandNeedsMoreBridges).collect(Collectors.toSet());
    }

    private boolean allIslandsConnected(final Set<Island> allIslands) {
        Optional<Island> optionalIsland = allIslands.stream().findFirst();

        if (optionalIsland.isPresent()) {
            return allIslands.size() == findAllIndirectlyConnectedIslands(optionalIsland.get()).size();
        }

        // TODO: What if puzzle has no islands?
        return true;
    }

    private Set<Island> findAllIndirectlyConnectedIslands(final Island startIsland) {
        final Set<Island> visitedIslands = new HashSet<>();
        final Queue<Island> islandsToVisit = new LinkedList<>();

        islandsToVisit.add(startIsland);

        do {
            Island island = islandsToVisit.remove();
            if (!visitedIslands.contains(island)) {
                islandsToVisit.addAll(island.getBridgedNeighbours());
                visitedIslands.add(island);
            }
        } while (!islandsToVisit.isEmpty());

        return visitedIslands;
    }

    private boolean noIsolatedIslands(final Set<Island> unfinishedIslands) {
        return unfinishedIslands.stream().allMatch(this::hasValidBridgeDestinations);
    }

    @Override
    public boolean hasValidBridgeDestinations(final Island island) {
        return !getValidBridgeDestinations(island).isEmpty();
    }

    @Override
    public Set<Island> getValidBridgeDestinations(final Island island) {
        //TODO Check that island belongs to puzzle
        final Set<Island> reachableUnfinishedNeighbours = getReachableUnfinishedNeighbours(puzzle, island);
        return reachableUnfinishedNeighbours.parallelStream().filter(island1 -> causesNoIsolation(puzzle, island, island1)).collect(Collectors.toSet());
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
}
