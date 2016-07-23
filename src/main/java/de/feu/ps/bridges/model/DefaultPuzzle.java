package de.feu.ps.bridges.model;

import java.util.*;

/**
 * @author Tim Gremplewski
 */
class DefaultPuzzle implements ModifiablePuzzle {

    private final int columnsCount;
    private final int rowsCount;

    private final Column[] columns;
    private final Row[] rows;

    private final Set<ModifiableBridge> bridges;
    private final Set<ModifiableIsland> islands;

    DefaultPuzzle(final int columns, final int rows) {
        if (columns < 1) {
            throw new IllegalArgumentException("Parameter 'columns' must not be less than 1.");
        }

        if (rows < 1) {
            throw new IllegalArgumentException("Parameter 'rows' must not be less than 1.");
        }

        columnsCount = columns;
        rowsCount = rows;
        this.columns = new Column[columns];
        this.rows = new Row[rows];
        bridges = new HashSet<>();
        islands = new HashSet<>();
    }

    @Override
    public Bridge buildBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        validateIslands(island1, island2);

        final ModifiableBridge bridge;
        final Optional<ModifiableBridge> possibleDuplicate = findBridge(island1, island2);

        if (possibleDuplicate.isPresent()) {
            bridge = possibleDuplicate.get();
            bridge.setDoubleBridge(true);
        } else {
            bridge = ModifiableBridgeFactory.createBridge(island1, island2, doubleBridge);
            ((ModifiableIsland) island1).addBridge(bridge);
            ((ModifiableIsland) island2).addBridge(bridge);
            bridges.add(bridge);
        }
        return bridge;
    }

    private void validateIslands(final Island island1, final Island island2) {
        Objects.requireNonNull(island1, "Parameter 'island1' must not be null.");
        Objects.requireNonNull(island2, "Parameter 'island2' must not be null.");

        if (!(island1 instanceof ModifiableIsland) || !islands.contains(island1)) {
            throw new IllegalArgumentException("Island1 is not part of this puzzle.");
        }

        if (!(island2 instanceof ModifiableIsland) || !islands.contains(island2)) {
            throw new IllegalArgumentException("Island2 is not part of this puzzle.");
        }
    }

    private Optional<ModifiableBridge> findBridge(final Island island1, final Island island2) {
        return bridges.stream()
                .filter(bridge -> bridge.getBridgedIslands().containsAll(
                        Arrays.asList(island1, island2))).findFirst();
    }

    @Override
    public Island buildIsland(final Position position, final int requiredBridges) {
        Objects.requireNonNull(position, "Parameter 'position' must not be null.");

        final int column = position.getColumn();
        final int row = position.getRow();

        if (columns[column] != null && columns[column].getIslandAtRow(row).isPresent()) {
            throw new IllegalStateException("An islands already exists at the given position.");
        }

        final ModifiableIsland island = ModifiableIslandFactory.create(position, requiredBridges);
        addToColumn(island, column);
        addToRow(island, row);
        islands.add(island);
        return island;
    }

    private void addToColumn(final ModifiableIsland island, final int columnIndex) {
        if (columns[columnIndex] == null) {
            columns[columnIndex] = new Column(columnIndex);
        }
        columns[columnIndex].addIsland(island);
    }

    private void addToRow(final ModifiableIsland island, final int rowIndex) {
        if (rows[rowIndex] == null) {
            rows[rowIndex] = new Row(rowIndex);
        }
        rows[rowIndex].addIsland(island);
    }

    @Override
    public Set<Bridge> getBridges() {
        return new HashSet<>(bridges);
    }

    @Override
    public int getColumnsCount() {
        return columnsCount;
    }

    @Override
    public Set<Island> getIslands() {
        return new HashSet<>(islands);
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }

    @Override
    public void removeAllBridges() {
        islands.forEach(ModifiableIsland::removeAllBridges);
        bridges.clear();
    }

    @Override
    public Optional<Bridge> tearDownBridge(final Island island1, final Island island2) {
        validateIslands(island1, island2);

        final Optional<ModifiableBridge> optionalBridge = findBridge(island1, island2);
        if (optionalBridge.isPresent()) {
            final ModifiableBridge bridge = optionalBridge.get();

            if (bridge.isDoubleBridge()) {
                bridge.setDoubleBridge(false);
            } else {
                ((ModifiableIsland) island1).removeBridge(bridge);
                ((ModifiableIsland) island2).removeBridge(bridge);
                bridges.remove(bridge);
            }
            return Optional.of(bridge);
        }
        return Optional.empty();
    }
}
