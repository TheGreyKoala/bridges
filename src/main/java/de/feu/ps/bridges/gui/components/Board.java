package de.feu.ps.bridges.gui.components;

import de.feu.ps.bridges.gui.model.Model;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to manage the visible islands in the gui application and to arrange them in a grid.
 * @author Tim Gremplewski
 */
class Board extends GridPane {

    private static final int CELL_SIZE = 50;

    /**
     * Half width and height of a cell containing an island.
     */
    static final int HALF_CELL_SIZE = CELL_SIZE / 2;

    private final int columns;
    private final int rows;
    private final Model model;
    private final Map<de.feu.ps.bridges.model.Island, Island> islands;

    /**
     * Creates a new instance.
     * @param columns Amount of columns in the grid.
     * @param rows Amount of rows in the grid.
     * @param model model to use.
     */
    Board(final int columns, final int rows, final Model model) {
        this.columns = columns;
        this.rows = rows;
        this.model = model;
        islands = new HashMap<>();
        setAlignment(Pos.CENTER);
        initColumnConstraints();
        initRowsConstraints();
    }

    private void initColumnConstraints() {
        ColumnConstraints columnConstraints = new ColumnConstraints(CELL_SIZE);
        for (int i = 0; i < columns; i++) {
            getColumnConstraints().add(columnConstraints);
        }
    }

    private void initRowsConstraints() {
        RowConstraints rowConstraints = new RowConstraints(CELL_SIZE);
        for (int i = 0; i < rows; i++) {
            getRowConstraints().add(rowConstraints);
        }
    }

    /**
     * Create a visual island for the given {@link de.feu.ps.bridges.model.Island} and add it to this grid.
     * @param island island to add to the grid.
     * @return the newly created visual island.
     */
    Island addIsland(final de.feu.ps.bridges.model.Island island) {
        final Island newIsland = new Island(island, model);
        islands.put(island, newIsland);
        add(newIsland, island.getPosition().getColumn(), island.getPosition().getRow());
        return newIsland;
    }

    /**
     * Get the visual island for the given {@link de.feu.ps.bridges.model.Island}.
     * @param island requested island.
     * @return visual island corresponding to the given island.
     */
    Island getIsland(final de.feu.ps.bridges.model.Island island) {
        return islands.get(island);
    }

    /**
     * Determine whether the islands in the grid should show their remaining amount of bridges or their required amount of bridges.
     * @param showRemainingBridges if true, the islands will show their remaining bridges.
     */
    void setShowRemainingBridges(final boolean showRemainingBridges) {
        islands.values().forEach(island -> island.setShowRemainingBridges(showRemainingBridges));
    }
}
