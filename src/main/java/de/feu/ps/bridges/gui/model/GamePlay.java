package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.feu.ps.bridges.gui.events.ErrorEvent.*;
import static de.feu.ps.bridges.gui.events.PuzzleEvent.INVALID_MOVE;

/**
 * Helper class that offers operations to build or remove bridges.
 * @author Tim Gremplewski
 */
class GamePlay {

    private static final Logger LOGGER = Logger.getLogger(GamePlay.class.getName());
    private final GameState gameState;

    /**
     * Creates a new instance that operates on the given {@link GameState}.
     * @param gameState {@link GameState} to operate on.
     */
    GamePlay(final GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Restart the puzzle.
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
    void tryBuildBridge(final Island island, final Direction direction) {
        try {
            Optional<Bridge> optionalBridge = gameState.getPuzzleToolkit().tryBuildBridge(island, direction);
            if (optionalBridge.isPresent()) {
                gameState.addBridge(optionalBridge.get());
            } else {
                gameState.broadcastEvent(INVALID_MOVE, null);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while building a bridge.", e);
            gameState.broadcastEvent(BUILDING_BRIDGE_FAILED);
        }
    }

    /**
     * Tear down the bridge of the given island in the given direction.
     * @param island island to remove the bridge from.
     * @param direction direction of the bridge to remove.
     */
    void tearDownBridge(final Island island, final Direction direction) {
        try {
            Optional<Bridge> optionalBridge = gameState.getPuzzleToolkit().tearDownBridge(island, direction);
            if (optionalBridge.isPresent()) {
                gameState.removeBridge(optionalBridge.get());
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while tearing down a bridge.", e);
            gameState.broadcastEvent(TEARING_DOWN_BRIDGE_FAILED);
        }
    }
}
