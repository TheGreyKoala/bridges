package de.feu.ps.bridges.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class PuzzleImpl implements Puzzle {

    private int columnsCount;
    private final int rowsCount;

    private Map<Integer, Column> columns;
    private Map<Integer, Row> rows;

    private Set<Bridge> bridges;

    public PuzzleImpl(final int columns, final int rows) {

        //TODO Parameter validation

        columnsCount = columns;
        rowsCount = rows;

        this.columns = new HashMap<>(columns);
        this.rows = new HashMap<>(rows);

        bridges = new HashSet<>();
    }

    @Override
    public int getColumnsCount() {
        return columnsCount;
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }

    @Override
    public Set<Island> getIslands() {
        Set<Island> islands = new HashSet<>();
        columns.forEach((integer, column) -> islands.addAll(column.getIslands()));
        return islands;
    }

    @Override
    public Set<Bridge> getBridges() {
        return new HashSet<>(bridges);
    }

    @Override
    public void addIsland(Island island) {
        int columnIndex = island.getColumn();
        int rowIndex = island.getRow();

        Column column;
        if (columns.containsKey(columnIndex)) {
            column = columns.get(columnIndex);
        } else {
            column = new Column(columnIndex);
            columns.put(columnIndex, column);
        }

        column.addIsland(island);

        Row row;
        if (rows.containsKey(rowIndex)) {
            row = rows.get(rowIndex);
        } else {
            row = new Row(rowIndex);
            rows.put(rowIndex, row);
        }

        row.addIsland(island);
    }

    @Override
    public void addBridge(Bridge bridge) {
        // TODO validate bridge

        bridges.add(bridge);
        bridge.getIsland1().addBridge(bridge);
        bridge.getIsland2().addBridge(bridge);
    }

    @Override
    public PuzzleStatus getStatus() {
        //TODO Implement de.feu.ps.bridges.model.Puzzle.getStatus
        return null;
    }

    @Override
    public Island getIslandAt(int column, int row) {
        return columns.get(column).getIslandAtRow(row);
    }

    @Override
    public void removeAllBridges() {
        bridges.forEach(bridge -> bridge.getConnectedIslands().forEach(Island::removeAllBridges));
        bridges.clear();
    }
}
