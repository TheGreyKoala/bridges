package de.feu.ps.bridges.gui.gamestate;

import de.feu.ps.bridges.facade.Facade;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;
import javafx.application.Platform;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Tim Gremplewski
 */
public class GameState {

    private static GameState instance;
    private final ExecutorService executorService;

    private Puzzle puzzle;
    private File sourceFile;
    private final Set<GameStateListener> gameStateListeners;
    private Future<?> currentTask;

    private GameState() {
        this.gameStateListeners = new HashSet<>();
        executorService = Executors.newWorkStealingPool();
    }

    public static synchronized GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void newPuzzle() {
        puzzle = Facade.newPuzzle();
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void newPuzzle(final int columns, final int rows) {
        puzzle = Facade.newPuzzle(columns, rows);
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void newPuzzle(final int columns, final int rows, final int islands) {
        puzzle = Facade.newPuzzle(columns, rows, islands);
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void loadPuzzle(final File sourceFile) {
        puzzle = Facade.loadPuzzle(sourceFile);
        this.sourceFile = sourceFile;
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void savePuzzle() {
        savePuzzleAs(sourceFile);
    }

    public void savePuzzleAs(final File destinationFile) {
        Facade.savePuzzle(puzzle, destinationFile);
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
        fireGameStateEvent(GameStateEventType.PUZZLE_RESTARTED);
    }

    public void nextMove() {
        Facade.nextMove(puzzle);
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void solve() {
        if (currentTask == null || currentTask.isDone()) {
            Platform.runLater(() -> fireGameStateEvent(GameStateEventType.AUTOMATIC_SOLVING_STARTED));
            currentTask = executorService.submit(() -> {
                boolean stop = false;
                do {
                    stop = !Facade.nextMove(puzzle);
                    Platform.runLater(() -> fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED));
                    try {
                        if (!stop) {
                            Thread.sleep(5000);
                        }
                    } catch (InterruptedException e) {
                        stop = true;
                    }
                } while (!stop && !currentTask.isCancelled());
                Platform.runLater(() -> fireGameStateEvent(GameStateEventType.AUTOMATIC_SOLVING_STOPPED));
            });
        } else {
            currentTask.cancel(true);
        }
    }

    public void addBridge(final Island island, final Direction direction) {
        Facade.addBridge(puzzle, island, direction);
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }

    public void removeBridge(final Island island, final Direction direction) {
        Facade.removeBridge(puzzle, island, direction);
        fireGameStateEvent(GameStateEventType.NEW_PUZZLE_LOADED);
    }
}
