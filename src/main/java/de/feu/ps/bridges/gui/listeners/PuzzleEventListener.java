package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.PuzzleEvent;

/**
 * Interface that defines means to handle a {@link PuzzleEvent}.
 * @author Tim Gremplewski
 */
public interface PuzzleEventListener {

    /**
     * Handle the given event.
     * @param event the event.
     * @param eventParameter additional event parameter.
     */
    void handleEvent(PuzzleEvent event, Object eventParameter);
}
