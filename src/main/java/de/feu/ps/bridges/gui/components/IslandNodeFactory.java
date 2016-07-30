package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.model.Model;
import de.feu.ps.bridges.model.Island;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * Factory class that creates a new {@link Node} that visualizes an {@link Island}.
 * The {@link Node} will have a click handler to build and tear down bridges.
 *
 * @author Tim Gremplewski
 */
class IslandNodeFactory {

    /**
     * The radius of an island.
     */
    static final int ISLAND_RADIUS = 10;

    private IslandNodeFactory() {
    }

    /**
     * Create a new {@link Node} that visualizes the given {@link Island}.
     * @param island {@link Island} to be visualized.
     * @param model {@link Model} that will be triggered on click events.
     * @param showRemainingBridges Indicates if the island should display the number of remaining bridges.
     * @return a new {@link Node} that visualizes the given {@link Island}.
     */
    static Node createIslandNode(final Island island, final Model model, final boolean showRemainingBridges) {
        final Circle circle = createCircle(island.getRemainingBridges());
        final Text text = createText(island, showRemainingBridges);

        final StackPane stackPane = new StackPane(circle, text);
        stackPane.setOnMouseClicked(new IslandClickHandler(island, model));

        return stackPane;
    }

    private static Circle createCircle(final int remainingBridges) {
        final Color color = remainingBridges == 0 ? Color.GREEN : Color.BLACK;
        return new Circle(ISLAND_RADIUS, color);
    }

    private static Text createText(final Island island, final boolean showRemainingBridges) {
        final int displayedNumber = showRemainingBridges ? island.getRemainingBridges() : island.getRequiredBridges();
        final Text text = new Text(String.valueOf(displayedNumber));
        text.setFont(new Font(12));
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.WHITE);
        return text;
    }
}
