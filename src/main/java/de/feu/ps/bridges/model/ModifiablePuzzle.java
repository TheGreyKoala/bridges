package de.feu.ps.bridges.model;

/**
 * {@link Puzzle} that can be extended with further islands.
 *
 * @author Tim Gremplewski
 */
interface ModifiablePuzzle extends Puzzle {

    /**
     * Build a new island at the given position.
     *
     * @param position Position of the new island.
     * @param requiredBridges Amount of required bridges of the new Island.
     * @return the newly created island.
     */
    Island buildIsland(Position position, int requiredBridges);
}
