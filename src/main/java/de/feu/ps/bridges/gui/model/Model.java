package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;

import java.io.File;

import static de.feu.ps.bridges.gui.events.GameOptionsEvent.SHOW_REMAINING_BRIDGES_OPTION_CHANGED;

/**
 * @author Tim Gremplewski
 */
// TODO: Model is not a good name. This class allows to perform operations on a GameState and therewith change it
public class Model {
    private final GamePlay gamePlay;
    private final PuzzleIO puzzleIo;
    private final AutomatedSolving automatedSolving;
    private final PuzzleGeneration puzzleGeneration;
    private final GameState gameState;

    public Model(final GameState gameState) {
        gamePlay = new GamePlay(gameState);
        puzzleIo = new PuzzleIO(gameState);
        automatedSolving = new AutomatedSolving(gameState);
        puzzleGeneration = new PuzzleGeneration(gameState);
        this.gameState = gameState;
    }

    public void restartPuzzle() {
        gamePlay.restartPuzzle();
    }

    public void loadPuzzle(final File sourceFile) {
        puzzleIo.loadPuzzle(sourceFile);
    }

    public void savePuzzle() {
        puzzleIo.savePuzzle();
    }

    public void savePuzzleAs(final File destinationFile) {
        puzzleIo.savePuzzleAs(destinationFile);
    }

    public void setShowRemainingBridges(final boolean isShowRemainingBridges) {
        gameState.broadcastEvent(SHOW_REMAINING_BRIDGES_OPTION_CHANGED, isShowRemainingBridges);
    }

    public void solve() {
        automatedSolving.solve();
    }

    public void nextMove() {
        automatedSolving.nextMove();
    }

    public void newPuzzle() {
        puzzleGeneration.newPuzzle();
    }

    public void newPuzzle(final int columns, final int rows) {
        puzzleGeneration.newPuzzle(columns, rows);
    }

    public void newPuzzle(final int columns, final int rows, final int islands) {
        puzzleGeneration.newPuzzle(columns, rows);
    }

    public void tryBuildBridge(final Island island, final Direction direction) {
        gamePlay.tryBuildBridge(island, direction);
    }

    public void tearDownBridge(final Island island, final Direction direction) {
        gamePlay.tearDownBridge(island, direction);
    }

    public GameState getGameState() {
        return gameState;
    }
}
