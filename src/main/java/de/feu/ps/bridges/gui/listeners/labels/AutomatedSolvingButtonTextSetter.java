package de.feu.ps.bridges.gui.listeners.labels;

import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.events.ErrorEvent;
import de.feu.ps.bridges.gui.listeners.AutomatedSolvingEventListener;
import de.feu.ps.bridges.gui.listeners.ErrorEventListener;

import java.util.ResourceBundle;
import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.STARTED;

/**
 * Listener that tells a given client which text to display on a button that starts and stops the automated solving.
 * @author Tim Gremplewski
 */
public class AutomatedSolvingButtonTextSetter implements AutomatedSolvingEventListener, ErrorEventListener {

    private final Consumer<String> buttonTextConsumer;
    private final ResourceBundle resourceBundle;

    /**
     * Creates a new instance.
     * @param buttonTextConsumer {@link Consumer} that will be told which text to display.
     * @param resourceBundle {@link ResourceBundle} to localize the text.
     */
    public AutomatedSolvingButtonTextSetter(final Consumer<String> buttonTextConsumer, final ResourceBundle resourceBundle) {
        this.buttonTextConsumer = buttonTextConsumer;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void handleEvent(final AutomatedSolvingEvent event) {
        buttonTextConsumer.accept(getButtonText(event));
    }

    @Override
    public void handleEvent(final ErrorEvent event) {
        buttonTextConsumer.accept(getButtonText(event));
    }

    private String getButtonText(final Object event) {
        if (event == STARTED) {
            return resourceBundle.getString("controls.solve.stop");
        } else {
            return resourceBundle.getString("controls.solve.start");
        }
    }
}
