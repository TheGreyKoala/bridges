package de.feu.ps.bridges.gui.model;

import de.feu.ps.bridges.gui.events.*;
import de.feu.ps.bridges.gui.listeners.*;
import javafx.application.Platform;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class that broadcasts events to registered listeners.
 * @author Tim Gremplewski
 */
class EventBroadcaster {

    private final Set<AutomatedSolvingEventListener> automatedSolvingEventListeners;
    private final Set<ErrorEventListener> errorEventListeners;
    private final Set<PuzzleEventListener> puzzleEventListeners;
    private final Set<SettingsEventListener> settingsEventListeners;

    /**
     * Creates a new instance.
     */
    EventBroadcaster() {
        automatedSolvingEventListeners = new LinkedHashSet<>();
        errorEventListeners = new LinkedHashSet<>();
        puzzleEventListeners = new LinkedHashSet<>();
        settingsEventListeners = new LinkedHashSet<>();
    }

    /**
     * Register a new {@link AutomatedSolvingEventListener}.
     * @param listener listener to register.
     */
    public void addAutomatedSolvingEventListener(final AutomatedSolvingEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        automatedSolvingEventListeners.add(listener);
    }

    /**
     * Register a new {@link ErrorEventListener}.
     * @param listener listener to register.
     */
    public void addErrorEventListener(final ErrorEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        errorEventListeners.add(listener);
    }

    /**
     * Register a new {@link PuzzleEventListener}.
     * @param listener listener to register.
     */
    public void addPuzzleEventListener(final PuzzleEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        puzzleEventListeners.add(listener);
    }

    /**
     * Register a new {@link SettingsEventListener}.
     * @param listener listener to register.
     */
    public void addSettingsEventListener(final SettingsEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        settingsEventListeners.add(listener);
    }

    /**
     * Broadcast the given {@link AutomatedSolvingEvent}.
     * @param event event to broadcast.
     */
    void broadcastEvent(final AutomatedSolvingEvent event) {
        Platform.runLater(() -> automatedSolvingEventListeners.forEach(listener -> listener.handleEvent(event)));
    }

    /**
     * Broadcast the given {@link ErrorEvent}.
     * @param event event to broadcast.
     */
    void broadcastEvent(final ErrorEvent event) {
        Platform.runLater(() -> errorEventListeners.forEach(listener -> listener.handleEvent(event)));
    }

    /**
     * Broadcast the given {@link PuzzleEvent}.
     * @param event event to broadcast.
     * @param eventParameter Additional parameter that will be passed with the event.
     */
    void broadcastEvent(final PuzzleEvent event, Object eventParameter) {
        Platform.runLater(() -> puzzleEventListeners.forEach(listener -> listener.handleEvent(event, eventParameter)));
    }

    /**
     * Broadcast the given {@link SettingsEvent}.
     * @param event event to broadcast.
     * @param eventParameter Additional parameter that will be passed with the event.
     */
    public void broadcastEvent(final SettingsEvent event, final Object eventParameter) {
        Platform.runLater(() -> settingsEventListeners.forEach(listener -> listener.handleEvent(event, eventParameter)));
    }
}
