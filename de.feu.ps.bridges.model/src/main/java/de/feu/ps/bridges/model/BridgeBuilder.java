package de.feu.ps.bridges.model;

/**
 * @author Tim Gremplewski
 */
public class BridgeBuilder {

    private Island island1;
    private Island island2;
    private boolean doubleBridge;

    private BridgeBuilder() {
    }

    public static BridgeBuilder createBuilder() {
        return new BridgeBuilder();
    }

    public static Bridge buildBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        return createBuilder()
                .setIsland1(island1)
                .setIsland2(island2)
                .setDoubleBridge(doubleBridge)
                .getResult();
    }

    public BridgeBuilder setIsland1(final Island island1) {
        this.island1 = island1;
        return this;
    }

    public BridgeBuilder setIsland2(Island island2) {
        this.island2 = island2;
        return this;
    }

    public BridgeBuilder setDoubleBridge(boolean doubleBridge) {
        this.doubleBridge = doubleBridge;
        return this;
    }

    public Bridge getResult() {
        return new DefaultBridge(island1, island2, doubleBridge);
    }
}
