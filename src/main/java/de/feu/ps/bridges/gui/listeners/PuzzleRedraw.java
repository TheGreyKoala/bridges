package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.GameOptionsEvent;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.model.GameState;
import de.feu.ps.bridges.model.Puzzle;

import java.util.Optional;
import java.util.function.BiConsumer;

import static de.feu.ps.bridges.gui.events.GameOptionsEvent.SHOW_REMAINING_BRIDGES_OPTION_CHANGED;
import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_CHANGED;

/**
 * @author Tim Gremplewski
 */
public class PuzzleRedraw implements PuzzleEventListener, GameOptionsEventListener {

    private final GameState gameState;
    private final BiConsumer<Puzzle, Boolean> consumer;
    private boolean showRemainingBridges;

    public PuzzleRedraw(final GameState gameState, final BiConsumer<Puzzle, Boolean> consumer) {
        this.gameState = gameState;
        this.consumer = consumer;
        this.showRemainingBridges = false;
    }

    @Override
    public void handleEvent(final PuzzleEvent event) {
        if (event == PUZZLE_CHANGED) {
            redrawPuzzle();
        }
    }

    @Override
    public void handleEvent(final GameOptionsEvent event) {
    }

    @Override
    public void handleEvent(final GameOptionsEvent event, final Object eventParameter) {
        if (event == SHOW_REMAINING_BRIDGES_OPTION_CHANGED && eventParameter instanceof Boolean) {
            showRemainingBridges = (Boolean) eventParameter;
            redrawPuzzle();
        }
    }

    private void redrawPuzzle() {
        Optional<Puzzle> puzzle = gameState.getPuzzle();
        if (puzzle.isPresent()) {
            consumer.accept(puzzle.get(), showRemainingBridges);
        }
    }
}
