package de.feu.ps.bridges.gui.gamestate;

import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.events.ErrorEvent;
import de.feu.ps.bridges.gui.events.GameStateEvent;
import de.feu.ps.bridges.gui.events.PuzzleEvent;
import de.feu.ps.bridges.gui.listeners.AutomatedSolvingEventListener;
import de.feu.ps.bridges.gui.listeners.ErrorEventListener;
import de.feu.ps.bridges.gui.listeners.GameStateEventListener;
import de.feu.ps.bridges.gui.listeners.PuzzleEventListener;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class EventBroadcaster {

    private final Set<AutomatedSolvingEventListener> automatedSolvingEventListeners;
    private final Set<ErrorEventListener> errorEventListeners;
    private final Set<GameStateEventListener> gameStateEventListeners;
    private final Set<PuzzleEventListener> puzzleEventListeners;

    public EventBroadcaster() {
        automatedSolvingEventListeners = new LinkedHashSet<>();
        errorEventListeners = new LinkedHashSet<>();
        gameStateEventListeners = new LinkedHashSet<>();
        puzzleEventListeners = new LinkedHashSet<>();
    }

    public void addAutomatedSolvingEventListener(final AutomatedSolvingEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        automatedSolvingEventListeners.add(listener);
    }

    public void addErrorEventListener(final ErrorEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        errorEventListeners.add(listener);
    }

    public void addGameStateEventListener(final GameStateEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        gameStateEventListeners.add(listener);
    }

    public void addPuzzleEventListener(final PuzzleEventListener listener) {
        Objects.requireNonNull(listener, "Parameter 'listener' must not be null.");
        puzzleEventListeners.add(listener);
    }

    public void broadcastEvent(final AutomatedSolvingEvent event) {
        automatedSolvingEventListeners.forEach(listener -> listener.handleEvent(event));
    }

    public void broadcastEvent(final ErrorEvent event) {
        errorEventListeners.forEach(listener -> listener.handleEvent(event));
    }

    public void broadcastEvent(final GameStateEvent event) {
        gameStateEventListeners.forEach(listener -> listener.handleEvent(event));
    }

    public void broadcastEvent(final GameStateEvent event, final Object eventParameter) {
        gameStateEventListeners.forEach(listener -> listener.handleEvent(event, eventParameter));
    }

    public void broadcastEvent(final PuzzleEvent event) {
        puzzleEventListeners.forEach(listener -> listener.handleEvent(event));
    }
}
