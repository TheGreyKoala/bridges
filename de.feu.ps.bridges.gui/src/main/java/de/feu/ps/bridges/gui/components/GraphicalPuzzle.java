package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class GraphicalPuzzle {

    public static final int CELL_SIZE = 50;
    public static final int HALF_CELL_SIZE = CELL_SIZE / 2;

    public static Pane createPuzzle(final Puzzle puzzle, final GameState gameState, final boolean showRemainingBridges) {
        Pane pane = new Pane();

        GridPane gridPane = new GridPane();
        initColumnConstraints(gridPane, puzzle.getColumnsCount());
        initRowsConstraints(gridPane, puzzle.getRowsCount());

        addIslands(gridPane, puzzle.getIslands(), gameState, showRemainingBridges);
        addBridges(pane, puzzle.getBridges(), gameState);

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

    private static void addIslands(final GridPane gridPane, final Set<Island> islands, final GameState gameState, final boolean showRemainingBridges) {
        for (Island island : islands) {
            Node graphicalIsland = GraphicalIsland.createIsland(island, gameState, showRemainingBridges);
            gridPane.add(graphicalIsland, island.getPosition().getColumn(), island.getPosition().getRow());
        }
    }

    private static void addBridges(final Pane pane, final Set<Bridge> bridges, final GameState gameState) {
        bridges.forEach(bridge -> pane.getChildren().add(GraphicalBridge.createBridge(bridge, gameState)));
    }
}
