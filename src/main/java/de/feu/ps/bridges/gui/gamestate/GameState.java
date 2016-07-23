package de.feu.ps.bridges.gui.gamestate;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.toolkit.PuzzleToolkit;
import de.feu.ps.bridges.toolkit.PuzzleToolkitFactory;
import javafx.application.Platform;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the central interface between the gui and the backend functions.
 * It stores the current puzzle, calls backend functions on it and fires events
 * that the gui can react on.
 *
 * @author Tim Gremplewski
 */
public class GameState {

    private static final Logger LOGGER = Logger.getLogger(GameState.class.getName());

    private final ExecutorService executorService;
    private Puzzle puzzle;
    private File sourceFile;
    private final Set<GameStateListener> gameStateListeners;
    private Future<?> currentTask;
    private LinkedList<Bridge> addedBridges;
    private PuzzleToolkit puzzleToolkit;

    /**
     * Creates a new instance.
     */
    public GameState() {
        this.gameStateListeners = new HashSet<>();
        executorService = Executors.newWorkStealingPool();
        addedBridges = new LinkedList<>();
    }

    /**
     * Add a new {@link GameStateListener} that will be notified on events.
     * @param gameStateListener listener to add.
     * @throws NullPointerException if gameStateListener is null.
     */
    public void addGameStateListener(final GameStateListener gameStateListener) {
        Objects.requireNonNull(gameStateListener, "Parameter 'gameStateListener' must not be null.");
        gameStateListeners.add(gameStateListener);
    }

    /**
     * Remove the given {@link GameStateListener} from the list of listener that will be notified.
     * @param gameStateListener listener to remove
     */
    public void removeGameStateListener(final GameStateListener gameStateListener) {
        if (gameStateListener != null) {
            gameStateListeners.remove(gameStateListener);
        }
    }

    private void fireGameStateEvent(GameStateEventType gameStateEventType) {
        GameStateEvent gameStateEvent = new GameStateEvent(gameStateEventType);
        gameStateListeners.forEach(gameStateListener -> gameStateListener.handleGameStateEvent(gameStateEvent));
    }

    /**
     * Get an {@link Optional} containing the current puzzle.
     * If no puzzle is currently loaded, the {@link Optional} will be empty.
     * @return {@link Optional} containing the current puzzle, if any is loaded.
     */
    public Optional<Puzzle> getPuzzle() {
        return Optional.ofNullable(puzzle);
    }

    /**
     * Indicates whether the current puzzle was loaded from a file, that is still known.
     * @return true, if the current puzzle was loaded from a file, that is still known.
     */
    public boolean isPuzzleSourceFileKnown() {
        return this.sourceFile != null;
    }

    /**
     * Generates a new random puzzle and fires a {@link GameStateEventType#PUZZLE_CHANGED} event.
     */
    public void newPuzzle() {
        try {
            puzzleToolkit = PuzzleToolkitFactory.createForGeneratedPuzzle();
            initAfterNewPuzzle();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while generating a random puzzle.", e);
            fireGameStateEvent(GameStateEventType.PUZZLE_GENERATION_FAILED);
        }
    }

    private void initAfterNewPuzzle() {
        puzzle = puzzleToolkit.getPuzzle();
        addedBridges.clear();
        fireGameStateEvent(GameStateEventType.PUZZLE_CHANGED);
    }

    /**
     * Generates a new random puzzle with the given amount of columns and rows.
     * @param columns amount of columns of the new puzzle.
     * @param rows amount of rows of the new puzzle.
     */
    public void newPuzzle(final int columns, final int rows) {
        try {
            puzzleToolkit = PuzzleToolkitFactory.createForGeneratedPuzzle(columns, rows);
            initAfterNewPuzzle();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while generating a random puzzle.", e);
            fireGameStateEvent(GameStateEventType.PUZZLE_GENERATION_FAILED);
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
            puzzleToolkit = PuzzleToolkitFactory.createForGeneratedPuzzle(columns, rows, islands);
            initAfterNewPuzzle();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while generating a random puzzle.", e);
            fireGameStateEvent(GameStateEventType.PUZZLE_GENERATION_FAILED);
        }
    }

    /**
     * Load the puzzle from the file file.
     * @param sourceFile location of the puzzle to be loaded.
     */
    public void loadPuzzle(final File sourceFile) {
        try {
            puzzleToolkit = PuzzleToolkitFactory.createForLoadedPuzzle(sourceFile);
            this.sourceFile = sourceFile;
            initAfterNewPuzzle();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while loading a puzzle.", e);
            fireGameStateEvent(GameStateEventType.LOADING_PUZZLE_FAILED);
        }
    }

    /**
     * Save the current puzzle in the file it was loaded from.
     */
    public void savePuzzle() {
        try {
            savePuzzleAs(sourceFile);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while saving a puzzle.", e);
            fireGameStateEvent(GameStateEventType.SAVING_PUZZLE_FAILED);
        }
    }

    /**
     * Save the current puzzle in the given location.
     * @param destinationFile destination for the puzzle to be saved.
     */
    public void savePuzzleAs(final File destinationFile) {
        try {
            puzzleToolkit.savePuzzle(destinationFile);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while saving a puzzle.", e);
            fireGameStateEvent(GameStateEventType.SAVING_PUZZLE_FAILED);
        }
    }

