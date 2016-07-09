package de.feu.ps.bridges.gui.gamestate;

/**
 * Interface for objects that want to react on events in the game state.
 * @author Tim Gremplewski
 */
public interface GameStateListener {

    /**
     * React on the given game state event.
     * @param event the event.
     */
    void handleGameStateEvent(GameStateEvent event);
}
