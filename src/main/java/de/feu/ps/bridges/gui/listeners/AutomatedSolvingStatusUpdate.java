package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.events.ErrorEvent;

import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.STARTED;

/**
 * @author Tim Gremplewski
 */
public class AutomatedSolvingStatusUpdate implements AutomatedSolvingEventListener, ErrorEventListener {

    private final Consumer<Boolean> consumer;

    public AutomatedSolvingStatusUpdate(final Consumer<Boolean> consumer) {
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
