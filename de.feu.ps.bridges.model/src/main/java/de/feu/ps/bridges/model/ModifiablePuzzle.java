package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
interface ModifiablePuzzle extends Puzzle {
    Island buildIsland(Position position, int requiredBridges);
}