    /**
     * Restart the current puzzle.
     */
    public void restartPuzzle() {
        try {
            puzzle.removeAllBridges();
            initAfterNewPuzzle();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while restarting a puzzle.", e);
            fireGameStateEvent(GameStateEventType.RESTARTING_PUZZLE_FAILED);
        }
    }

    /**
     * Perform a next save move on the current puzzle, if any can be found.
     */
    public void nextMove() {
        try {
            Optional<Bridge> optionalBridge = puzzleToolkit.nextMove();
            if (optionalBridge.isPresent()) {
                addedBridges.add(optionalBridge.get());
                fireGameStateEvent(GameStateEventType.PUZZLE_CHANGED);
            } else {
                fireGameStateEvent(GameStateEventType.NO_NEXT_MOVE);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while applying a next safe move.", e);
            fireGameStateEvent(GameStateEventType.NEXT_MOVE_FAILED);
        }
    }

    /**
     * Solve the current puzzle as far as possible.
     */
    public void solve() {
        try {
            if (currentTask == null || currentTask.isDone()) {
                Platform.runLater(() -> fireGameStateEvent(GameStateEventType.AUTOMATIC_SOLVING_STARTED));
                currentTask = executorService.submit(this::solvePuzzle);
            } else {
                currentTask.cancel(true);
                fireGameStateEvent(GameStateEventType.AUTOMATIC_SOLVING_CANCELLED_BY_USER);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while solving a puzzle.", e);
            fireGameStateEvent(GameStateEventType.SOLVING_FAILED);
        }
    }

    private void solvePuzzle() {
        try {
            Optional<Bridge> optionalBridge;
            boolean notEvenOneMoveFound = true;
            do {
                optionalBridge = puzzleToolkit.nextMove();

                if (optionalBridge.isPresent()) {
                    notEvenOneMoveFound = false;
                    addedBridges.add(optionalBridge.get());
                    Platform.runLater(() -> fireGameStateEvent(GameStateEventType.PUZZLE_CHANGED));

                    if (getPuzzleStatus() == PuzzleStatus.UNSOLVED && !currentTask.isCancelled()) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            currentTask.cancel(true);
                        }
                    }
                } else if (notEvenOneMoveFound) {
                    // If we didn't even find one move, we still want to update the status and show an info dialog
                    Platform.runLater(() -> fireGameStateEvent(GameStateEventType.PUZZLE_CHANGED));
                }
            } while (optionalBridge.isPresent() && !currentTask.isCancelled());

            if (!currentTask.isCancelled()) {
                Platform.runLater(() -> fireGameStateEvent(GameStateEventType.AUTOMATIC_SOLVING_FINISHED));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while solving a puzzle.", e);
            Platform.runLater(() -> fireGameStateEvent(GameStateEventType.SOLVING_FAILED));
        }
    }

    /**
     * Try to build a bridge to the given {@link Island} in the given {@link Direction}.
     * @param island {@link} Island to add a bridge to.
     * @param direction {@link Direction} to build the bridge.
     */
    public void tryBuildBridge(final Island island, final Direction direction) {
        try {
            Optional<Bridge> optionalBridge = puzzleToolkit.tryBuildBridge(island, direction);
            if (optionalBridge.isPresent()) {
                addedBridges.add(optionalBridge.get());
                fireGameStateEvent(GameStateEventType.PUZZLE_CHANGED);
            } else {
                fireGameStateEvent(GameStateEventType.INVALID_MOVE);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while building a bridge.", e);
            fireGameStateEvent(GameStateEventType.BUILD_BRIDGE_FAILED);
        }
    }

    /**
     * Tear down the bridge of the given island in the given direction.
     * @param island island to remove the bridge from.
     * @param direction direction of the bridge to remove.
     */
    public void tearDownBridge(final Island island, final Direction direction) {
        try {
            Optional<Bridge> optionalBridge = puzzleToolkit.tearDownBridge(island, direction);
            if (optionalBridge.isPresent()) {
                addedBridges.removeLastOccurrence(optionalBridge.get());
                fireGameStateEvent(GameStateEventType.PUZZLE_CHANGED);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while tearing down a bridge.", e);
            fireGameStateEvent(GameStateEventType.TEAR_DOWN_BRIDGE_FAILED);
        }
    }

    /**
     * Indicates if the given bridge is the last one that was added to the current puzzle.
     * @param bridge {@link Bridge} to check.
     * @return true, if the given {@link Bridge} is the last one that was added to the puzzle, false otherwise.
     */
    public boolean isLatestBridge(final Bridge bridge) {
        return !addedBridges.isEmpty() && addedBridges.getLast().equals(bridge);
    }

    /**
     * Get the status of the current puzzle.
     * @return the status of the current puzzle.
     */
    public PuzzleStatus getPuzzleStatus() {
        try {
            return puzzleToolkit.getPuzzleStatus();
        } catch (final Exception e) {
            // TODO: Handle null in calling methods
            LOGGER.log(Level.SEVERE, "Unexpected error while tearing down a bridge.", e);
            return null;
        }
    }
}
