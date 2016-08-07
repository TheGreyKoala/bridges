package de.feu.ps.bridges.gui.events;

/**
 * Events that occur regarding the current puzzle.
 * @author Tim Gremplewski
 */
public enum PuzzleEvent {

    /**
     * A bridge was added to the puzzle.
     */
    BRIDGE_ADDED,

    /**
     * A bridge was removed from the puzzle.
     */
    BRIDGE_REMOVED,

    /**
     * The user tried to apply an invalid move.
     */
    INVALID_MOVE,

    /**
     * The puzzle has been reset, i.e. all bridges have been removed.
     */
    PUZZLE_RESET,

    /**
     * The puzzle itself changed, e.g. a new puzzle was generated.
     */
    PUZZLE_CHANGED,

    /**
     * The status of the puzzle changed.
     */
    PUZZLE_STATUS_CHANGED
}
