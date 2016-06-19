package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
interface ModifiablePuzzle extends Puzzle {
    void addIsland(Island island);
}
