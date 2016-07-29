package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.components.GraphicalPuzzle;
import de.feu.ps.bridges.gui.events.GameStateEvent;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.model.Puzzle;
import javafx.scene.Node;

import java.util.Optional;
import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.GameStateEvent.SHOW_REMAINING_BRIDGES_OPTION_CHANGED;
import static de.feu.ps.bridges.gui.events.PuzzleEvent.PUZZLE_CHANGED;

/**
 * @author Tim Gremplewski
 */
public class PuzzleRedraw implements PuzzleEventListener, GameStateEventListener {

    private final GameState gameState;
    private final Consumer<Optional<Node>> consumer;
    private boolean showRemainingBridges;

    public PuzzleRedraw(final GameState gameState, final Consumer<Optional<Node>> consumer) {
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
    public void handleEvent(final GameStateEvent event) {
    }

    @Override
    public void handleEvent(final GameStateEvent event, final Object eventParameter) {
        if (event == SHOW_REMAINING_BRIDGES_OPTION_CHANGED && eventParameter instanceof Boolean) {
            showRemainingBridges = (Boolean) eventParameter;
            redrawPuzzle();
        }
    }

    private void redrawPuzzle() {
        Optional<Puzzle> puzzle = gameState.getPuzzle();
        if (puzzle.isPresent()) {
            Node puzzleNode = GraphicalPuzzle.createPuzzle(puzzle.get(), gameState, showRemainingBridges);
            consumer.accept(Optional.ofNullable(puzzleNode));
        }
    }
}
