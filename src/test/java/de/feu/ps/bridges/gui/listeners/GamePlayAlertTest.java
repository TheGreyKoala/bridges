package de.feu.ps.bridges.gui.listeners;

import org.junit.Test;

import java.util.ResourceBundle;

import static de.feu.ps.bridges.gui.events.GamePlayEvent.INVALID_MOVE;
import static javafx.scene.control.Alert.AlertType.WARNING;

/**
 * @author Tim Gremplewski
 */
public class GamePlayAlertTest extends AlertTest {

    @Test
    public void testInvalidMoveEvent() {
        final GamePlayAlert gamePlayAlert = new GamePlayAlert(DummyAlertWrapper::new, getResourceBundle());
        gamePlayAlert.handleEvent(INVALID_MOVE);

        final ResourceBundle resourceBundle = getResourceBundle();
        final String expectedTitle = resourceBundle.getString("warning.title");
        final String expectedHeaderText = resourceBundle.getString("invalidMoveDialog.headerText");
        final String expectedContentText = resourceBundle.getString("invalidMoveDialog.contentText");

        assertSingleAlert(WARNING, expectedTitle, expectedHeaderText, expectedContentText);
    }
}