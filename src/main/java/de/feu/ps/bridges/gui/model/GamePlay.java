package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.gui.events.GameStateEvent;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.feu.ps.bridges.gui.events.ErrorEvent.*;

/**
 * @author Tim Gremplewski
 */
class GamePlay {

    private static final Logger LOGGER = Logger.getLogger(GamePlay.class.getName());
    private final GameState gameState;

    GamePlay(final GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Restart the current puzzle.
     */
    void restartPuzzle() {
        try {
            gameState.removeAllBridges();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while restarting a puzzle.", e);
            gameState.broadcastEvent(RESTARTING_PUZZLE_FAILED);
        }
    }

    /**
     * Try to build a bridge to the given {@link Island} in the given {@link Direction}.
     * @param island {@link} Island to add a bridge to.
     * @param direction {@link Direction} to build the bridge.
     */
    public void tryBuildBridge(final Island island, final Direction direction) {
        try {
            Optional<Bridge> optionalBridge = gameState.getPuzzleToolkit().tryBuildBridge(island, direction);
            if (optionalBridge.isPresent()) {
                gameState.addBridge(optionalBridge.get());
            } else {
                gameState.broadcastEvent(GameStateEvent.INVALID_MOVE);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while building a bridge.", e);
            gameState.broadcastEvent(BUILD_BRIDGE_FAILED);
        }
    }

    /**
     * Tear down the bridge of the given island in the given direction.
     * @param island island to remove the bridge from.
     * @param direction direction of the bridge to remove.
     */
    public void tearDownBridge(final Island island, final Direction direction) {
        try {
            Optional<Bridge> optionalBridge = gameState.getPuzzleToolkit().tearDownBridge(island, direction);
            if (optionalBridge.isPresent()) {
                gameState.removeBridge(optionalBridge.get());
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while tearing down a bridge.", e);
            gameState.broadcastEvent(TEAR_DOWN_BRIDGE_FAILED);
        }
    }
}
