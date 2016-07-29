package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.toolkit.PuzzleToolkit;

import java.io.File;
import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_CHANGED;
import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_STATUS_CHANGED;

/**
 * @author Tim Gremplewski
 */
public class GameState extends EventBroadcaster {
    private static final Logger LOGGER = Logger.getLogger(GameState.class.getName());


    private Puzzle puzzle;
    private File sourceFile;
    private PuzzleToolkit puzzleToolkit;
    private PuzzleStatus puzzleStatus;
    private LinkedList<Bridge> addedBridges;

    void setPuzzleToolkit(final PuzzleToolkit puzzleToolkit) {
        this.puzzleToolkit = puzzleToolkit;
        puzzle = puzzleToolkit.getPuzzle();
        addedBridges = new LinkedList<>();
        clearAddedBridgesAndBroadcastEvents();
    }

    private void clearAddedBridgesAndBroadcastEvents() {
        addedBridges.clear();
        refreshPuzzleStatus();
        broadcastEvent(PUZZLE_CHANGED);
    }

    public Optional<Puzzle> getPuzzle() {
        return Optional.ofNullable(puzzle);
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public PuzzleToolkit getPuzzleToolkit() {
        return puzzleToolkit;
    }

    public PuzzleStatus getPuzzleStatus() {
        return puzzleStatus;
    }

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
     * Indicates if the given bridge is the last one that was added to the current puzzle.
     * @param bridge {@link Bridge} to check.
     * @return true, if the given {@link Bridge} is the last one that was added to the puzzle, false otherwise.
     */
    public boolean isLatestBridge(final Bridge bridge) {
        return !addedBridges.isEmpty() && addedBridges.getLast().equals(bridge);
    }

    void removeAllBridges() {
        if (getPuzzle().isPresent()) {
            puzzle.removeAllBridges();
        }
        clearAddedBridgesAndBroadcastEvents();
    }

    void addBridge(final Bridge bridge) {
        addedBridges.add(bridge);
        broadcastEvent(PUZZLE_CHANGED);
        refreshPuzzleStatus();
    }

    void removeBridge(final Bridge bridge) {
        addedBridges.removeLastOccurrence(bridge);
        broadcastEvent(PUZZLE_CHANGED);
        refreshPuzzleStatus();
    }

    private void refreshPuzzleStatus() {
        try {
            final PuzzleStatus oldPuzzleStatus = puzzleStatus;
            puzzleStatus = puzzleToolkit.getPuzzleStatus();
            if (puzzleStatus != null && oldPuzzleStatus != puzzleStatus) {
                broadcastEvent(PUZZLE_STATUS_CHANGED);
            }
        } catch (final Exception e) {
            // TODO: Handle null in calling methods
            LOGGER.log(Level.SEVERE, "Unexpected error while tearing down a bridge.", e);
            puzzleStatus = null;
        }
    }
}
