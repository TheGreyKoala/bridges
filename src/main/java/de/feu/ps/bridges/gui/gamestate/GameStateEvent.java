package de.feu.ps.bridges.gui.gamestate;

import java.util.Objects;

/**
 * An event in the game.
 * @author Tim Gremplewski
 */
public class GameStateEvent {
    private final GameStateEventType gameStateEventType;

    /**
     * Create a new event with the given type.
     * @param gameStateEventType type of the new event.
     * @throws NullPointerException if gameStateEventType is null.
     */
    GameStateEvent(final GameStateEventType gameStateEventType) {
        this.gameStateEventType = Objects.requireNonNull(gameStateEventType, "Parameter 'gameStateEventType' must not be null.");
    }

    /**
     * Get the type of this event.
     * @return the type of this event.
     */
    public GameStateEventType getGameStateEventType() {
        return gameStateEventType;
    }
}
