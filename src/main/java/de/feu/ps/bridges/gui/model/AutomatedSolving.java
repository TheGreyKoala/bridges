package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.model.Bridge;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.*;
import static de.feu.ps.bridges.gui.events.ErrorEvent.APPLYING_NEXT_MOVE_FAILED;
import static de.feu.ps.bridges.gui.events.ErrorEvent.SOLVING_FAILED;

/**
 * Helper class that offers operations to automatically solve the puzzle of a given {@link GameState}.
 * @author Tim Gremplewski
 */
class AutomatedSolving {

    private static final Logger LOGGER = Logger.getLogger(AutomatedSolving.class.getName());
    private final ExecutorService executorService;
    private final GameState gameState;
    private final Set<UUID> cancelledTasks;
    private final ReentrantLock reentrantLock;
    private Future<?> currentTask;
    private UUID currentTaskId;

    /**
     * Creates a new instance that operates on the given {@link GameState}.
     * @param gameState {@link GameState} to operate on.
     */
    AutomatedSolving(final GameState gameState) {
        this.gameState = gameState;
        cancelledTasks = new HashSet<>();
        executorService = Executors.newWorkStealingPool();
        reentrantLock = new ReentrantLock();
    }

    /**
     * Apply a next save move on the puzzle, if any can be found.
     */
    void applyNextMove() {
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
     * Solve the puzzle as far as possible.
     */
    synchronized void solve() {
        /*
         * 1. User starts solving
         * 2. User cancels solving while thread sleeps (currentTask.isCancelled = true)
         * 3. User restarts solving while the thread is still sleeping
         * 4. A 2nd thread will be started and currentTask will be overridden (currentTask.isCancelled = false)
         * 5. If the 1st thread uses currentTask.isCancelled, it won't notice it has been cancelled
         *
         * Therefore each thread gets a unique id, that will be "blacklisted", when a task was cancelled.
         * So using unique ids we make sure that the thread knows when it was cancelled (also see solvePuzzle).
         * Management of this ids is a critical section. Therefore this method is synchronized.
         */

        if (gameState.getPuzzle().isPresent()) {
            try {
                if (currentTask == null || currentTask.isDone()) {
                    gameState.broadcastEvent(STARTED);
                    currentTaskId = UUID.randomUUID();
                    currentTask = executorService.submit(() -> solvePuzzle(currentTaskId));
                } else {
                    cancelledTasks.add(currentTaskId);
                    currentTask.cancel(true);
                    gameState.broadcastEvent(CANCELLED_BY_USER);
                }
            } catch (final Exception e) {
                LOGGER.log(Level.SEVERE, "Unexpected error while solving a puzzle.", e);
                gameState.broadcastEvent(SOLVING_FAILED);
            }
        }
    }

    private void solvePuzzle(final UUID taskId) {
        /*
         * Theoretically it would be possible, that a 2nd thread starts solving the puzzle
         * in the time frame between the 1st thread's last cancel check and the 1st thread's next attempt
         * to add a bridge (between while and addBridge).
         * In this case, both threads would add a bridge and the user would not see a break between these bridges.
         *
         * Therefore this method uses a reentrant lock. That way only one thread at a time is able to solve the puzzle.
         * So using the reentrant lock, we make sure that only one thread at a time solves the puzzle.
         */

        try {
            reentrantLock.lock();

            /*
             * A blocked thread may be cancelled before it could acquire the lock.
             * In this case, it must not start solving the puzzle but just quit.
             * Therefore check if the task has been cancelled.
             */
            if (!cancelledTasks.contains(taskId)) {
                Optional<Bridge> optionalBridge;
                do {
                    optionalBridge = gameState.getPuzzleToolkit().nextMove();
                    if (optionalBridge.isPresent()) {
                        gameState.addBridge(optionalBridge.get());

                        /*
                         * We do not check if the task was cancelled,
                         * because a new thread may already wait to start solving the puzzle.
                         * In this case, the 1st thread would add its last bridge and quit.
                         * The 2nd thread would immediately add its first bridge.
                         * For the user there would not be a break between the added bridges.
                         * Therefore we always sleep when a new bridge was added.
                         */
                        if (gameState.getPuzzleStatus() == PuzzleStatus.UNSOLVED) {
                            Thread.sleep(5000);
                        }
                    } else {
                        gameState.broadcastEvent(FINISHED);
                    }
                } while (optionalBridge.isPresent() && !cancelledTasks.contains(taskId));
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while solving a puzzle.", e);
            gameState.broadcastEvent(SOLVING_FAILED);
        } finally {
            reentrantLock.unlock();
        }
    }
}
