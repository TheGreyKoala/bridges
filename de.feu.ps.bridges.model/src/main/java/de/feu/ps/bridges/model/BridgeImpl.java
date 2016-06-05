package de.feu.ps.bridges.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class BridgeImpl implements Bridge {

    private final Set<Island> connectedIslands;

    protected BridgeImpl(final Island island1, final Island island2) {
        if (island1 == null || island2 == null) {
            throw new IllegalArgumentException("None of the connected islands may be null.");
        }

        connectedIslands = new HashSet<>(2);
        connectedIslands.add(island1);
        connectedIslands.add(island2);
    }

    @Override
    public Set<Island> getConnectedIslands() {
        return new HashSet<>(connectedIslands);
    }
}
