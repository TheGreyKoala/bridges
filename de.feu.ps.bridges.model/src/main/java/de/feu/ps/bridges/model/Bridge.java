package de.feu.ps.bridges.model;

import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public interface Bridge {
    Set<Island> getConnectedIslands();
}
