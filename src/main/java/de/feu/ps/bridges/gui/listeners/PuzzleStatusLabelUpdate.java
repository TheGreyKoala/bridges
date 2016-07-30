package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.model.GameState;

import java.util.ResourceBundle;
import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_STATUS_CHANGED;

/**
 * Listener that tells a given client which text to display to visualize the changed puzzle status.
 * @author Tim Gremplewski
 */
public class PuzzleStatusLabelUpdate implements PuzzleEventListener {

    private final ResourceBundle resourceBundle;
    private final Consumer<String> consumer;
    private final GameState gameState;

    /**
     * Creates a new instance, that uses the given {@link GameState} to query the puzzle status
     * and informs the given {@link Consumer} which text to display.
     * @param gameState {@link GameState} that will be used to query the puzzle status.
     * @param consumer {@link Consumer} that will be told which text to display.
     * @param resourceBundle {@link ResourceBundle} to localize the text.
     */
    public PuzzleStatusLabelUpdate(final GameState gameState, final Consumer<String> consumer, final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        this.consumer = consumer;
        this.gameState = gameState;
    }

    @Override
    public void handleEvent(final PuzzleEvent event) {
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
