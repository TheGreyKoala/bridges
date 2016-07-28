package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.events.ErrorEvent;

import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.CANCELLED_BY_USER;
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
        if (event == AutomatedSolvingEvent.FINISHED || event == CANCELLED_BY_USER) {
            consumer.accept(false);
        } else if (event == STARTED) {
            consumer.accept(true);
        }
    }

    @Override
    public void handleEvent(final ErrorEvent event) {
        consumer.accept(false);
    }
}
