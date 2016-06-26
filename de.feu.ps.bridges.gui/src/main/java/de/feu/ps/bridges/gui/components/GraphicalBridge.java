package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.model.Bridge;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import static de.feu.ps.bridges.gui.components.GraphicalIsland.ISLAND_RADIUS;
import static de.feu.ps.bridges.gui.components.GraphicalPuzzle.CELL_SIZE;
import static de.feu.ps.bridges.gui.components.GraphicalPuzzle.HALF_CELL_SIZE;

/**
 * @author Tim Gremplewski
 */
public class GraphicalBridge {

    public static Node createBridge(final Bridge bridge, final GameState gameState) {
        Pane pane = new Pane();

        int startColumn = bridge.getIsland1().getColumn();
        int startRow = bridge.getIsland1().getRow();

        int endColumn = bridge.getIsland2().getColumn();
        int endRow = bridge.getIsland2().getRow();

        boolean latestBridge = gameState.isLatestBridge(bridge);

        if (bridge.isDoubleBridge()) {
            createDoubleBridge(bridge, pane, startColumn, startRow, endColumn, endRow, latestBridge);
        } else {
            createSingleBridge(pane, startColumn, startRow, endColumn, endRow, latestBridge);
        }

        return pane;
    }

    private static void createDoubleBridge(Bridge bridge, Pane pane, int startColumn, int startRow, int endColumn, int endRow, boolean latestBridge) {
        Line line1;
        Line line2;
        if (bridge.isHorizontal()) {
            line1 = new Line(startColumn * CELL_SIZE + HALF_CELL_SIZE,
                    startRow * CELL_SIZE + HALF_CELL_SIZE - ISLAND_RADIUS / 2,
                    endColumn * CELL_SIZE + HALF_CELL_SIZE,
                    endRow * CELL_SIZE + HALF_CELL_SIZE - ISLAND_RADIUS / 2);
            line2 = new Line(startColumn * CELL_SIZE + HALF_CELL_SIZE,
                    startRow * CELL_SIZE + HALF_CELL_SIZE + ISLAND_RADIUS / 2,
                    endColumn * CELL_SIZE + HALF_CELL_SIZE,
                    endRow * CELL_SIZE + HALF_CELL_SIZE + ISLAND_RADIUS / 2);
        } else {
            line1 = new Line(startColumn * CELL_SIZE + HALF_CELL_SIZE - ISLAND_RADIUS / 2,
                    startRow * CELL_SIZE + HALF_CELL_SIZE,
                    endColumn * CELL_SIZE + HALF_CELL_SIZE - ISLAND_RADIUS / 2,
                    endRow * CELL_SIZE + HALF_CELL_SIZE);
            line2 = new Line(startColumn * CELL_SIZE + HALF_CELL_SIZE + ISLAND_RADIUS / 2,
                    startRow * CELL_SIZE + HALF_CELL_SIZE,
                    endColumn * CELL_SIZE + HALF_CELL_SIZE + ISLAND_RADIUS / 2,
                    endRow * CELL_SIZE + HALF_CELL_SIZE);
        }

        if (latestBridge) {
            line1.setStroke(Color.BLUE);
        }
        pane.getChildren().addAll(line1, line2);
    }

    private static void createSingleBridge(Pane pane, int startColumn, int startRow, int endColumn, int endRow, boolean latestBridge) {
        Line line = new Line(startColumn * CELL_SIZE + HALF_CELL_SIZE,
                startRow * CELL_SIZE + HALF_CELL_SIZE,
                endColumn * CELL_SIZE + HALF_CELL_SIZE,
                endRow * CELL_SIZE + HALF_CELL_SIZE);

        if (latestBridge) {
            line.setStroke(Color.BLUE);
        }

        pane.getChildren().add(line);
    }
}
