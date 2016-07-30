package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import static de.feu.ps.bridges.gui.components.IslandNodeFactory.ISLAND_RADIUS;
import static de.feu.ps.bridges.gui.components.PuzzleNodeFactory.HALF_CELL_SIZE;

/**
 * Helper class to create a graphical representation of a {@link Bridge}.
 * @author Tim Gremplewski
 */
class BridgeNodeFactory {

    private BridgeNodeFactory() {
    }

    /**
     * Create a new graphical representation of the given {@link Bridge}.
     * @param bridge {@link Bridge} to be drawn.
     * @param island1 Graphical representation of the first bridged {@link Island}
     * @param island2 Graphical representation of the second bridged {@link Island}
     * @param latestBridge if true, the bridge will be highlighted
     * @return A graphical representation of the given {@link Bridge}.
     */
    static Node createBridge(final Bridge bridge, final Node island1, final Node island2, final boolean latestBridge) {
        final Pane pane = new Pane();

        if (bridge.isDoubleBridge()) {
            if (bridge.isHorizontal()) {
                addHorizontalDoubleBridge(pane, island1, island2);
            } else {
                addVerticalDoubleBridge(pane, island1, island2);
            }
        } else {
            addSingleBridge(pane, island1, island2);
        }

        if (latestBridge) {
            ((Line) pane.getChildren().get(0)).setStroke(Color.BLUE);
        }

        return pane;
    }

    private static void addHorizontalDoubleBridge(final Pane pane, final Node island1, final Node island2) {
        pane.getChildren().addAll(
            createLine(island1, island2, 0, -ISLAND_RADIUS / 2),
            createLine(island1, island2, 0, ISLAND_RADIUS / 2));
    }

    private static void addVerticalDoubleBridge(final Pane pane, final Node island1, final Node island2) {
        pane.getChildren().addAll(
            createLine(island1, island2, -ISLAND_RADIUS / 2, 0),
            createLine(island1, island2, ISLAND_RADIUS / 2, 0));
    }

    private static void addSingleBridge(final Pane pane, final Node island1, final Node island2) {
        pane.getChildren().add(createLine(island1, island2, 0, 0));
    }

    private static Line createLine(final Node island1, final Node island2, final int xOffset, final int yOffset) {
        final Line line = new Line(
                getBridgePosition(island1.getLayoutX(), xOffset),
                getBridgePosition(island1.getLayoutX(), yOffset),
                getBridgePosition(island2.getLayoutX(), xOffset),
                getBridgePosition(island2.getLayoutX(), yOffset));

        island1.layoutXProperty().addListener(
            (observable, oldValue, newValue) -> line.setStartX(getBridgePosition(newValue.doubleValue(), xOffset)));

        island1.layoutYProperty().addListener(
            (observable, oldValue, newValue) -> line.setStartY(getBridgePosition(newValue.doubleValue(), yOffset)));

        island2.layoutXProperty().addListener(
            (observable, oldValue, newValue) -> line.setEndX(getBridgePosition(newValue.doubleValue(), xOffset)));

        island2.layoutYProperty().addListener(
            (observable, oldValue, newValue) -> line.setEndY(getBridgePosition(newValue.doubleValue(), yOffset)));

        return line;
    }

    private static double getBridgePosition(final double cellPosition, final int offset) {
        return cellPosition + HALF_CELL_SIZE + offset;
    }
}
