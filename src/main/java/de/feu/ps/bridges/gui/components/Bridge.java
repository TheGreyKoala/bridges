package de.feu.ps.bridges.gui.components;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import static de.feu.ps.bridges.gui.components.Board.HALF_CELL_SIZE;
import static de.feu.ps.bridges.gui.components.Island.ISLAND_RADIUS;

/**
 * Visual representation of a {@link de.feu.ps.bridges.model.Bridge} in the gui application.
 * @author Tim Gremplewski
 */
class Bridge extends Pane {

    private final boolean horizontal;
    private final Island island1;
    private final Island island2;
    private final Line line1;
    private final Line line2;

    private boolean doubleBridge;

    /**
     * Creates a new instance.
     * @param horizontal indicates whether the bridge is horizontal.
     * @param island1 first bridged island.
     * @param island2 second bridged island.
     */
    Bridge(final boolean horizontal, final Island island1, final Island island2) {
        this.horizontal = horizontal;
        this.doubleBridge = false;
        this.island1 = island1;
        this.island2 = island2;
        this.line1 = new Line();
        this.line2 = new Line();
        initListener(island1);
        initListener(island2);
        updateStartEndPositions();
        getChildren().add(line1);
    }

    private void initListener(final Island island) {
        final ChangeListener<Number> positionListener = (ob, o, n) -> updateStartEndPositions();
        island.layoutXProperty().addListener(positionListener);
        island.layoutYProperty().addListener(positionListener);
    }

    private void updateStartEndPositions() {
        if (doubleBridge) {
            if (horizontal) {
                setLineStartEnd(line1, 0, -ISLAND_RADIUS / 2);
                setLineStartEnd(line2, 0, ISLAND_RADIUS / 2);
            } else {
                setLineStartEnd(line1, -ISLAND_RADIUS / 2, 0);
                setLineStartEnd(line2, ISLAND_RADIUS / 2, 0);
            }
        } else {
            setLineStartEnd(line1, 0, 0);
        }
    }

    private void setLineStartEnd(final Line line, final int xOffset, final int yOffset) {
        line.setStartX(getBridgePosition(island1.getLayoutX(), xOffset));
        line.setStartY(getBridgePosition(island1.getLayoutY(), yOffset));
        line.setEndX(getBridgePosition(island2.getLayoutX(), xOffset));
        line.setEndY(getBridgePosition(island2.getLayoutY(), yOffset));
    }

    private static double getBridgePosition(final double cellPosition, final int offset) {
        return cellPosition + HALF_CELL_SIZE + offset;
    }

    /**
     * Get the first bridged island.
     * @return the first bridged island.
     */
    Island getIsland1() {
        return island1;
    }

    /**
     * Get the second bridged island.
     * @return the second bridges island.
     */
    Island getIsland2() {
        return island2;
    }

    /**
     * Indicates whether this bridge is a double bridge or not.
     * @return true, if this bridge is a double bridge, false otherwise.
     */
    boolean isDoubleBridge() {
        return doubleBridge;
    }

    /**
     * Determine whether this bridge is a double bridge or not.
     * @param doubleBridge if true, this bridge will be visualized as a double bridge.
     */
    void setDoubleBridge(final boolean doubleBridge) {
        if (doubleBridge != this.doubleBridge) {
            this.doubleBridge = doubleBridge;

            updateStartEndPositions();
            if (doubleBridge) {
                getChildren().add(line2);
            } else {
                getChildren().remove(line2);
            }
        }
    }

    /**
     * Determine if this bridge is the latest bridge in the puzzle and therewith should be highlighted.
     * @param latestBridge if true, this bridge will be highlighted as the latest bridge.
     */
    void setLatestBridge(final boolean latestBridge) {
        line1.setStroke(latestBridge ? Color.BLUE : Color.BLACK);
    }
}
