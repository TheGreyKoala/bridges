package de.feu.ps.bridges.gui.listeners.labels;

import de.feu.ps.bridges.gui.components.Puzzle;
import de.feu.ps.bridges.gui.events.SettingsEvent;
import de.feu.ps.bridges.gui.listeners.SettingsEventListener;

import java.util.Optional;
import java.util.function.Supplier;

import static de.feu.ps.bridges.gui.events.SettingsEvent.SHOW_REMAINING_BRIDGES_CHANGED;

/**
 * Listener that reacts on {@link SettingsEvent#SHOW_REMAINING_BRIDGES_CHANGED}
 * and lets the islands in the visual puzzle display the correct number.
 *
 * @author Tim Gremplewski
 */
public class IslandTextUpdater implements SettingsEventListener {

    private final Supplier<Optional<Puzzle>> puzzleSupplier;

    /**
     * Creates a new instance that works on the puzzle returned by the given supplier.
     * @param puzzleSupplier Supplier that returns the visual puzzle to use.
     */
    public IslandTextUpdater(final Supplier<Optional<Puzzle>> puzzleSupplier) {
        this.puzzleSupplier = puzzleSupplier;
    }

    @Override
    public void handleEvent(final SettingsEvent event, final Object eventParameter) {
        if (event == SHOW_REMAINING_BRIDGES_CHANGED) {
            final Optional<Puzzle> puzzle = puzzleSupplier.get();
            if (puzzle.isPresent()) {
                puzzle.get().setShowRemainingBridges(((boolean) eventParameter));
            }
        }
    }
}
