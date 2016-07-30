package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.toolkit.PuzzleToolkitFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

import static de.feu.ps.bridges.gui.events.ErrorEvent.GENERATING_PUZZLE_FAILED;

/**
 * @author Tim Gremplewski
 */
class PuzzleGeneration {
    private static final Logger LOGGER = Logger.getLogger(PuzzleGeneration.class.getName());
    private final GameState gameState;

    PuzzleGeneration(final GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Generates a new random puzzle.
     */
    void newPuzzle() {
        try {
            gameState.setPuzzleToolkit(PuzzleToolkitFactory.createForGeneratedPuzzle());
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while generating a random puzzle.", e);
            gameState.broadcastEvent(GENERATING_PUZZLE_FAILED);
        }
    }

    /**
     * Generates a new random puzzle with the given amount of columns and rows.
     * @param columns amount of columns of the new puzzle.
     * @param rows amount of rows of the new puzzle.
     */
    void newPuzzle(final int columns, final int rows) {
        try {
            gameState.setPuzzleToolkit(PuzzleToolkitFactory.createForGeneratedPuzzle(columns, rows));
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while generating a random puzzle.", e);
            gameState.broadcastEvent(GENERATING_PUZZLE_FAILED);
        }
    }

    /**
     * Generate a new random puzzle with the given amount of columns, rows and islands.
     * @param columns amount of columns of the new puzzle.
     * @param rows amount of rows of the new puzzle.
     * @param islands amount of islands in the new puzzle.
     */
    public void newPuzzle(final int columns, final int rows, final int islands) {
        try {
            gameState.setPuzzleToolkit(PuzzleToolkitFactory.createForGeneratedPuzzle(columns, rows, islands));
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while generating a random puzzle.", e);
            gameState.broadcastEvent(GENERATING_PUZZLE_FAILED);
        }
    }
}
