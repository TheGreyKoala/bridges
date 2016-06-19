package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
interface ModifiablePuzzle extends Puzzle {
    Island buildIsland(int column, int row, int requiredBridges);
}
