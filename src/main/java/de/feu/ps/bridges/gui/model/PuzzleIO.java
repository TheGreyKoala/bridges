package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.toolkit.PuzzleToolkitFactory;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.feu.ps.bridges.gui.events.ErrorEvent.LOADING_PUZZLE_FAILED;
import static de.feu.ps.bridges.gui.events.ErrorEvent.SAVING_PUZZLE_FAILED;

/**
 * @author Tim Gremplewski
 */
class PuzzleIO {
    private static final Logger LOGGER = Logger.getLogger(PuzzleIO.class.getName());

    private final GameState gameState;

    PuzzleIO(final GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Load the puzzle from the file file.
     * @param sourceFile location of the puzzle to be loaded.
     */
    void loadPuzzle(final File sourceFile) {
        try {
            gameState.setPuzzleToolkit(PuzzleToolkitFactory.createForLoadedPuzzle(sourceFile));
            gameState.setSourceFile(sourceFile);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while loading a puzzle.", e);
            gameState.broadcastEvent(LOADING_PUZZLE_FAILED);
        }
    }

    /**
     * Save the current puzzle in the file it was loaded from.
     */
    public void savePuzzle() {
        try {
            savePuzzleAs(gameState.getSourceFile());
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while saving a puzzle.", e);
            gameState.broadcastEvent(SAVING_PUZZLE_FAILED);
        }
    }

    /**
     * Save the current puzzle in the given location.
     * @param destinationFile destination for the puzzle to be saved.
     */
    void savePuzzleAs(final File destinationFile) {
        try {
            gameState.getPuzzleToolkit().savePuzzle(destinationFile);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while saving a puzzle.", e);
            gameState.broadcastEvent(SAVING_PUZZLE_FAILED);
        }
    }
}
