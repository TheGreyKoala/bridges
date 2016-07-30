package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.GameOptionsEvent;

/**
 * @author Tim Gremplewski
 */
public interface GameOptionsEventListener {
    void handleEvent(GameOptionsEvent event);
    void handleEvent(GameOptionsEvent event, Object eventParameter);
}
