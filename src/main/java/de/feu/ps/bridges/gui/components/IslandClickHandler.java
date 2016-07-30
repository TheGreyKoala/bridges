package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.model.Model;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import static de.feu.ps.bridges.gui.components.PuzzleNodeFactory.HALF_CELL_SIZE;

/**
 * @author Tim Gremplewski
 */
class IslandClickHandler implements EventHandler<MouseEvent> {
    private static Rotate rotate45DegreesClockwise = Transform.rotate(45, 0, 0);
    private static Translate translateOriginToCircleCenter = Transform.translate(-HALF_CELL_SIZE, -HALF_CELL_SIZE);

    private final Island island;
    private final Model model;

    IslandClickHandler(final Island island, final Model model) {
        this.island = island;
        this.model = model;
    }

    @Override
    public void handle(final MouseEvent event) {
        final Point2D pointWhenCircleCenterOrigin = translateOriginToCircleCenter.transform(event.getX(), event.getY());
        final Point2D pointOnCartesianSystem = rotate45DegreesClockwise.transform(pointWhenCircleCenterOrigin);

        if (event.getButton() == MouseButton.PRIMARY
            || event.getButton() == MouseButton.SECONDARY) {

            final Direction direction = getDirection(pointOnCartesianSystem.getX(), pointOnCartesianSystem.getY());
            if (event.getButton() == MouseButton.PRIMARY) {
                model.tryBuildBridge(island, direction);
            } else {
                model.tearDownBridge(island, direction);
            }
        }
    }

    private Direction getDirection(final double x, final double y) {
        return x >= 0 && y >= 0 ? Direction.EAST :
               x < 0 && y >= 0 ? Direction.SOUTH :
               x < 0 && y < 0 ? Direction.WEST :
               Direction.NORTH;
    }
}
