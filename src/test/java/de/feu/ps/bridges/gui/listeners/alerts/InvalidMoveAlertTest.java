package de.feu.ps.bridges.gui.listeners.alerts;

import org.junit.Test;

import java.util.ResourceBundle;

import static de.feu.ps.bridges.gui.events.PuzzleEvent.INVALID_MOVE;
import static javafx.scene.control.Alert.AlertType.WARNING;

/**
 * @author Tim Gremplewski
 */
public class InvalidMoveAlertTest extends AlertTest {

    @Test
    public void testInvalidMoveEvent() {
        final InvalidMoveAlert invalidMoveAlert = new InvalidMoveAlert(DummyAlertWrapper::new, getResourceBundle());
        invalidMoveAlert.handleEvent(INVALID_MOVE, null);

        final ResourceBundle resourceBundle = getResourceBundle();
        final String expectedTitle = resourceBundle.getString("warning.title");
        final String expectedHeaderText = resourceBundle.getString("invalidMoveDialog.headerText");
        final String expectedContentText = resourceBundle.getString("invalidMoveDialog.contentText");

        assertSingleAlert(WARNING, expectedTitle, expectedHeaderText, expectedContentText);
    }
}