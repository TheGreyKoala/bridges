package de.feu.ps.bridges.gui.listeners.game;

import de.feu.ps.bridges.gui.components.Puzzle;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.listeners.PuzzleEventListener;
import de.feu.ps.bridges.model.Bridge;

import java.util.Optional;
import java.util.function.Supplier;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.*;

/**
 * Listener that applies changes in the puzzle's bridges in its visual representation.
 * @author Tim Gremplewski
 */
public class BridgesUpdater implements PuzzleEventListener {

    private final Supplier<Optional<Puzzle>> puzzleSupplier;

    /**
     * Creates a new instance that applies changes on the puzzle returned by the given supplier.
     * @param puzzleSupplier Supplier that returns the visual puzzle that will be used.
     */
    public BridgesUpdater(final Supplier<Optional<Puzzle>> puzzleSupplier) {
        this.puzzleSupplier = puzzleSupplier;
    }

    @Override
    public void handleEvent(final PuzzleEvent event, Object eventParameter) {
        // If a new puzzle was started, the supplier will return another object,
        // so we have to call get() every time
        final Optional<Puzzle> optionalPuzzle = puzzleSupplier.get();
        if (optionalPuzzle.isPresent()) {
            final Puzzle puzzle = optionalPuzzle.get();
            if (event == BRIDGE_ADDED) {
                puzzle.addBridge((Bridge) eventParameter);
            } else if (event == BRIDGE_REMOVED) {
                puzzle.removeBridge((Bridge) eventParameter);
            } else if (event == PUZZLE_RESET) {
                puzzle.removeAllBridges();
            }
        }
    }
}
