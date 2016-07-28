package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.GameStateEvent;

/**
 * @author Tim Gremplewski
 */
public interface GameStateEventListener {
    void handleEvent(GameStateEvent event);
    void handleEvent(GameStateEvent event, Object eventParameter);
}
