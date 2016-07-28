package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.GameStateEvent;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;

import static de.feu.ps.bridges.gui.events.GameStateEvent.INVALID_MOVE;

/**
 * @author Tim Gremplewski
 */
public class EventAlert implements GameStateEventListener {

    private final ResourceBundle resourceBundle;

    public EventAlert(final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void handleEvent(final GameStateEvent event) {
        if (event == INVALID_MOVE) {
            final Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resourceBundle.getString("warning.title"));
            alert.setHeaderText(resourceBundle.getString("invalidMoveDialog.headerText"));
            alert.setContentText(resourceBundle.getString("invalidMoveDialog.contentText"));
            alert.showAndWait();
        }
    }

    @Override
    public void handleEvent(final GameStateEvent event, final Object eventParameter) {
    }
}
