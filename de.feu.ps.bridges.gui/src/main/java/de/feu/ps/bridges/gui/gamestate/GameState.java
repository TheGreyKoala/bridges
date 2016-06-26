package de.feu.ps.bridges.gui.gamestate;

import de.feu.ps.bridges.facade.Facade;
import de.feu.ps.bridges.model.Puzzle;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class GameState {

    private static GameState instance;

    private Puzzle puzzle;
    private File sourceFile;
    private final Set<GameStateListener> gameStateListeners;

    private GameState() {
        this.gameStateListeners = new HashSet<>();
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
}
