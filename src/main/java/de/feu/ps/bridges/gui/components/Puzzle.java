package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.model.Model;
import javafx.scene.layout.StackPane;

import java.util.*;

/**
 * Visual representation of a {@link de.feu.ps.bridges.model.Puzzle} in the gui application.
 * @author Tim Gremplewski
 */
public class Puzzle extends StackPane {

    private final Board board;
    private final BridgesLayer bridgesLayer;

    /**
     * Creates a new instance.
     * @param puzzle Puzzle to be visualized.
     * @param model Model to use.
     */
    public Puzzle(final de.feu.ps.bridges.model.Puzzle puzzle, final Model model) {
        board = new Board(puzzle.getColumnsCount(), puzzle.getRowsCount(), model);
        bridgesLayer = new BridgesLayer(board::getIsland, model);
        puzzle.getIslands().forEach(this::addIsland);
        puzzle.getBridges().forEach(this::addBridge);
        getChildren().addAll(bridgesLayer, board);
    }

    private void addIsland(final de.feu.ps.bridges.model.Island island) {
        board.addIsland(island);
    }

    /**
     * Create a visual representation of the given bridge and add it to this puzzle.
     * @param bridge Bridge to be visualized.
     */
    public void addBridge(final de.feu.ps.bridges.model.Bridge bridge) {
        final Bridge addedBridge = bridgesLayer.addBridge(bridge);
        updateIslands(addedBridge);
    }

    private void updateIslands(final Bridge bridge) {
        bridge.getIsland1().update();
        bridge.getIsland2().update();
    }

    /**
     * Remove the visual representation of the given bridge from this puzzle.
     * @param bridge Bridge to be removed.
     */
    public void removeBridge(final de.feu.ps.bridges.model.Bridge bridge) {
        final Bridge removedBridge = bridgesLayer.removeBridge(bridge);
        updateIslands(removedBridge);
    }

    /**
     * Remove all bridges from this puzzle.
     */
    public void removeAllBridges() {
        final Set<Bridge> removedBridges = bridgesLayer.removeAllBridges();
        removedBridges.forEach(this::updateIslands);
    }

    /**
     * Determine whether the islands in this puzzle should show their remaining amount of bridges or their required amount of bridges.
     * @param showRemainingBridges if true, the islands will show their remaining bridges.
     */
    public void setShowRemainingBridges(final boolean showRemainingBridges) {
        board.setShowRemainingBridges(showRemainingBridges);
    }
}
