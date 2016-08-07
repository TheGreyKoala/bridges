package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.model.Model;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * Visual representation of a {@link de.feu.ps.bridges.model.Island} in the gui application.
 * @author Tim Gremplewski
 */
class Island extends StackPane {

    /**
     * Radius of a visualized island.
     */
    static final int ISLAND_RADIUS = 15;

    private final de.feu.ps.bridges.model.Island island;
    private final Circle circle;
    private final Text text;

    private boolean showRemainingBridges;

    /**
     * Creates a new instance.
     * @param island Island to be visualized.
     * @param model Model to be used.
     */
    Island(final de.feu.ps.bridges.model.Island island, final Model model) {
        this.island = island;
        circle = createCircle();
        text = createText();
        setShowRemainingBridges(false);
        getChildren().addAll(circle, text);
        setOnMouseClicked(new IslandClickHandler(island, model));
    }

    private Circle createCircle() {
        return new Circle(ISLAND_RADIUS, getCircleFill());
    }

    private Color getCircleFill() {
        return island.getRemainingBridges() == 0 ? Color.GREEN : Color.BLACK;
    }

    private Text createText() {
        final Text text = new Text(String.valueOf(island.getRequiredBridges()));
        text.setFont(new Font(12));
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.WHITE);
        return text;
    }

    /**
     * Determine if this island should display its amount of remaining bridges.
     * @param showRemainingBridges if true, this island will display its amount of remaining bridges.
     */
    void setShowRemainingBridges(final boolean showRemainingBridges) {
        this.showRemainingBridges = showRemainingBridges;
        updateTextContent();
    }

    /**
     * Update the visual status of this island.
     */
    void update() {
        updateCircleFill();
        updateTextContent();
    }

    private void updateCircleFill() {
        circle.setFill(getCircleFill());
    }

    private void updateTextContent() {
        final int displayedNumber = showRemainingBridges ? island.getRemainingBridges() : island.getRequiredBridges();
        text.setText(String.valueOf(displayedNumber));
    }
}
