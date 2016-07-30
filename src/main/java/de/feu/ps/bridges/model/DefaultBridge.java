package de.feu.ps.bridges.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Default implementation of {@link Bridge}.
 *
 * @author Tim Gremplewski
 */
class DefaultBridge implements ModifiableBridge {

    private final Island island1;
    private final Island island2;
    private final Set<Island> islands;
    private boolean doubleBridge;

    /**
     * Creates a new bridge between <code>island1</code> and <code>island2</code>.
     *
     * The bridged islands must not be equal,
     * but either lie in the same row or the same column.
     * Otherwise an {@link IllegalArgumentException} will be thrown.
     *
     * @param island1 {@link Island} to be bridged.
     * @param island2 Another {@link Island} island to be bridged.
     * @param doubleBridge <code>boolean</code> that indicates whether the new bridge should be a double bridge or not.
     * @throws NullPointerException if <code>island1</code> or <code>island2</code> are <code>null</code>
     * @throws IllegalArgumentException if <code>island1</code> and <code>island2</code> are equal
     *  or if the islands do not lie in the same row or the same column.
     */
    DefaultBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        this.island1 = Objects.requireNonNull(island1, "Parameter 'island1' must not be null.");
        this.island2 = Objects.requireNonNull(island2, "Parameter 'island2' must not be null.");
        this.doubleBridge = doubleBridge;

        if (island1.equals(island2)) {
            throw new IllegalArgumentException("Bridged islands must not be equal.");
        }

        if (!(lieInTheSameRow(island1, island2) || lieInTheSameColumn(island1, island2))) {
            throw new IllegalArgumentException("Bridged islands must either lie in the same row or the same column");
        }

        islands = new HashSet<>(2);
        islands.add(island1);
        islands.add(island2);
    }

    private boolean lieInTheSameRow(final Island island1, final Island island2) {
        return island1.getPosition().getRow() == island2.getPosition().getRow();
    }

    private boolean lieInTheSameColumn(final Island island1, final Island island2) {
        return island1.getPosition().getColumn() == island2.getPosition().getColumn();
    }

    @Override
    public Set<Island> getBridgedIslands() {
        return new HashSet<>(islands);
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
    public boolean isHorizontal() {
        return lieInTheSameRow(island1, island2);
    }

    @Override
    public boolean isVertical() {
        return lieInTheSameColumn(island1, island2);
    }

    @Override
    public void setDoubleBridge(boolean doubleBridge) {
        this.doubleBridge = doubleBridge;
    }
}
