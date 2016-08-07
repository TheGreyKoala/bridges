package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.model.Model;
import javafx.scene.layout.StackPane;

import java.util.*;
import java.util.function.Function;

/**
 * Helper class to manage the visible bridges in the gui application.
 * @author Tim Gremplewski
 */
class BridgesLayer extends StackPane {

    private final Map<de.feu.ps.bridges.model.Bridge, Bridge> bridges;
    private final Function<de.feu.ps.bridges.model.Island, Island> islandMapper;
    private final Model model;

    private Bridge latestBridge;

    /**
     * Creates a new instance.
     * @param islandMapper function that will map a given {@link de.feu.ps.bridges.model.Island} to its visual counterpart.
     * @param model model to use.
     */
    BridgesLayer(final Function<de.feu.ps.bridges.model.Island, Island> islandMapper, final Model model) {
        this.islandMapper = islandMapper;
        this.model = model;
        bridges = new HashMap<>();
    }

    /**
     * Create a new visual representation of the given {@link de.feu.ps.bridges.model.Bridge} and add it to this layer.
     * @param bridge bridge to visualize.
     * @return the newly created bridge.
     */
    Bridge addBridge(final de.feu.ps.bridges.model.Bridge bridge) {
        final Bridge addedBridge = bridges.containsKey(bridge) ? updateIsDoubleBridge(bridge) : addNewBridge(bridge);
        setLatestBridge(addedBridge);
        return addedBridge;
    }

    private Bridge updateIsDoubleBridge(final de.feu.ps.bridges.model.Bridge bridge) {
        final Bridge addedBridge = bridges.get(bridge);
        addedBridge.setDoubleBridge(bridge.isDoubleBridge());
        return addedBridge;
    }

    private Bridge addNewBridge(final de.feu.ps.bridges.model.Bridge bridge) {
        final Island island1 = islandMapper.apply(bridge.getIsland1());
        final Island island2 = islandMapper.apply(bridge.getIsland2());
        final Bridge newBridge = new Bridge(bridge.isHorizontal(), island1, island2);
        newBridge.setDoubleBridge(bridge.isDoubleBridge());
        bridges.put(bridge, newBridge);
        getChildren().add(0, newBridge);
        return newBridge;
    }

    private void setLatestBridge(final Bridge latestBridge) {
        if (this.latestBridge != null) {
            this.latestBridge.setLatestBridge(false);
        }
        latestBridge.setLatestBridge(true);
        this.latestBridge = latestBridge;
    }

    /**
     * Remove the visual representation of the given {@link de.feu.ps.bridges.model.Bridge} from this layer.
     * @param bridge Bridge to be removed.
     * @return the removed bridge.
     */
    Bridge removeBridge(final de.feu.ps.bridges.model.Bridge bridge) {
        final Bridge removedBridge = bridges.get(bridge);
        if (removedBridge.isDoubleBridge()) {
            removedBridge.setDoubleBridge(false);
        } else {
            bridges.remove(bridge);
            getChildren().remove(removedBridge);
        }
        setLatestBridgeFromGameState();
        return removedBridge;
    }

    private void setLatestBridgeFromGameState() {
        final Optional<de.feu.ps.bridges.model.Bridge> latestBridge = model.getGameState().getLatestBridge();
        if (latestBridge.isPresent()) {
            setLatestBridge(bridges.get(latestBridge.get()));
        } else {
            this.latestBridge = null;
        }
    }

    /**
     * Remove all bridges from this layer.
     * @return the removed bridges.
     */
    Set<Bridge> removeAllBridges() {
        getChildren().clear();
        this.latestBridge = null;
        final Set<Bridge> removedBridges = new HashSet<>(bridges.values());
        bridges.clear();
        return removedBridges;
    }
}
