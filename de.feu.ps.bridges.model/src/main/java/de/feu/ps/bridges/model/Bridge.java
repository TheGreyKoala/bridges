package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
public interface Bridge {
    Island getIsland1();
    Island getIsland2();
    boolean isDoubleBridge();
}
