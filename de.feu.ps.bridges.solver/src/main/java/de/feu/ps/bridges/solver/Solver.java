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
public class Solver {

    private Map<Island, Set<Bridge>> saveMoves;

    public Solver() {
        saveMoves = new HashMap<>();
    }

    public void solve(Puzzle puzzle) {
        Bridge addedBridge;
        do {
            addedBridge = getSafeMove(puzzle);

            if (addedBridge != null) {
                puzzle.addBridge(addedBridge);
            }
        } while (/*puzzle.getStatus() != PuzzleStatus.SOLVED &&*/ addedBridge != null);

        int i = 5;
    }

    public Bridge getSafeMove(Puzzle puzzle) {
        // TODO check if already solved

        Set<Island> islands = puzzle.getIslands();

        Bridge safeMove = null;

        for (Island island : islands) {
            if (island.getRemainingBridges() == 0) {
                continue;
            }

            final Set<Island> reachableUnfinishedNeighbours = getReachableUnfinishedNeighbours(puzzle, island);
//            final Set<Island> islandsThatNeedToConnectTo = islandsThatNeedToConnectTo(puzzle, island, reachableUnfinishedNeighbours);
            final Set<Island> islandsThatNeedToConnectTo = new HashSet<>();

            // Some islands may have no other option than bridge to the current island
            // These islands need to be looked at first, otherwise all reachable neighbours are possible destinations
            final Set<Island> possibleDestinations;
            if (islandsThatNeedToConnectTo.isEmpty()) {
                possibleDestinations = reachableUnfinishedNeighbours;
            } else {
                possibleDestinations = islandsThatNeedToConnectTo;
            }

            if (!possibleDestinations.isEmpty()) {
                int remainingBridges = island.getRemainingBridges();

                int saveBridgesToEveryPossibleDestination = 0;

                if (remainingBridges == 2 * possibleDestinations.size()) {
                    saveBridgesToEveryPossibleDestination = 2;
                } else if (remainingBridges == 2 * possibleDestinations.size() - 1) {
                    saveBridgesToEveryPossibleDestination = 1;
                }

                if (saveBridgesToEveryPossibleDestination > 0 && island.getRemainingBridges() > 0) {

                    Island finalDestination = null;
                    boolean createDoubleBridge = false;

                    Optional<Island> optionalDestination =
                        possibleDestinations.stream()
                            .filter(destination -> !island.isBridgedTo(destination))
                            .findFirst();

                    if (optionalDestination.isPresent()) {
                        finalDestination = optionalDestination.get();
                        createDoubleBridge = saveBridgesToEveryPossibleDestination == 2;
                    } else if (saveBridgesToEveryPossibleDestination == 2) {
                        optionalDestination =
                            possibleDestinations.stream()
                            .filter(destination -> !island.getBridgeTo(destination).isDoubleBridge())
                            .findFirst();

                        if (optionalDestination.isPresent()) {
                            finalDestination = optionalDestination.get();
                        }
                    }

                    if (finalDestination != null) {
                        safeMove = new BridgeImpl(island, finalDestination, createDoubleBridge);
                        break;
                    }
                }
            }
        }

        return safeMove;
    }

    private Set<Island> getReachableUnfinishedNeighbours(Puzzle puzzle, final Island island) {
        return island
            .getNeighbours().stream()
            .filter(neighbour -> !island.isBridgedTo(neighbour) || !island.getBridgeTo(island).isDoubleBridge())
            .filter(neighbour -> noIntersectingBridge(puzzle, island, neighbour))
            .filter(this::islandNeedsMoreBridges)
            .collect(Collectors.toSet());
    }

    private Set<Island> islandsThatNeedToConnectTo(Puzzle puzzle, Island island, Set<Island> neighbours) {
        return neighbours.stream()
            .filter(neighbour -> {
                Set<Island> reachableUnfinishedNeighbours = getReachableUnfinishedNeighbours(puzzle, neighbour);
                return reachableUnfinishedNeighbours.size() == 1 && reachableUnfinishedNeighbours.contains(island);
            })
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
