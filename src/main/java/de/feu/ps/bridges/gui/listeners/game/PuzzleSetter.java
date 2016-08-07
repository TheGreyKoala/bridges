package de.feu.ps.bridges.gui.listeners.game;

import de.feu.ps.bridges.gui.components.Puzzle;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.events.SettingsEvent;
import de.feu.ps.bridges.gui.listeners.PuzzleEventListener;
import de.feu.ps.bridges.gui.listeners.SettingsEventListener;
import de.feu.ps.bridges.gui.model.Model;

import java.util.Optional;
import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_CHANGED;
import static de.feu.ps.bridges.gui.events.SettingsEvent.SHOW_REMAINING_BRIDGES_CHANGED;

/**
 * Listener that reacts on {@link PuzzleEvent#PUZZLE_CHANGED}
 * and injects a new visual puzzle into a given client.
 *
 * @author Tim Gremplewski
 */
public class PuzzleSetter implements PuzzleEventListener, SettingsEventListener {

    private final Consumer<Puzzle> client;
    private final Model model;
    private boolean showRemainingBridges;

    /**
     * Creates a new instance.
     * @param client the client that will be given new visual puzzles.
     * @param model the model to use.
     */
    public PuzzleSetter(final Consumer<Puzzle> client, final Model model) {
        this.client = client;
        this.model = model;
    }

    @Override
    public void handleEvent(final PuzzleEvent event, final Object eventParameter) {
        if (event == PUZZLE_CHANGED) {
            Optional<de.feu.ps.bridges.model.Puzzle> optionalPuzzle = model.getGameState().getPuzzle();
            if (optionalPuzzle.isPresent()) {
                final Puzzle puzzle = new Puzzle(optionalPuzzle.get(), model);
                puzzle.setShowRemainingBridges(showRemainingBridges);
                client.accept(puzzle);
            }
        }
    }

    @Override
    public void handleEvent(final SettingsEvent event, final Object eventParameter) {
        if (event == SHOW_REMAINING_BRIDGES_CHANGED) {
            showRemainingBridges = (boolean) eventParameter;
        }
    }
}
