package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.toolkit.PuzzleToolkit;

import java.io.File;
import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.*;

/**
 * Class that stores the current state of the game.
 * @author Tim Gremplewski
 */
public class GameState extends EventBroadcaster {

    private static final Logger LOGGER = Logger.getLogger(GameState.class.getName());
    private Puzzle puzzle;
    private File sourceFile;
    private PuzzleToolkit puzzleToolkit;
    private PuzzleStatus puzzleStatus;
    private LinkedList<Bridge> addedBridges;

    /**
     * Creates a new instance.
     */
    public GameState() {
    }

    /**
     * Set the {@link PuzzleToolkit}.
     * @param puzzleToolkit the puzzle toolkit.
     */
    void setPuzzleToolkit(final PuzzleToolkit puzzleToolkit) {
        this.puzzleToolkit = puzzleToolkit;
        puzzle = puzzleToolkit.getPuzzle();
        addedBridges = new LinkedList<>();
        clearAddedBridgesAndBroadcastEvents(PUZZLE_CHANGED, puzzle);
    }

    private void clearAddedBridgesAndBroadcastEvents(final PuzzleEvent puzzleEvent, final Object eventParameter) {
        addedBridges.clear();
        refreshPuzzleStatus();
        broadcastEvent(puzzleEvent, eventParameter);
    }

    /**
     * Get the current puzzle.
     * If no puzzle has been set yet, the returned {@link Optional} will be empty.
     * @return {@link Optional} containing the current puzzle.
     */
    public Optional<Puzzle> getPuzzle() {
        return Optional.ofNullable(puzzle);
    }

    /**
     * Get the file from which the current puzzle was loaded.
     * @return the file from which the current puzzle was loaded.
     */
    File getSourceFile() {
        return sourceFile;
    }

    /**
     * Get the current puzzle toolkit.
     * @return the current puzzle toolkit.
     */
    PuzzleToolkit getPuzzleToolkit() {
        return puzzleToolkit;
    }

    /**
     * Get the status of the current puzzle.
     * @return the status of the current puzzle.
     */
    PuzzleStatus getPuzzleStatus() {
        // TODO: Handle null in calling methods
        return puzzleStatus;
    }

    /**
     * Set the file from where the current puzzle was loaded.
     * @param sourceFile the file from where the current puzzle was loaded.
     */
    void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Indicates whether the current puzzle was loaded from a file, that is still known.
     * @return true, if the current puzzle was loaded from a file, that is still known.
     */
    public boolean isPuzzleSourceFileKnown() {
        return this.sourceFile != null;
    }

    /**
     * Get the latest bridge that was added to the puzzle.
     * @return the latest bridge that was added to the puzzle.
     */
    public Optional<Bridge> getLatestBridge() {
        return addedBridges.isEmpty() ? Optional.empty() : Optional.ofNullable(addedBridges.getLast());
    }

    /**
     * Remove all bridges from the current puzzle.
     */
    void removeAllBridges() {
        if (getPuzzle().isPresent()) {
            puzzle.removeAllBridges();
        }
        clearAddedBridgesAndBroadcastEvents(PUZZLE_RESET, null);
    }

    /**
     * Add the given bridge to the current puzzle.
     * @param bridge bridge to add.
     */
    void addBridge(final Bridge bridge) {
        addedBridges.add(bridge);
        broadcastEvent(BRIDGE_ADDED, bridge);
        refreshPuzzleStatus();
    }

    /**
     * Remove the given bridge from the current puzzle.
     * @param bridge bridge to remove.
     */
    void removeBridge(final Bridge bridge) {
        addedBridges.removeLastOccurrence(bridge);
        broadcastEvent(BRIDGE_REMOVED, bridge);
        refreshPuzzleStatus();
    }

    private void refreshPuzzleStatus() {
        try {
            final PuzzleStatus oldPuzzleStatus = puzzleStatus;
            puzzleStatus = puzzleToolkit.getPuzzleStatus();
            if (puzzleStatus != null && oldPuzzleStatus != puzzleStatus) {
                broadcastEvent(PUZZLE_STATUS_CHANGED, puzzleStatus);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while tearing down a bridge.", e);
            puzzleStatus = null;
        }
    }
}
