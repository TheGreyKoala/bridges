package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;

import java.io.File;

/**
 * This class allows to perform operations on the given {@link GameState}.
 * @author Tim Gremplewski
 */
public class Model {
    private final GamePlay gamePlay;
    private final PuzzleIO puzzleIo;
    private final AutomatedSolving automatedSolving;
    private final PuzzleGeneration puzzleGeneration;
    private final GameState gameState;

    /**
     * Create a new instance that operates on the given {@link GameState}.
     * @param gameState {@link GameState} to operate on.
     */
    public Model(final GameState gameState) {
        gamePlay = new GamePlay(gameState);
        puzzleIo = new PuzzleIO(gameState);
        automatedSolving = new AutomatedSolving(gameState);
        puzzleGeneration = new PuzzleGeneration(gameState);
        this.gameState = gameState;
    }

    /**
     * Get the {@link GameState}.
     * @return the game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Generate a random puzzle.
     */
    public void newPuzzle() {
        puzzleGeneration.newPuzzle();
    }

    /**
     * Generate a random puzzle with the given amount of columns and rows.
     * @param columns amount of columns of the generated puzzle.
     * @param rows amount of rows of the generated puzzle.
     */
    public void newPuzzle(final int columns, final int rows) {
        puzzleGeneration.newPuzzle(columns, rows);
    }

    /**
     * Generate a random puzzle with the given amount of columns, rows and islands.
     * @param columns amount of columns of the generated puzzle.
     * @param rows amount of rows of the generated puzzle.
     * @param islands amount of islands in the generated puzzle.
     */
    public void newPuzzle(final int columns, final int rows, final int islands) {
        puzzleGeneration.newPuzzle(columns, rows, islands);
    }

    /**
     * Restart the current puzzle.
     */
    public void restartPuzzle() {
        gamePlay.restartPuzzle();
    }

    /**
     * Try to add a bridge to the given island in the given direction.
     * @param island Island to add the bridge to.
     * @param direction Direction of the new bridge.
     */
    public void tryBuildBridge(final Island island, final Direction direction) {
        gamePlay.tryBuildBridge(island, direction);
    }

    /**
     * Tear down the bridge in the given direction of the given island.
     * @param island Island to remove bridge from.
     * @param direction Direction of the bridge to be removed.
     */
    public void tearDownBridge(final Island island, final Direction direction) {
        gamePlay.tearDownBridge(island, direction);
    }

    /**
     * Load the puzzle from the given file.
     * @param sourceFile file to load the puzzle from.
     */
    public void loadPuzzle(final File sourceFile) {
        puzzleIo.loadPuzzle(sourceFile);
    }

    /**
     * Save the current puzzle in its original source file.
     */
    public void savePuzzle() {
        puzzleIo.savePuzzle();
    }

    /**
     * Save the current puzzle in the given file.
     * @param destinationFile file where the puzzle should be saved.
     */
    public void savePuzzleAs(final File destinationFile) {
        puzzleIo.savePuzzleAs(destinationFile);
    }

    /**
     * Solve the current puzzle as far as possible.
     */
    public void solve() {
        automatedSolving.solve();
    }

    /**
     * Apply a next save move on the current puzzle.
     */
    public void applyNextMove() {
        automatedSolving.applyNextMove();
    }
}
