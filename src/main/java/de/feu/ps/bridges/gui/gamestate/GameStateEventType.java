package de.feu.ps.bridges.gui.gamestate;

/**
 * Types of events that can occur.
 * @author Tim Gremplewski
 */
public enum GameStateEventType {

    /**
     * The current puzzle has changed.
     */
    PUZZLE_CHANGED,

    /**
     * The automatic solving of the current puzzle has been started.
     */
    AUTOMATIC_SOLVING_STARTED,

    /**
     * The automatic solving of the current puzzle has been finished.
     */
    AUTOMATIC_SOLVING_FINISHED,

    /**
     * The automatic solving of the current puzzle has been cancelled by the user.
     */
    AUTOMATIC_SOLVING_CANCELLED_BY_USER,

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
     * No next safe move can be applied to the puzzle.
     */
    NO_NEXT_MOVE,

    /**
     * Solving the puzzle failed.
     */
    SOLVING_FAILED,

    /**
     * Building a bridge failed.
     */
    BUILD_BRIDGE_FAILED,

    /**
     * The move to be applied is invalid,
     */
    INVALID_MOVE,

    /**
     * Tearing down a bridge failed.
     */
    TEAR_DOWN_BRIDGE_FAILED
}
