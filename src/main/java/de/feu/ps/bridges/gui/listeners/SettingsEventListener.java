package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.SettingsEvent;

/**
 * Interface that defines means to handle a {@link SettingsEvent}.
 * @author Tim Gremplewski
 */
public interface SettingsEventListener {

    /**
     * Handle the given event.
     * @param event the event.
     * @param eventParameter additional event parameter.
     */
    void handleEvent(SettingsEvent event, Object eventParameter);
}
