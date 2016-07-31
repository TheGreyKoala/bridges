package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.gui.events.GamePlayEvent;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;
import java.util.function.Function;

import static de.feu.ps.bridges.gui.events.GamePlayEvent.INVALID_MOVE;
import static javafx.scene.control.Alert.AlertType.WARNING;

/**
 * Listener that shows information dialogs when game play events occur.
 * @author Tim Gremplewski
 */
public class GamePlayAlert implements GamePlayEventListener {

    private final Function<Alert.AlertType, AlertWrapper> alertWrapperFactory;
    private final ResourceBundle resourceBundle;

    /**
     * Create a new instance that uses the given {@link ResourceBundle} to localize the dialog.
     * @param resourceBundle {@link ResourceBundle} that will be used to localize the dialog.
     */
    public GamePlayAlert(final ResourceBundle resourceBundle) {
        this(DefaultAlertWrapper::new, resourceBundle);
    }

    /**
     * This constructor is needed for test purposes only.
     * In tests we can not show an alert, so during tests we need to inject a dummy alert,
     * that does not work on a real alert.
     * @param alertWrapperFactory {@link Function} that creates a new {@link AlertWrapper}.
     * @param resourceBundle {@link ResourceBundle} that will be used to localize the dialog.
     */
    GamePlayAlert(final Function<Alert.AlertType, AlertWrapper> alertWrapperFactory, final ResourceBundle resourceBundle) {
        this.alertWrapperFactory = alertWrapperFactory;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void handleEvent(final GamePlayEvent event) {
        if (event == INVALID_MOVE) {
            final AlertWrapper alert = alertWrapperFactory.apply(WARNING);
            alert.setTitle(resourceBundle.getString("warning.title"));
            alert.setHeaderText(resourceBundle.getString("invalidMoveDialog.headerText"));
            alert.setContentText(resourceBundle.getString("invalidMoveDialog.contentText"));
            alert.showAndWait();
        }
    }
}
