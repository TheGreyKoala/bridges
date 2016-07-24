package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import java.util.*;

/**
 * @author Tim Gremplewski
 */
class StatusAnalyser {

    private final Puzzle puzzle;
    private final MoveAnalyser moveAnalyser;

    StatusAnalyser(final Puzzle puzzle, final MoveAnalyser moveAnalyser) {
        this.moveAnalyser = Objects.requireNonNull(moveAnalyser, "Parameter 'moveAnalyser' must not be null.");
        this.puzzle = Objects.requireNonNull(puzzle, "Parameter 'puzzle' must not be null.");
    }

    PuzzleStatus getStatus() {
        PuzzleStatus status = PuzzleStatus.UNSOLVABLE;
        final Set<Island> unfinishedIslands = moveAnalyser.getUnfinishedIslands();

        if (unfinishedIslands.isEmpty()) {
            final Set<Island> allIslands = puzzle.getIslands();
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

    private boolean allIslandsConnected(final Set<Island> allIslands) {
        Optional<Island> optionalIsland = allIslands.stream().findFirst();

        if (optionalIsland.isPresent()) {
            return allIslands.size() == findAllIndirectlyConnectedIslands(optionalIsland.get()).size();
        }

        // If a puzzle has no islands, it is treated as if all islands were connected
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
        return unfinishedIslands.stream().allMatch(island -> !moveAnalyser.getValidBridgeDestinations(island, false).isEmpty());
    }
}
