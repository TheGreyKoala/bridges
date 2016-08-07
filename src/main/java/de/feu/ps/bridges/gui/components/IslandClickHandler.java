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

import static de.feu.ps.bridges.gui.components.Board.HALF_CELL_SIZE;


/**
 * {@link EventHandler} for click events on an island.
 * Handles clicks of the primary (typically left) and secondary (typically right) button.
 *
 * @author Tim Gremplewski
 */
class IslandClickHandler implements EventHandler<MouseEvent> {

    private static final Rotate ROTATE_45_DEGREES_CLOCKWISE = Transform.rotate(45, 0, 0);
    private static final Translate TRANSLATE_ORIGIN_TO_CIRCLE_CENTER = Transform.translate(-HALF_CELL_SIZE, -HALF_CELL_SIZE);

    private final Island island;
    private final Model model;

    /**
     * Create a new click handler that will trigger methods of the given {@link Model}
     * and use the given {@link Island} as the counterpart of the clicked graphical island.
     * @param island {@link Island} that will be used as the counterpart for the clicked graphical island.
     * @param model {@link Model} which will be triggered on click events.
     */
    IslandClickHandler(final Island island, final Model model) {
        this.island = island;
        this.model = model;
    }

    @Override
    public void handle(final MouseEvent event) {
        final Point2D pointWhenCircleCenterOrigin = TRANSLATE_ORIGIN_TO_CIRCLE_CENTER.transform(event.getX(), event.getY());
        final Point2D pointOnCartesianSystem = ROTATE_45_DEGREES_CLOCKWISE.transform(pointWhenCircleCenterOrigin);

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
