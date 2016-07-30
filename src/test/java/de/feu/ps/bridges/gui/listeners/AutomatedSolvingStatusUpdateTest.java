package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.events.ErrorEvent;
import org.junit.Assert;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.function.Consumer;

import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.STARTED;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class AutomatedSolvingStatusUpdateTest {

    @DataPoints("automatedSolvingEvents")
    public static AutomatedSolvingEvent[] automatedSolvingEvent = AutomatedSolvingEvent.values();

    @DataPoints("errorEvents")
    public static ErrorEvent[] errorEvents = ErrorEvent.values();

    @Theory
    public void testHandleAutomatedSolvingEvent(@FromDataPoints("automatedSolvingEvent") final AutomatedSolvingEvent event) throws Exception {
        Consumer<Boolean> consumer = event == STARTED ? Assert::assertTrue : Assert::assertFalse;
        AutomatedSolvingStatusUpdate automatedSolvingStatusUpdate = new AutomatedSolvingStatusUpdate(consumer);
        automatedSolvingStatusUpdate.handleEvent(event);
    }

    @Theory
    public void testHandleErrorEvent(@FromDataPoints("errorEvents") final ErrorEvent errorEvent) {
        Consumer<Boolean> consumer = Assert::assertFalse;
        AutomatedSolvingStatusUpdate automatedSolvingStatusUpdate = new AutomatedSolvingStatusUpdate(consumer);
        automatedSolvingStatusUpdate.handleEvent(errorEvent);
    }
}