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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Tim Gremplewski
 */
public class GameState {

    // TODO Recalculate status only if necessary and not every time it is requested

    private static GameState instance;
    private final ExecutorService executorService;

    private Puzzle puzzle;
    private File sourceFile;
    private final Set<GameStateListener> gameStateListeners;
    private Future<?> currentTask;
    private LinkedList<Bridge> addedBridges;
    private PuzzleToolkit puzzleToolkit;

    private GameState() {
        this.gameStateListeners = new HashSet<>();
        executorService = Executors.newWorkStealingPool();
        addedBridges = new LinkedList<>();
    }

    public static synchronized GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void newPuzzle() {
        puzzleToolkit = PuzzleToolkitFactory.createForGeneratedPuzzle();
        puzzle = puzzleToolkit.getPuzzle();
        addedBridges.clear();
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void newPuzzle(final int columns, final int rows) {
        puzzleToolkit = PuzzleToolkitFactory.createForGeneratedPuzzle(columns, rows);
        puzzle = puzzleToolkit.getPuzzle();
        addedBridges.clear();
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void newPuzzle(final int columns, final int rows, final int islands) {
        puzzleToolkit = PuzzleToolkitFactory.createForGeneratedPuzzle(columns, rows, islands);
        puzzle = puzzleToolkit.getPuzzle();
        addedBridges.clear();
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void loadPuzzle(final File sourceFile) {
        puzzleToolkit = PuzzleToolkitFactory.createForLoadedPuzzle(sourceFile);
        puzzle = puzzleToolkit.getPuzzle();
        addedBridges.clear();
        this.sourceFile = sourceFile;
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void savePuzzle() {
        savePuzzleAs(sourceFile);
    }

    public void savePuzzleAs(final File destinationFile) {
        puzzleToolkit.savePuzzle(destinationFile);
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void addGameStateListener(final GameStateListener gameStateListener) {
        gameStateListeners.add(gameStateListener);
    }

    public void removeGameStateListener(final GameStateListener gameStateListener) {
        gameStateListeners.remove(gameStateListener);
    }

    private void fireGameStateEvent(GameStateEventType gameStateEventType) {
        GameStateEvent gameStateEvent = new GameStateEvent(gameStateEventType);
        gameStateListeners.forEach(gameStateListener -> gameStateListener.handleGameStateEvent(gameStateEvent));
    }

    public void restartPuzzle() {
        puzzle.removeAllBridges();
        addedBridges.clear();
        fireGameStateEvent(GameStateEventType.PUZZLE_RESTARTED);
    }

    public boolean nextMove() {
        Optional<Bridge> optionalBridge = puzzleToolkit.nextMove();
        if (optionalBridge.isPresent()) {
            addedBridges.add(optionalBridge.get());
            fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
        }
        return optionalBridge.isPresent();
    }

    public void solve() {
        if (currentTask == null || currentTask.isDone()) {
            Platform.runLater(() -> fireGameStateEvent(GameStateEventType.AUTOMATIC_SOLVING_STARTED));
            currentTask = executorService.submit(() -> {
                Optional<Bridge> optionalBridge;
                do {
                    optionalBridge = puzzleToolkit.nextMove();

                    if (optionalBridge.isPresent()) {
                        addedBridges.add(optionalBridge.get());
                        Platform.runLater(() -> fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED));

                        if (getPuzzleStatus() == PuzzleStatus.UNSOLVED && !currentTask.isCancelled()) {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                currentTask.cancel(true);
                            }
                        }
                    }
                } while (optionalBridge.isPresent() && !currentTask.isCancelled());

                if (currentTask.isCancelled()) {
                    Platform.runLater(() -> fireGameStateEvent(GameStateEventType.AUTOMATIC_SOLVING_CANCELLED_BY_USER));
                } else {
                    Platform.runLater(() -> fireGameStateEvent(GameStateEventType.AUTOMATIC_SOLVING_STOPPED));
                }

            });
        } else {
            currentTask.cancel(true);
        }
    }

    public boolean tryAddBridge(final Island island, final Direction direction) {
        Optional<Bridge> optionalBridge = puzzleToolkit.tryAddBridge(island, direction);
        if (optionalBridge.isPresent()) {
            addedBridges.add(optionalBridge.get());
            fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
        }
        return optionalBridge.isPresent();
    }

    public void removeBridge(final Island island, final Direction direction) {
        Optional<Bridge> optionalBridge = puzzleToolkit.removeBridge(island, direction);
        if (optionalBridge.isPresent()) {
            addedBridges.removeLastOccurrence(optionalBridge.get());
            fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
        }
    }

    public boolean isLatestBridge(final Bridge bridge) {
        if (addedBridges.isEmpty()) {
            return false;
        }
        return addedBridges.getLast().equals(bridge);
    }

    public PuzzleStatus getPuzzleStatus() {
        return puzzleToolkit.getPuzzleStatus();
    }
}
