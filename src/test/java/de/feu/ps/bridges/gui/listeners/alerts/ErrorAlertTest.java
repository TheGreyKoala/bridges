package de.feu.ps.bridges.gui.listeners.alerts;

import de.feu.ps.bridges.gui.events.ErrorEvent;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * @author Tim Gremplewski
 */
@RunWith(Theories.class)
public class ErrorAlertTest extends AlertTest {

    @DataPoints("errorEvents")
    public static ErrorEvent[] errorEvents = ErrorEvent.values();

    @Theory
    public void testHandleErrorEvent(@FromDataPoints("errorEvents") final ErrorEvent event) {
        final ErrorAlert errorAlert = new ErrorAlert(DummyAlertWrapper::new, getResourceBundle());
        errorAlert.handleEvent(event);
        assertSingleAlert(event);
    }

    private void assertSingleAlert(final ErrorEvent event) {
        final ResourceBundle resourceBundle = getResourceBundle();
        final String expectedTitle = resourceBundle.getString("error.title");
        final String expectedHeaderText = getExpectedHeaderText(event);
        final String expectedContentText = resourceBundle.getString("error.content.text");
        assertSingleAlert(ERROR, expectedTitle, expectedHeaderText, expectedContentText);
    }

    private String getExpectedHeaderText(final ErrorEvent event) {
        final ResourceBundle resourceBundle = getResourceBundle();
        switch (event) {
            case GENERATING_PUZZLE_FAILED:
                return resourceBundle.getString("generation.failed");
            case LOADING_PUZZLE_FAILED:
                return resourceBundle.getString("loading.failed");
            case SAVING_PUZZLE_FAILED:
                return resourceBundle.getString("saving.failed");
            case RESTARTING_PUZZLE_FAILED:
                return resourceBundle.getString("restarting.failed");
            case APPLYING_NEXT_MOVE_FAILED:
                return resourceBundle.getString("nextMove.failed");
            case SOLVING_FAILED:
                return resourceBundle.getString("solving.failed");
            case BUILDING_BRIDGE_FAILED:
                return resourceBundle.getString("buildBridge.failed");
            case TEARING_DOWN_BRIDGE_FAILED:
                return resourceBundle.getString("tearDownBridge.failed");
            default:
                return "";
        }
    }
}