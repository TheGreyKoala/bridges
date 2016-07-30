package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.model.Bridge;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.*;
import static de.feu.ps.bridges.gui.events.ErrorEvent.APPLYING_NEXT_MOVE_FAILED;
import static de.feu.ps.bridges.gui.events.ErrorEvent.SOLVING_FAILED;

/**
 * @author Tim Gremplewski
 */
class AutomatedSolving {

    private static final Logger LOGGER = Logger.getLogger(AutomatedSolving.class.getName());

    private final ExecutorService executorService;
    private final GameState gameState;
    private Future<?> currentTask;

    AutomatedSolving(final GameState gameState) {
        this.gameState = gameState;
        executorService = Executors.newWorkStealingPool();
    }

    /**
     * Perform a next save move on the current puzzle, if any can be found.
     */
    public void nextMove() {
        if (gameState.getPuzzle().isPresent()) {
            try {
                Optional<Bridge> optionalBridge = gameState.getPuzzleToolkit().nextMove();
                if (optionalBridge.isPresent()) {
                    gameState.addBridge(optionalBridge.get());
                } else {
                    gameState.broadcastEvent(NO_NEXT_MOVE);
                }
            } catch (final Exception e) {
                LOGGER.log(Level.SEVERE, "Unexpected error while applying a next safe move.", e);
                gameState.broadcastEvent(APPLYING_NEXT_MOVE_FAILED);
            }
        }
    }

    /**
     * Solve the current puzzle as far as possible.
     */
    public void solve() {
        if (gameState.getPuzzle().isPresent()) {
            try {
                if (currentTask == null || currentTask.isDone()) {
                    gameState.broadcastEvent(STARTED);
                    currentTask = executorService.submit(this::solvePuzzle);
                } else {
                    currentTask.cancel(true);
                    gameState.broadcastEvent(CANCELLED_BY_USER);
                }
            } catch (final Exception e) {
                LOGGER.log(Level.SEVERE, "Unexpected error while solving a puzzle.", e);
                gameState.broadcastEvent(SOLVING_FAILED);
            }
        }
    }

    private void solvePuzzle() {
        try {
            Optional<Bridge> optionalBridge;
            do {
                optionalBridge = gameState.getPuzzleToolkit().nextMove();

                if (optionalBridge.isPresent()) {
                    gameState.addBridge(optionalBridge.get());

                    if (gameState.getPuzzleStatus() == PuzzleStatus.UNSOLVED && !currentTask.isCancelled()) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            currentTask.cancel(true);
                        }
                    }
                }
            } while (optionalBridge.isPresent() && !currentTask.isCancelled());

            if (!currentTask.isCancelled()) {
                gameState.broadcastEvent(AutomatedSolvingEvent.FINISHED);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while solving a puzzle.", e);
            gameState.broadcastEvent(SOLVING_FAILED);
        }
    }
}
