package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.PuzzleEvent;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_CHANGED;

/**
 * Listener that tells a given client, when a puzzle has changed.
 * @author Tim Gremplewski
 */
public class PuzzleUpdate implements PuzzleEventListener {

    private final Client client;

    /**
     * Create a new instance that informs the given {@link Client},
     * when a puzzle has changed.
     * @param client {@link Client} that will be informed when a puzzle has changed.
     */
    public PuzzleUpdate(final Client client) {
        this.client = client;
    }

    @Override
    public void handleEvent(final PuzzleEvent event) {
        if (event == PUZZLE_CHANGED) {
            client.handlePuzzleUpdate();
        }
    }

    /**
     * Functional interface for objects that want to act as clients for {@link PuzzleUpdate}.
     */
    @FunctionalInterface
    public interface Client {
        void handlePuzzleUpdate();
    }
}
