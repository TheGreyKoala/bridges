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
 * Helper class to create a graphical representation of a {@link Bridge}.
 * @author Tim Gremplewski
 */
public class GraphicalBridge {

    private GraphicalBridge() {
    }

    /**
     * Create a new graphical representation of the given {@link Bridge}.
     * @param bridge {@link Bridge} to be drawn.
     * @param gameState {@link GameState} to use.
     * @return a new {@link Node} that contains the drawn bridge.
     */
    public static Node createBridge(final Bridge bridge, final GameState gameState) {
        Pane pane = new Pane();

        int startColumn = bridge.getIsland1().getPosition().getColumn();
        int startRow = bridge.getIsland1().getPosition().getRow();

        int endColumn = bridge.getIsland2().getPosition().getColumn();
        int endRow = bridge.getIsland2().getPosition().getRow();

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
