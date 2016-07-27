package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.gui.gamestate.GameStateEvent;
import de.feu.ps.bridges.gui.gamestate.GameStateEventType;
import de.feu.ps.bridges.gui.gamestate.GameStateListener;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

/**
 * @author Tim Gremplewski
 */
public class StatusBarHelper implements GameStateListener {

    private final ResourceBundle resourceBundle;
    private final Label statusLabel;
    private final GameState gameState;

    public StatusBarHelper(final ResourceBundle resourceBundle, final Label statusLabel, final GameState gameState) {
        this.resourceBundle = resourceBundle;
        this.statusLabel = statusLabel;
        this.gameState = gameState;
        // TODO: Create a new Listener that only listens on PuzzleStatus changes.
        // That way we get rid of the dependency to GameState
    }

    @Override
    public void handleGameStateEvent(final GameStateEvent event) {
        // TODO: Fire event in GameState
        if (event.getGameStateEventType() == GameStateEventType.PUZZLE_STATUS_CHANGED) {
            updateStatusBar(gameState.getPuzzleStatus());
        }
    }

    private void updateStatusBar(final PuzzleStatus puzzleStatus) {
        switch (puzzleStatus) {
            case SOLVED:
                statusLabel.setText(resourceBundle.getString("puzzle.status.solved"));
                break;
            case UNSOLVED:
                statusLabel.setText(resourceBundle.getString("puzzle.status.unsolved"));
                break;
            case UNSOLVABLE:
                statusLabel.setText(resourceBundle.getString("puzzle.status.unsolvable"));
                break;
        }
    }
}
