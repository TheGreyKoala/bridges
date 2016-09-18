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
        if (columns < 4 || columns > 25) {
            throw new IllegalArgumentException("Parameter 'columns' must be between 4 and 25.");
        }

        if (rows < 4 || rows > 25) {
            throw new IllegalArgumentException("Parameter 'rows' must be between 4 and 25.");
        }

        columnsCount = columns;
        rowsCount = rows;
        this.columns = new Column[columns];
        this.rows = new Row[rows];
        bridges = new HashSet<>();
        islands = new HashSet<>();
    }

    @Override
    public Bridge buildBridge(final Island island1, final Island island2) {
        validateIslands(island1, island2);

        final ModifiableBridge bridge;
        final Optional<ModifiableBridge> possibleDuplicate = findBridge(island1, island2);

        if (possibleDuplicate.isPresent()) {
            bridge = possibleDuplicate.get();

            if (bridge.isDoubleBridge()) {
                throw new IllegalStateException("Two bridges already exist between the given islands.");
            } else {
                bridge.setDoubleBridge(true);
            }
        } else {
            bridge = ModifiableBridgeFactory.createBridge(island1, island2, false);
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
        validatePosition(position);

        final int column = position.getColumn();
        final int row = position.getRow();

        if (columns[column] != null && columns[column].getIslandAtRow(row).isPresent()) {
            throw new IllegalStateException("An island already exists at the given position.");
        }

        final ModifiableIsland island = ModifiableIslandFactory.create(position, requiredBridges);
        addToColumn(island, column);
        addToRow(island, row);
        islands.add(island);
        return island;
    }

    private void validatePosition(final Position position) {
        Objects.requireNonNull(position, "Parameter 'position' must not be null.");

        if (position.getColumn() >= columns.length) {
            throw new IllegalArgumentException("The puzzle does not have this column: " + position.getColumn());
        }

        if (position.getRow() >= rows.length) {
            throw new IllegalArgumentException("The puzzle does not have this row: " + position.getRow());
        }
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

    @Override
    public Island getRightAndUppermostIsland() {
        for (int i = columns.length - 1; i >= 0; i--) {
            if (columns[i] != null) {
                Optional<Island> firstIsland = columns[i].getFirstUnfinishedIsland(Direction.EAST);
                if (firstIsland.isPresent()) {
                    return firstIsland.get();
                }
            }
        }
        return null;
    }

    @Override
    public Island getLeftAndUppermostIsland() {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i] != null) {
                Optional<Island> firstIsland = columns[i].getFirstUnfinishedIsland(Direction.WEST);
                if (firstIsland.isPresent()) {
                    return firstIsland.get();
                }
            }
        }
        return null;
    }
}
