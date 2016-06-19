package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
public interface ModifiablePuzzle extends Puzzle {
    void addIsland(Island island);
}
