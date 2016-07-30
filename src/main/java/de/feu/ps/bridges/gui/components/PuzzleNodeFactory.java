package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.model.GameState;
import de.feu.ps.bridges.gui.model.Model;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Factory class that creates a new {@link Pane} that visualizes a {@link Puzzle}.
 *
 * @author Tim Gremplewski
 */
public class PuzzleNodeFactory {

    private static final int CELL_SIZE = 40;

    /**
     * Half width and height of a cell containing an island.
     */
    static final int HALF_CELL_SIZE = CELL_SIZE / 2;

    private PuzzleNodeFactory() {
    }

    /**
     * Create a new {@link Pane} that visualizes the given {@link Puzzle}.
     * @param puzzle {@link Puzzle} to visualizes.
     * @param model {@link Model} whose {@link GameState} will be used and that will be triggered on island click events.
     * @param showRemainingBridges Indicates if islands should display the number of remaining bridges.
     * @return a new {@link Pane} that visualizes the given {@link Puzzle}.
     */
    public static Pane createPuzzle(final Puzzle puzzle, final Model model, final boolean showRemainingBridges) {
        final Map<Island, Node> islandNodes = createIslandNodes(puzzle.getIslands(), model, showRemainingBridges);
        final Map<Bridge, Node> bridgeNodes = createBridgeNodes(puzzle.getBridges(), islandNodes, model.getGameState());
        final GridPane gridPane = createGridPane(puzzle.getColumnsCount(), puzzle.getRowsCount(), islandNodes);
        return createStackPane(gridPane, bridgeNodes);
    }

    private static GridPane createGridPane(final int columns, final int rows, final Map<Island, Node> islandNodes) {
        final GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        initColumnConstraints(gridPane, columns);
        initRowsConstraints(gridPane, rows);

        islandNodes.forEach((island, node) -> gridPane.add(node, island.getPosition().getColumn(), island.getPosition().getRow()));

        return gridPane;
    }

    private static void initColumnConstraints(final GridPane gridPane, final int columns) {
        ColumnConstraints columnConstraints = new ColumnConstraints(CELL_SIZE);
        for (int i = 0; i < columns; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }
    }

    private static void initRowsConstraints(final GridPane gridPane, final int rows) {
        RowConstraints rowConstraints = new RowConstraints(CELL_SIZE);
        for (int i = 0; i < rows; i++) {
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private static Map<Island, Node> createIslandNodes(final Set<Island> islands, final Model model, final boolean showRemainingBridges) {
        final Map<Island, Node> islandNodes = new HashMap<>();
        for (final Island island : islands) {
            final Node islandNode = IslandNodeFactory.createIslandNode(island, model, showRemainingBridges);
            islandNodes.put(island, islandNode);
        }
        return islandNodes;
    }

    private static Map<Bridge, Node> createBridgeNodes(final Set<Bridge> bridges, final Map<Island, Node> islandNodes, final GameState gameState) {
        final Map<Bridge, Node> bridgeNodes = new HashMap<>();
        bridges.forEach(bridge -> {
            boolean latestBridge = gameState.isLatestBridge(bridge);
            Node bridgeNode = BridgeNodeFactory.createBridgeNode(bridge, islandNodes.get(bridge.getIsland1()), islandNodes.get(bridge.getIsland2()), latestBridge);
            bridgeNodes.put(bridge, bridgeNode);
        });
        return bridgeNodes;
    }

    private static StackPane createStackPane(final GridPane gridPane, final Map<Bridge, Node> bridgeNodes) {
        final StackPane pane = new StackPane();
        bridgeNodes.forEach((bridge, node) -> pane.getChildren().add(node));
        pane.getChildren().add(gridPane);
        return pane;
    }
}
