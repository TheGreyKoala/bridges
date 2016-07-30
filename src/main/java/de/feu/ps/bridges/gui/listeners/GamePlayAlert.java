package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.GamePlayEvent;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;

import static de.feu.ps.bridges.gui.events.GamePlayEvent.INVALID_MOVE;

/**
 * @author Tim Gremplewski
 */
public class GamePlayAlert implements GamePlayEventListener {

    private final ResourceBundle resourceBundle;

    public GamePlayAlert(final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void handleEvent(final GamePlayEvent event) {
        if (event == INVALID_MOVE) {
            final Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resourceBundle.getString("warning.title"));
            alert.setHeaderText(resourceBundle.getString("invalidMoveDialog.headerText"));
            alert.setContentText(resourceBundle.getString("invalidMoveDialog.contentText"));
            alert.showAndWait();
        }
    }
}
