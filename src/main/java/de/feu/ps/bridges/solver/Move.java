package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import java.util.Objects;

/**
 * Represents a save move for a puzzle.
 * The move can also be applied on the puzzle.
 *
 * @author Tim Gremplewski
 */
public class Move {

    private final Puzzle puzzle;
    private final Island island1;
    private final Island island2;

    private Move(final Puzzle puzzle, final Island island1, final Island island2) {
        this.puzzle = Objects.requireNonNull(puzzle, "Parameter 'puzzle' must not be null.");
        this.island1 = Objects.requireNonNull(island1, "Parameter 'island1' must not be null.");
        this.island2 = Objects.requireNonNull(island2, "Parameter 'island2' must not be null.");
    }

    /**
     * Create a new instance.
     * @param puzzle {@link Puzzle} to which this move belongs.
     * @param island1 {@link Island} that is part of this move.
     * @param island2 Another {@link Island} that is part of this move.
     * @return a new instance.
     */
    static Move create(final Puzzle puzzle, final Island island1, final Island island2) {
        return new Move(puzzle, island1, island2);
    }

    /**
     * Apply this move on the puzzle.
     * @return the {@link Bridge} that was created when applying this move.
     */
    public Bridge apply() {
        return puzzle.buildBridge(island1, island2);
    }
}
