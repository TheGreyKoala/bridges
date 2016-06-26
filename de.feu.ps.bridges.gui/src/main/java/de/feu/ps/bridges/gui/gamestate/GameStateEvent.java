package de.feu.ps.bridges.gui.gamestate;

/**
 * @author Tim Gremplewski
 */
public class GameStateEvent {
    private final GameStateEventType gameStateEventType;

    public GameStateEvent(GameStateEventType gameStateEventType) {
        this.gameStateEventType = gameStateEventType;
    }

    public GameStateEventType getGameStateEventType() {
        return gameStateEventType;
    }
}
