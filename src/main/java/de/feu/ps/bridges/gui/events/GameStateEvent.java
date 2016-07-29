package de.feu.ps.bridges.gui.events;

/**
 * Types of events that can occur.
 * @author Tim Gremplewski
 */
public enum GameStateEvent {

    /**
     * No next safe move can be applied to the puzzle.
     */
    NO_NEXT_MOVE,

    /**
     * The move to be applied is invalid,
     */
    INVALID_MOVE,

    /**
     * The option to display the remaining bridges changed.
     */
    SHOW_REMAINING_BRIDGES_OPTION_CHANGED
}
