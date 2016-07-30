package de.feu.ps.bridges.gui.events;

/**
 * Events that can occur during the automated solving of a puzzle.
 * These includes events during the complete solving of a puzzle as well
 * as events during the next move operation.
 *
 * @author Tim Gremplewski
 */
public enum AutomatedSolvingEvent {

    /**
     * Solving the current puzzle has been started.
     */
    STARTED,

    /**
     * Solving the current puzzle has been cancelled by the user.
     */
    CANCELLED_BY_USER,

    /**
     * Solving the current puzzle has been finished.
     */
    FINISHED,

    /**
     * No next safe move can be found in the current puzzle.
     */
    NO_NEXT_MOVE
}
