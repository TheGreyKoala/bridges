package de.feu.ps.bridges.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Default implementation of {@link Bridge}.
 *
 * @author Tim Gremplewski
 */
public class DefaultBridge implements Bridge {

    // TODO Test

    private final Island island1;
    private final Island island2;
    private final Set<Island> islands;
    private boolean doubleBridge;

    DefaultBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        this.island1 = Objects.requireNonNull(island1, "Parameter 'island1' must not be null.");
        this.island2 = Objects.requireNonNull(island2, "Parameter 'island2' must not be null.");

        if (island1 == island2) {
            throw new IllegalArgumentException("Can not build a bridge from an island to itself.");
        }

        if (!canBeConnected(island1, island2)) {
            throw new IllegalArgumentException("Islands must either be on the same row or the same column");
        }

        islands = new HashSet<>(2);
        islands.add(island1);
        islands.add(island2);
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

    @Override
    public Set<Island> getConnectedIslands() {
        return new HashSet<>(islands);
    }

    private boolean canBeConnected(final Island island1, final Island island2) {
        return island1.getRow() == island2.getRow() || island1.getColumn() == island2.getColumn();
    }

    @Override
    public void setDoubleBridge(boolean doubleBridge) {
        this.doubleBridge = doubleBridge;
    }

    @Override
    public String toString() {
        return "DefaultBridge{" +
                "[" + island1.getPosition().getColumn() + ", " + island1.getPosition().getRow() + "] - " +
                "[" + island2.getPosition().getColumn() + ", " + island2.getPosition().getRow() + "]" +
                ", doubleBridge=" + doubleBridge +
                '}';
    }
}
