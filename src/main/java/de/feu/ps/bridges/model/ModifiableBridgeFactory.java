package de.feu.ps.bridges.model;

/**
 * Factory class to create an instance of {@link ModifiableBridge}.
 * @author Tim Gremplewski
 */
final class ModifiableBridgeFactory {

    private ModifiableBridgeFactory() {
    }

    /**
     * Create a new {@link ModifiableBridge} object.
     * @param island1 {@link Island} to be bridged.
     * @param island2 Another {@link Island} to be bridged.
     * @param doubleBridge <code>boolean</code> that indicates whether the new bridge should be a double bridge or not.
     * @return A new {@link ModifiableBridge} instance
     * @throws NullPointerException if <code>island1</code> or <code>island2</code> are <code>null</code>
     * @throws IllegalArgumentException if <code>island1</code> and <code>island2</code> are equal
     *  or if the islands do not lie in the same row or the same column.
     */
    static ModifiableBridge createBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        return new DefaultBridge(island1, island2, doubleBridge);
    }
}
