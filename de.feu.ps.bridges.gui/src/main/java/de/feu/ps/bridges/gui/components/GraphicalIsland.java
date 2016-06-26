package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import static de.feu.ps.bridges.gui.components.GraphicalPuzzle.HALF_CELL_SIZE;

/**
 * @author Tim Gremplewski
 */
public class GraphicalIsland {

    public static final int ISLAND_RADIUS = 15;

    private static Rotate rotate = Transform.rotate(45, 0, 0);
    private static Translate translate = Transform.translate(-HALF_CELL_SIZE, -HALF_CELL_SIZE);

    public static Node createIsland(final Island island, final GameState gameState, final boolean showRemainingBridges) {
        Color color = island.getRemainingBridges() == 0 ? Color.GREEN : Color.BLACK;
        Circle circle = new Circle(ISLAND_RADIUS, color);

        int displayedNumber = showRemainingBridges ? island.getRemainingBridges() : island.getRequiredBridges();

        Text text = new Text(String.valueOf(displayedNumber));
        text.setFont(new Font(12));
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.WHITE);

        StackPane stackPane = new StackPane(circle, text);
        stackPane.setOnMouseClicked(createClickHandler(island, gameState));

        return stackPane;
    }

    private static EventHandler<MouseEvent> createClickHandler(final Island island, final GameState gameState) {
        return (event -> {
            Point2D pointWhenCircleCenterOrigin = translate.transform(event.getX(), event.getY());
            Point2D pointOnCartesianSystem = rotate.transform(pointWhenCircleCenterOrigin);

            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
                boolean addBridge = event.getButton() == MouseButton.PRIMARY;
                Direction direction = getDirection(pointOnCartesianSystem.getX(), pointOnCartesianSystem.getY());

                if (addBridge) {
                    gameState.addBridge(island, direction);
                } else {
                    gameState.removeBridge(island, direction);
                }
            }
        });
    }

    private static Direction getDirection(final double x, final double y) {
        return
            x >= 0 && y >= 0 ? Direction.EAST :
            x < 0 && y >= 0 ? Direction.SOUTH :
            x < 0 && y < 0 ? Direction.WEST :
            Direction.NORTH;
    }
}
