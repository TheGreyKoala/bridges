package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.gui.events.*;
import de.feu.ps.bridges.gui.listeners.*;
import javafx.application.Platform;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
class EventBroadcaster {

    private final Set<AutomatedSolvingEventListener> automatedSolvingEventListeners;
    private final Set<ErrorEventListener> errorEventListeners;
    private final Set<GameOptionsEventListener> gameOptionsEventListeners;
    private final Set<PuzzleEventListener> puzzleEventListeners;
    private final Set<GamePlayEventListener> gamePlayEventListeners;

    EventBroadcaster() {
        automatedSolvingEventListeners = new LinkedHashSet<>();
        errorEventListeners = new LinkedHashSet<>();
        gameOptionsEventListeners = new LinkedHashSet<>();
        puzzleEventListeners = new LinkedHashSet<>();
        gamePlayEventListeners = new LinkedHashSet<>();
    }

    public void addAutomatedSolvingEventListener(final AutomatedSolvingEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        automatedSolvingEventListeners.add(listener);
    }

    public void addErrorEventListener(final ErrorEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        errorEventListeners.add(listener);
    }

    public void addGameOptionsEventListener(final GameOptionsEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        gameOptionsEventListeners.add(listener);
    }

    public void addGamePlayEventListener(final GamePlayEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        gamePlayEventListeners.add(listener);
    }

    public void addPuzzleEventListener(final PuzzleEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        puzzleEventListeners.add(listener);
    }

    void broadcastEvent(final AutomatedSolvingEvent event) {
        Platform.runLater(() -> {
            automatedSolvingEventListeners.forEach(listener -> listener.handleEvent(event));
        });
    }

    void broadcastEvent(final ErrorEvent event) {
        Platform.runLater(() -> {
            errorEventListeners.forEach(listener -> listener.handleEvent(event));
        });
    }

    void broadcastEvent(final GamePlayEvent event) {
        Platform.runLater(() -> {
            gamePlayEventListeners.forEach(listener -> listener.handleEvent(event));
        });
    }

    void broadcastEvent(final GameOptionsEvent event, final Object eventParameter) {
        Platform.runLater(() -> {
            gameOptionsEventListeners.forEach(listener -> listener.handleEvent(event, eventParameter));
        });
    }

    void broadcastEvent(final PuzzleEvent event) {
        Platform.runLater(() -> {
            puzzleEventListeners.forEach(listener -> listener.handleEvent(event));
        });
    }
}
