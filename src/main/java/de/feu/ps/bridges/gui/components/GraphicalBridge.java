package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.model.Model;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import static de.feu.ps.bridges.gui.components.GraphicalIsland.ISLAND_RADIUS;
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
     * @param island1 Graphical representation of the first bridged {@link Island}
     * @param island2 Graphical representation of the second bridged {@link Island}
     * @param model {@link Model} to use
     * @return A graphical representation of the given {@link Bridge}.
     */
    public static Node createBridge(final Bridge bridge, final Node island1, final Node island2, final Model model) {
        final Pane pane = new Pane();

        if (bridge.isDoubleBridge()) {
            addDoubleBridge(pane, bridge, island1, island2);
        } else {
            addSingleBridge(pane, island1, island2);
        }

        if (model.getGameState().isLatestBridge(bridge)) {
            ((Line) pane.getChildren().get(0)).setStroke(Color.BLUE);
        }

        return pane;
    }

    private static void addDoubleBridge(final Pane pane, final Bridge bridge, final Node island1, final Node island2) {
        Line line;
        Line doubleLine;
        if (bridge.isHorizontal()) {
            line = initLine(island1, island2, 0, -ISLAND_RADIUS / 2);
            doubleLine = initLine(island1, island2, 0, ISLAND_RADIUS / 2);
        } else {
            line = initLine(island1, island2, -ISLAND_RADIUS / 2, 0);
            doubleLine = initLine(island1, island2, ISLAND_RADIUS / 2, 0);
        }
        pane.getChildren().addAll(line, doubleLine);
    }

    private static void addSingleBridge(final Pane pane, final Node island1, final Node island2) {
        pane.getChildren().add(initLine(island1, island2, 0, 0));
    }

    private static Line initLine(final Node island1, final Node island2, final int xOffset, final int yOffset) {
        Line line = new Line();

        island1.layoutXProperty().addListener((observable, oldValue, newValue) -> {
            line.setStartX(newValue.doubleValue() + HALF_CELL_SIZE + xOffset);
        });

        island1.layoutYProperty().addListener((observable, oldValue, newValue) -> {
            line.setStartY(newValue.doubleValue() + HALF_CELL_SIZE + yOffset);
        });

        island2.layoutXProperty().addListener((observable, oldValue, newValue) -> {
            line.setEndX(newValue.doubleValue() + HALF_CELL_SIZE + xOffset);
        });

        island2.layoutYProperty().addListener((observable, oldValue, newValue) -> {
            line.setEndY(newValue.doubleValue() + HALF_CELL_SIZE + yOffset);
        });

        return line;
    }
}
