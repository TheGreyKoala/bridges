package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link PuzzleAnalyser}.
 * @author Tim Gremplewski
 */
class DefaultPuzzleAnalyser implements PuzzleAnalyser {

    private final Puzzle puzzle;

    /**
     * Creates a new instance.
     * @param puzzle {@link Puzzle} to be analysed.
     */
    DefaultPuzzleAnalyser(final Puzzle puzzle) {
        this.puzzle = Objects.requireNonNull(puzzle, "Parameter 'puzzle' must not be empty.");
    }

    @Override
    public PuzzleStatus getStatus() {
        PuzzleStatus status = PuzzleStatus.UNSOLVABLE;
        final Set<Island> unfinishedIslands = getUnfinishedIslands();

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

    @Override
    public Set<Island> getUnfinishedIslands() {
        return puzzle.getIslands().stream()
                .filter(island -> islandCanTakeBridge(island, false))
                .collect(Collectors.toSet());
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
        return unfinishedIslands.stream().allMatch(island -> !getValidBridgeDestinations(island, false).isEmpty());
    }

    @Override
    public Set<Island> getValidBridgeDestinations(final Island island, final boolean doubleBridge) {
        Objects.requireNonNull(island, "Parameter 'island' must not be null.");

        if (!puzzle.getIslands().contains(island)) {
            throw new IllegalStateException("Given island does not belong to the puzzle.");
        }

        final Set<Island> reachableUnfinishedNeighbours = getReachableUnfinishedNeighbours(puzzle, island, doubleBridge);
        return reachableUnfinishedNeighbours.stream()
                .filter(island1 -> causesNoIsolation(puzzle, island, island1, doubleBridge))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValidMove(final Island island, final Direction direction, final boolean doubleBridge) {
        Objects.requireNonNull(island, "Parameter 'island' must not be null");
        Objects.requireNonNull(direction, "Parameter 'direction' must not be null");

        Optional<Island> neighbour = island.getNeighbour(direction);
        return neighbour.isPresent() && isValidMove(island, neighbour.get(), doubleBridge);
    }

    @Override
    public boolean isValidMove(final Island island1, final Island island2, final boolean doubleBridge) {
        Objects.requireNonNull(island1, "Parameter 'island1' must not be null");
        Objects.requireNonNull(island2, "Parameter 'island2' must not be null");

        // Do not use getValidBridgeDestinations, because a move that causes isolation can still be valid (but not safe)
        return islandCanTakeBridge(island1, doubleBridge)
                && getReachableUnfinishedNeighbours(puzzle, island1, doubleBridge).contains(island2);
    }

    private Set<Island> getReachableUnfinishedNeighbours(final Puzzle puzzle, final Island island, final boolean doubleBridge) {
        return island
                .getNeighbours().stream()
                .filter(island1 -> islandCanTakeBridge(island1, doubleBridge))
                .filter(neighbour -> {
                    Optional<Bridge> optionalBridge = island.getBridgeTo(neighbour);
                    return !optionalBridge.isPresent() || !optionalBridge.get().isDoubleBridge();
                })
                .filter(neighbour -> noIntersectingBridge(puzzle, island, neighbour))
                .collect(Collectors.toSet());
    }

    private boolean noIntersectingBridge(Puzzle puzzle, Island island1, Island island2) {
        return !puzzle.isAnyBridgeCrossing(island1.getPosition(), island2.getPosition());
    }

    private boolean islandCanTakeBridge(Island island, boolean doubleBridge) {
        return island.getRemainingBridges() >= (doubleBridge ? 2 : 1);
    }

    private boolean causesNoIsolation(Puzzle puzzle, Island island1, Island island2, boolean doubleBridge) {

        Set<Island> visitedIslands = new HashSet<>();
        Queue<Island> islandsToVisit = new LinkedList<>();

        if (island1.getRemainingBridges() > 1) {
            // If remainingBridges == 1, island1 will only be able to reach island2
            Set<Island> reachable = getReachableUnfinishedNeighbours(puzzle, island1, doubleBridge);
            islandsToVisit.addAll(reachable);
        }

        islandsToVisit.addAll(island1.getBridgedNeighbours());
        visitedIslands.add(island1);

        if (island2.getRemainingBridges() > 1) {
            // If remainingBridges == 1, island2 will only be able to reach island1
            Set<Island> reachable = getReachableUnfinishedNeighbours(puzzle, island2, doubleBridge);
            islandsToVisit.addAll(reachable);
        }

        islandsToVisit.addAll(island2.getBridgedNeighbours());
        visitedIslands.add(island2);

        while (!islandsToVisit.isEmpty()) {
            Island island = islandsToVisit.remove();
            if (!visitedIslands.contains(island)) {
                if (island.getRemainingBridges() > 0) {
                    islandsToVisit.addAll(getReachableUnfinishedNeighbours(puzzle, island, doubleBridge));
                }
                islandsToVisit.addAll(island.getBridgedNeighbours());
            }
            visitedIslands.add(island);
        }

        return visitedIslands.containsAll(puzzle.getIslands());
    }
}
