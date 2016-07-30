package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.ErrorEvent;

/**
 * Interface that defines means to handle an {@link ErrorEvent}.
 * @author Tim Gremplewski
 */
public interface ErrorEventListener {

    /**
     * Handle the given event.
     * @param event the event.
     */
    void handleEvent(ErrorEvent event);
}
