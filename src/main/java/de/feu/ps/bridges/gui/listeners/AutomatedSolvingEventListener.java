package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;

/**
 * Interface that defines means to handle an {@link AutomatedSolvingEvent}.
 * @author Tim Gremplewski
 */
public interface AutomatedSolvingEventListener {

    /**
     * Handle the given event.
     * @param event the event.
     */
    void handleEvent(AutomatedSolvingEvent event);
}
