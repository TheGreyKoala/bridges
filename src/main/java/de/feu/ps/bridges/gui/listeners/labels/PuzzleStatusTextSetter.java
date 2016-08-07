package de.feu.ps.bridges.gui.listeners.labels;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.listeners.PuzzleEventListener;

import java.util.ResourceBundle;
import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_STATUS_CHANGED;

/**
 * Listener that tells a given client which text to display to visualize the puzzle status.
 * @author Tim Gremplewski
 */
public class PuzzleStatusTextSetter implements PuzzleEventListener {

    private final ResourceBundle resourceBundle;
    private final Consumer<String> consumer;

    /**
     * Creates a new instance, that informs the given {@link Consumer} which text to display.
     * @param consumer {@link Consumer} that will be told which text to display.
     * @param resourceBundle {@link ResourceBundle} to localize the text.
     */
    public PuzzleStatusTextSetter(final Consumer<String> consumer, final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        this.consumer = consumer;
    }

    @Override
    public void handleEvent(final PuzzleEvent event, final Object eventParameter) {
        if (event == PUZZLE_STATUS_CHANGED) {
            updateStatusBar((PuzzleStatus) eventParameter);
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
