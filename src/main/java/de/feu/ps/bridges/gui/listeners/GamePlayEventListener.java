package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.GamePlayEvent;

/**
 * Interface that defines means to handle a {@link GamePlayEvent}.
 * @author Tim Gremplewski
 */
public interface GamePlayEventListener {

    /**
     * Handle the given event.
     * @param event the event.
     */
    void handleEvent(GamePlayEvent event);
}
