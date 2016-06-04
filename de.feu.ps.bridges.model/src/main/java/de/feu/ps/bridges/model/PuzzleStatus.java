package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
public enum PuzzleStatus {
    SOLVED,

    /**
     * e.g. de.feu.ps.bridges.model.Island has too many Bridges
     */
    ILLEGAL_STATE,

    NOT_SOLVABLE
}
