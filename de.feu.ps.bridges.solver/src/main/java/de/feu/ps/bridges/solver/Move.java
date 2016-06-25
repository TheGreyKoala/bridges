package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

/**
 * @author Tim Gremplewski
 */
public class Move {

    private final Puzzle puzzle;
    private final Island island1;
    private final Island island2;

    private Move(final Puzzle puzzle, final Island island1, final Island island2) {
        // TODO: Test & Validation
        this.puzzle = puzzle;
        this.island1 = island1;
        this.island2 = island2;
    }

    public static Move create(final Puzzle puzzle, final Island island1, final Island island2) {
        return new Move(puzzle, island1, island2);
    }

    public void apply() {
        puzzle.buildBridge(island1, island2, false);
    }
}
