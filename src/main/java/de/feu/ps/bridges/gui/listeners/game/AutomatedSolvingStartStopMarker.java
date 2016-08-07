package de.feu.ps.bridges.gui.listeners.game;

import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.events.ErrorEvent;
import de.feu.ps.bridges.gui.listeners.AutomatedSolvingEventListener;
import de.feu.ps.bridges.gui.listeners.ErrorEventListener;

import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.STARTED;

/**
 * Listener that notifies a given client when the automated solving of the puzzle has started or ended.
 * @author Tim Gremplewski
 */
public class AutomatedSolvingStartStopMarker implements AutomatedSolvingEventListener, ErrorEventListener {

    private final Consumer<Boolean> consumer;

    /**
     * Create a new instance that informs the given {@link Consumer},
     * when the complete automated solving of a puzzle has been started or ended.
     * When the solving has been started, the consumer's parameter will be true,
     * otherwise false.
     *
     * @param consumer the {@link Consumer} to inform.
     */
    public AutomatedSolvingStartStopMarker(final Consumer<Boolean> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void handleEvent(final AutomatedSolvingEvent event) {
        consumer.accept(event == STARTED);
    }

    @Override
    public void handleEvent(final ErrorEvent event) {
        consumer.accept(false);
    }
}
