package de.feu.ps.bridges.model;

/**
 * {@link Bridge} that can switch between being a double bridge or not.
 *
 * @author Tim Gremplewski
 */
interface ModifiableBridge extends Bridge {

    /**
     * Make this bridge a double bridge or a single bridge.
     *
     * If <code>doubleBridge</code> is <code>true</code>,
     * this bridge will become a double bridge,
     * otherwise it will become a single bridge.
     *
     * @param doubleBridge <code>boolean</code> that indicates whether this bridge should become a double bridge or not
     */
    void setDoubleBridge(boolean doubleBridge);
}
