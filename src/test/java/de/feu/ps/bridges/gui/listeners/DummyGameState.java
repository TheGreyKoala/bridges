package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.model.GameState;

/**
 * @author Tim Gremplewski
 */
class DummyGameState extends GameState {
    private final PuzzleStatus puzzleStatus;

    DummyGameState(final PuzzleStatus puzzleStatus) {
        this.puzzleStatus = puzzleStatus;
    }

    @Override
    public PuzzleStatus getPuzzleStatus() {
        return puzzleStatus;
    }
}
