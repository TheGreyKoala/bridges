package de.feu.ps.bridges.gui.events;

/**
 * Events that can occur when the puzzle changes.
 * @author Tim Gremplewski
 */
public enum PuzzleEvent {

    /**
     * The puzzle itself changed, e.g. a bridge was added.
     */
    PUZZLE_CHANGED,

    /**
     * The status of the puzzle changed.
     */
    PUZZLE_STATUS_CHANGED
}
