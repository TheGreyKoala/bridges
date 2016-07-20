package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.gamestate.GameState;
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
 * Helper class to create a graphical representation of a puzzle in the gui application.
 *
 * @author Tim Gremplewski
 */
public class GraphicalPuzzle {

    /**
     * The width and height of a cell that contains an island.
     */
    public static final int CELL_SIZE = 40;

    /**
     * Half of {@link #CELL_SIZE}.
     */
    static final int HALF_CELL_SIZE = CELL_SIZE / 2;

    private GraphicalPuzzle() {
    }

    /**
     * Create a new {@link Pane} and draw the given puzzle into it.
     * @param puzzle {@link Puzzle} to draw.
     * @param gameState {@link GameState} to use.
     * @param showRemainingBridges Indicates if islands should display the number of remaining bridges.
     * @return a new {@link Pane} that contains a graphical representation of the given {@link Puzzle}.
     */
    public static Pane createPuzzle(final Puzzle puzzle, final GameState gameState, final boolean showRemainingBridges) {
        StackPane pane = new StackPane();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        initColumnConstraints(gridPane, puzzle.getColumnsCount());
        initRowsConstraints(gridPane, puzzle.getRowsCount());

        Map<Island, Node> islandGraphicalIslandMap = addIslands(gridPane, puzzle.getIslands(), gameState, showRemainingBridges);
        addBridges(pane, puzzle.getBridges(), gameState, islandGraphicalIslandMap);

        pane.getChildren().add(gridPane);

        return pane;
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

    private static Map<Island, Node> addIslands(final GridPane gridPane, final Set<Island> islands, final GameState gameState, final boolean showRemainingBridges) {
        final Map<Island, Node> island2GraphicalIsland = new HashMap<>();

        for (Island island : islands) {
            Node graphicalIsland = GraphicalIsland.createIsland(island, gameState, showRemainingBridges);
            island2GraphicalIsland.put(island, graphicalIsland);
            gridPane.add(graphicalIsland, island.getPosition().getColumn(), island.getPosition().getRow());
        }
        return island2GraphicalIsland;
    }

    private static void addBridges(final Pane pane, final Set<Bridge> bridges, final GameState gameState, Map<Island, Node> islandGraphicalIslandMap) {
        bridges.forEach(bridge -> {
            Node graphicalIsland1 = islandGraphicalIslandMap.get(bridge.getIsland1());
            Node graphicalIsland2 = islandGraphicalIslandMap.get(bridge.getIsland2());

            pane.getChildren().add(GraphicalBridge.createBridge(bridge, graphicalIsland1, graphicalIsland2, gameState));
        });
    }
}
