package de.feu.ps.bridges.gui.listeners.alerts;

import de.feu.ps.bridges.gui.events.ErrorEvent;
import de.feu.ps.bridges.gui.listeners.ErrorEventListener;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;
import java.util.function.Function;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Listener that shows an error dialog whenever an error event occurs.
 * @author Tim Gremplewski
 */
public class ErrorAlert implements ErrorEventListener {

    private final Function<Alert.AlertType, AlertWrapper> alertWrapperFactory;
    private final ResourceBundle resourceBundle;

    /**
     * Create a new instance that uses the given {@link ResourceBundle} to localize the error dialog.
     * @param resourceBundle {@link ResourceBundle} that will be used to localize the error dialog.
     */
    public ErrorAlert(final ResourceBundle resourceBundle) {
        this(DefaultAlertWrapper::new, resourceBundle);
    }

    /**
     * This constructor is needed for test purposes only.
     * In tests we can not show an alert, so during tests we need to inject a dummy alert,
     * that does not work on a real alert.
     * @param alertWrapperFactory {@link Function} that creates a new {@link AlertWrapper}.
     * @param resourceBundle {@link ResourceBundle} that will be used to localize the dialog.
     */
    ErrorAlert(final Function<Alert.AlertType, AlertWrapper> alertWrapperFactory, final ResourceBundle resourceBundle) {
        this.alertWrapperFactory = alertWrapperFactory;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void handleEvent(final ErrorEvent event) {
        getErrorAlert(event)
            .showAndWait();
    }

    private AlertWrapper getErrorAlert(final ErrorEvent errorEventType) {
        switch (errorEventType) {
            case GENERATING_PUZZLE_FAILED:
                return getErrorAlert(resourceBundle.getString("generation.failed"));
            case LOADING_PUZZLE_FAILED:
                return getErrorAlert(resourceBundle.getString("loading.failed"));
            case SAVING_PUZZLE_FAILED:
                return getErrorAlert(resourceBundle.getString("saving.failed"));
            case RESTARTING_PUZZLE_FAILED:
                return getErrorAlert(resourceBundle.getString("restarting.failed"));
            case APPLYING_NEXT_MOVE_FAILED:
                return getErrorAlert(resourceBundle.getString("nextMove.failed"));
            case SOLVING_FAILED:
                return getErrorAlert(resourceBundle.getString("solving.failed"));
            case BUILDING_BRIDGE_FAILED:
                return getErrorAlert(resourceBundle.getString("buildBridge.failed"));
            case TEARING_DOWN_BRIDGE_FAILED:
                return getErrorAlert(resourceBundle.getString("tearDownBridge.failed"));
            default:
                return getErrorAlert("");
        }
    }

    private AlertWrapper getErrorAlert(final String headerText) {
        final AlertWrapper alert = alertWrapperFactory.apply(ERROR);
        alert.setTitle(resourceBundle.getString("error.title"));
        alert.setHeaderText(headerText);
        alert.setContentText(resourceBundle.getString("error.content.text"));
        return alert;
    }
}
