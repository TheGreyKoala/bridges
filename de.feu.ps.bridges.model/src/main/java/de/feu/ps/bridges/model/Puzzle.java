package de.feu.ps.bridges.model;

import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public interface Puzzle {
    int getWidth();
    int getHeight();
    Set<Island> getIslands();
    Set<Bridge> getBridges();
    PuzzleStatus getStatus();
}
