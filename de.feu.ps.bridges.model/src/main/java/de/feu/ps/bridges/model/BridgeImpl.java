package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
public class BridgeImpl implements Bridge {

    private final Island island1;
    private final Island island2;
    private final boolean doubleBridge;

    protected BridgeImpl(final Island island1, final Island island2, final boolean doubleBridge) {
        // TODO Validate parameters

        if (island1 == null || island2 == null) {
            throw new IllegalArgumentException("None of the connected islands may be null.");
        }

        this.island1 = island1;
        this.island2 = island2;
        this.doubleBridge = doubleBridge;
    }

    @Override
    public Island getIsland1() {
        return island1;
    }

    @Override
    public Island getIsland2() {
        return island2;
    }

    @Override
    public boolean isDoubleBridge() {
        return doubleBridge;
    }
}
