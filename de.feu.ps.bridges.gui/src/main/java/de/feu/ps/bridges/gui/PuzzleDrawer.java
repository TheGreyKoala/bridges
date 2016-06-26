package de.feu.ps.bridges.gui;

import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.util.Set;


/**
 * @author Tim Gremplewski
 */
public class PuzzleDrawer {

    private static final int CELL_SIZE = 50;
    private static final int HALF_CELL_SIZE = CELL_SIZE / 2;
    private static final int ISLAND_RADIUS = 15;
    private final GameState gameState;

    private Rotate rotate = Transform.rotate(45, 0, 0);
    private Translate translate = Transform.translate(-HALF_CELL_SIZE, -HALF_CELL_SIZE);

    public PuzzleDrawer(final GameState gameState) {
        this.gameState = gameState;
    }

    public void drawPuzzle(final Puzzle puzzle, final Pane pane) {
        Set<Island> islands = puzzle.getIslands();

        GridPane gridPane = new GridPane();

        ColumnConstraints columnConstraints = new ColumnConstraints(CELL_SIZE);
        RowConstraints rowConstraints = new RowConstraints(CELL_SIZE);

        for (int i = 0; i < puzzle.getColumnsCount(); i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < puzzle.getRowsCount(); i++) {
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (Island island : islands) {
            Circle circle = new Circle(ISLAND_RADIUS, Color.BLACK);

            Text text = new Text(String.valueOf(island.getRequiredBridges()));
            text.setFont(new Font(12));
            text.setBoundsType(TextBoundsType.VISUAL);
            text.setFill(Color.WHITE);

            StackPane stackPane = new StackPane(circle, text);
            stackPane.setOnMouseClicked(createClickHandler(island, gameState));

            gridPane.add(stackPane, island.getColumn(), island.getRow());
        }

        Set<Bridge> bridges = puzzle.getBridges();

        for (Bridge bridge : bridges) {
            int startColumn = bridge.getIsland1().getColumn();
            int startRow = bridge.getIsland1().getRow();

            int endColumn = bridge.getIsland2().getColumn();
            int endRow = bridge.getIsland2().getRow();

            if (bridge.isDoubleBridge()) {
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
                pane.getChildren().addAll(line1, line2);
            } else {
                Line line = new Line(startColumn * CELL_SIZE + HALF_CELL_SIZE,
                        startRow * CELL_SIZE + HALF_CELL_SIZE,
                        endColumn * CELL_SIZE + HALF_CELL_SIZE,
                        endRow * CELL_SIZE + HALF_CELL_SIZE);

                pane.getChildren().add(line);
            }
        }

        pane.getChildren().add(gridPane);
    }

    private EventHandler<MouseEvent> createClickHandler(final Island island, final GameState gameState) {
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

    private Direction getDirection(final double x, final double y) {
        return
            x >= 0 && y >= 0 ? Direction.EAST :
            x < 0 && y >= 0 ? Direction.SOUTH :
            x < 0 && y < 0 ? Direction.WEST :
            Direction.NORTH;
    }
}
