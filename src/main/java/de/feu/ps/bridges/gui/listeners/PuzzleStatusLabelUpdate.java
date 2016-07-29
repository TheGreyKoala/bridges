package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.gamestate.GameState;

import java.util.ResourceBundle;
import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_STATUS_CHANGED;

/**
 * @author Tim Gremplewski
 */
public class PuzzleStatusLabelUpdate implements PuzzleEventListener {

    private final ResourceBundle resourceBundle;
    private final Consumer<String> consumer;
    private final GameState gameState;

    public PuzzleStatusLabelUpdate(final ResourceBundle resourceBundle, final Consumer<String> consumer, final GameState gameState) {
        this.resourceBundle = resourceBundle;
        this.consumer = consumer;
        this.gameState = gameState;
        // TODO: Create a new Listener that only listens on PuzzleStatus changes.
        // That way we get rid of the dependency to GameState
    }

    @Override
    public void handleEvent(final PuzzleEvent event) {
        // TODO: Fire event in GameState
        if (event == PUZZLE_STATUS_CHANGED) {
            updateStatusBar(gameState.getPuzzleStatus());
        }
    }

    private void updateStatusBar(final PuzzleStatus puzzleStatus) {
        switch (puzzleStatus) {
            case SOLVED:
                consumer.accept(resourceBundle.getString("puzzle.status.solved"));
                break;
            case UNSOLVED:
                consumer.accept(resourceBundle.getString("puzzle.status.unsolved"));
                break;
            case UNSOLVABLE:
                consumer.accept(resourceBundle.getString("puzzle.status.unsolvable"));
                break;
        }
    }
}
