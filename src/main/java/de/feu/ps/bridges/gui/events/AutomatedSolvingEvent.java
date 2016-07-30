package de.feu.ps.bridges.gui.events;

/**
 * @author Tim Gremplewski
 */
public enum AutomatedSolvingEvent {

    /**
     * The automatic solving of the current puzzle has been started.
     */
    STARTED,

    /**
     * The automatic solving of the current puzzle has been finished.
     */
    FINISHED,

    /**
     * The automatic solving of the current puzzle has been cancelled by the user.
     */
    CANCELLED_BY_USER,

    /**
     * No next safe move can be applied to the puzzle.
     */
    NO_NEXT_MOVE
}
