package de.feu.ps.bridges.model;

import java.awt.geom.Line2D;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tim Gremplewski
 */
public class DefaultPuzzle implements ModifiablePuzzle {

    private int columnsCount;
    private final int rowsCount;

    private Map<Integer, Column> columns;
    private Map<Integer, Row> rows;

    private Set<Bridge> bridges;

    public DefaultPuzzle(final int columns, final int rows) {

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
    public Bridge buildBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        // TODO validate bridge and reject

        Bridge bridge;
        Optional<Bridge> possibleDuplicate = findBridge(island1, island2);

        if (possibleDuplicate.isPresent()) {
            bridge = possibleDuplicate.get();
            bridge.setDoubleBridge(true);
        } else {
            // TODO What if island already has enough islands
            bridge = BridgeBuilder.buildBridge(island1, island2, doubleBridge);
            bridges.add(bridge);
            bridge.getIsland1().addBridge(bridge);
            bridge.getIsland2().addBridge(bridge);
        }

        return bridge;
    }

    private Optional<Bridge> findBridge(final Island island1, final Island island2) {
        return bridges.stream()
                .filter(bridge1 -> bridge1.getConnectedIslands().containsAll(
                        Arrays.asList(island1, island2))).findFirst();
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

    @Override
    public boolean isAnyBridgeCrossing(final Position otherBridgeStart, final Position otherBridgeEnd) {

        // TODO: TEST!

        return bridges.stream().anyMatch(bridge -> {
            Position bridgeStart = bridge.getIsland1().getPosition();
            Position bridgeEnd = bridge.getIsland2().getPosition();

            boolean linesIntersect = Line2D.linesIntersect(
                bridgeStart.getColumn(),
                bridgeStart.getRow(),
                bridgeEnd.getColumn(),
                bridgeEnd.getRow(),
                otherBridgeStart.getColumn(),
                otherBridgeStart.getRow(),
                otherBridgeEnd.getColumn(),
                otherBridgeEnd.getRow());

            boolean bridgesShareSingeIsland =
                bridgeStart.equals(otherBridgeStart)
                    ^ bridgeStart.equals(otherBridgeEnd)
                    ^ bridgeEnd.equals(otherBridgeStart)
                    ^ bridgeEnd.equals(otherBridgeEnd);

            boolean bridgesConnectTheSameIsland =
                (bridgeStart.equals(otherBridgeStart) || bridgeStart.equals(otherBridgeEnd))
                    && (bridgeEnd.equals(otherBridgeEnd) || bridgeEnd.equals(otherBridgeStart))
                    && !bridge.isDoubleBridge();

            return linesIntersect && !(bridgesShareSingeIsland || bridgesConnectTheSameIsland);
        });
    }

    /*@Override
    public void removeBridge(Bridge bridge) {
        Optional<Bridge> possibleDuplicate =
                bridges.stream()
                        .filter(bridge1 -> bridge1.getConnectedIslands().containsAll(
                                bridge.getConnectedIslands())).findFirst();

        if (possibleDuplicate.isPresent()) {
            Bridge duplicate = possibleDuplicate.get();
            if (duplicate.isDoubleBridge()) {
                duplicate.setDoubleBridge(false);
            } else {
                bridges.remove(duplicate);
                duplicate.getConnectedIslands().forEach(island -> island.removeBridge(bridge));
            }
            possibleDuplicate.get().setDoubleBridge(false);
        }
    }*/

    @Override
    public void tearDownBridge(final Island island1, final Island island2) {
        Optional<Bridge> optionalBridge = findBridge(island1, island2);
        if (optionalBridge.isPresent()) {
            Bridge bridge = optionalBridge.get();
            if (bridge.isDoubleBridge()) {
                bridge.setDoubleBridge(false);
            } else {
                bridge.getConnectedIslands().forEach(island -> island.removeBridge(bridge));
                bridges.remove(bridge);
            }
        }
    }

    @Override
    public Set<Island> getUnfinishedIslands() {
        return getIslands().stream()
                .filter(island -> island.getRemainingBridges() > 0)
                .collect(Collectors.toSet());
    }
}
