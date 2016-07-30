package de.feu.ps.bridges.gui.events;

/**
 * Error events that can occur.
 * @author Tim Gremplewski
 */
public enum ErrorEvent {

    /**
     * Applying a next safe move to the puzzle failed.
     */
    APPLYING_NEXT_MOVE_FAILED,

    /**
     * Building a bridge failed.
     */
    BUILDING_BRIDGE_FAILED,

    /**
     * Generating a new puzzle failed.
     */
    GENERATING_PUZZLE_FAILED,

    /**
     * Loading a puzzle from a file failed.
     */
    LOADING_PUZZLE_FAILED,

    /**
     * Restarting the puzzle failed.
     */
    RESTARTING_PUZZLE_FAILED,

    /**
     * Saving the puzzle to a file failed.
     */
    SAVING_PUZZLE_FAILED,

    /**
     * Solving the puzzle failed.
     */
    SOLVING_FAILED,

    /**
     * Tearing down a bridge failed.
     */
    TEARING_DOWN_BRIDGE_FAILED
}
