package de.feu.ps.bridges.model;

import java.awt.geom.Line2D;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tim Gremplewski
 */
class DefaultPuzzle implements ModifiablePuzzle {

    private int columnsCount;
    private final int rowsCount;

    private Map<Integer, Column> columns;
    private Map<Integer, Row> rows;

    private Set<ModifiableBridge> bridges;

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
    public Island buildIsland(final int columnIndex, final int rowIndex, final int requiredBridges) {
        // TODO Create Builder?
        final ModifiableIsland island = new DefaultIsland(columnIndex, rowIndex, requiredBridges);

        final Column column;
        if (columns.containsKey(columnIndex)) {
            column = columns.get(columnIndex);
        } else {
            column = new Column(columnIndex);
            columns.put(columnIndex, column);
        }

        column.addIsland(island);

        final Row row;
        if (rows.containsKey(rowIndex)) {
            row = rows.get(rowIndex);
        } else {
            row = new Row(rowIndex);
            rows.put(rowIndex, row);
        }

        row.addIsland(island);

        return island;
    }

    @Override
    public Bridge buildBridge(final Island island1, final Island island2, final boolean doubleBridge) {
        // TODO validate bridge and reject

        ModifiableBridge bridge;
        Optional<ModifiableBridge> possibleDuplicate = findBridge(island1, island2);

        if (possibleDuplicate.isPresent()) {
            bridge = possibleDuplicate.get();
            bridge.setDoubleBridge(true);
        } else {
            // TODO What if island already has enough islands
            bridge = ModifiableBridgeFactory.createBridge(island1, island2, doubleBridge);
            bridges.add(bridge);

            // TODO: This is ugly but we need to verify if the islands are in this puzzle anyway.
            // If we check, we know that this implementation only uses ModifiableIslands
            ((ModifiableIsland) bridge.getIsland1()).addBridge(bridge);
            ((ModifiableIsland) bridge.getIsland2()).addBridge(bridge);
        }

        return bridge;
    }

    private Optional<ModifiableBridge> findBridge(final Island island1, final Island island2) {
        return bridges.stream()
                .filter(bridge1 -> bridge1.getBridgedIslands().containsAll(
                        Arrays.asList(island1, island2))).findFirst();
    }

    @Override
    public void removeAllBridges() {
        // TODO the cast is ugly
        bridges.forEach(bridge -> bridge.getBridgedIslands().forEach(island -> ((ModifiableIsland)island).removeAllBridges()));
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
                        .filter(bridge1 -> bridge1.getBridgedIslands().containsAll(
                                bridge.getBridgedIslands())).findFirst();

        if (possibleDuplicate.isPresent()) {
            Bridge duplicate = possibleDuplicate.get();
            if (duplicate.isDoubleBridge()) {
                duplicate.setDoubleBridge(false);
            } else {
                bridges.remove(duplicate);
                duplicate.getBridgedIslands().forEach(island -> island.removeBridge(bridge));
            }
            possibleDuplicate.get().setDoubleBridge(false);
        }
    }*/

    @Override
    public Optional<Bridge> tearDownBridge(final Island island1, final Island island2) {
        Optional<ModifiableBridge> optionalBridge = findBridge(island1, island2);
        if (optionalBridge.isPresent()) {
            ModifiableBridge bridge = optionalBridge.get();
            if (bridge.isDoubleBridge()) {
                bridge.setDoubleBridge(false);
            } else {
                // TODO The cast is ugly
                bridge.getBridgedIslands().forEach(island -> ((ModifiableIsland) island).removeBridge(bridge));
                bridges.remove(bridge);
            }
            return Optional.of(bridge);
        }
        return Optional.empty();
    }

    @Override
    public Set<Island> getUnfinishedIslands() {
        return getIslands().stream()
                .filter(island -> island.getRemainingBridges() > 0)
                .collect(Collectors.toSet());
    }
}
