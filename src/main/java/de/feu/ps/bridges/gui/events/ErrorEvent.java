package de.feu.ps.bridges.gui.events;

/**
 * @author Tim Gremplewski
 */
public enum ErrorEvent {
    /**
     * The generation of a new puzzle failed.
     */
    PUZZLE_GENERATION_FAILED,

    /**
     * Loading a puzzle from a file failed.
     */
    LOADING_PUZZLE_FAILED,

    /**
     * Saving a puzzle to a file failed.
     */
    SAVING_PUZZLE_FAILED,

    /**
     * Restarting the puzzle failed.
     */
    RESTARTING_PUZZLE_FAILED,

    /**
     * Applying a next safe move to the puzzle failed.
     */
    NEXT_MOVE_FAILED,

    /**
     * Solving the puzzle failed.
     */
    SOLVING_FAILED,

    /**
     * Building a bridge failed.
     */
    BUILD_BRIDGE_FAILED,

    /**
     * Tearing down a bridge failed.
     */
    TEAR_DOWN_BRIDGE_FAILED
}
