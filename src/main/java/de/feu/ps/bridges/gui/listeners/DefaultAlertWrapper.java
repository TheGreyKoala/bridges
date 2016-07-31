package de.feu.ps.bridges.gui.listeners;

import javafx.scene.control.Alert;

/**
 * Default implementation of {@link AlertWrapper}.
 * This class delegates all calls to an actual {@link Alert}
 * and is meant for use during runtime.
 * @author Tim Gremplewski
 */
class DefaultAlertWrapper implements AlertWrapper {

    private final Alert alert;

    /**
     * Creates a new instance using the given alert type.
     * @param alertType the alert type to be used.
     */
    DefaultAlertWrapper(final Alert.AlertType alertType) {
        alert = new Alert(alertType);
    }

    @Override
    public Alert.AlertType getAlertType() {
        return alert.getAlertType();
    }

    @Override
    public String getTitle() {
        return alert.getTitle();
    }

    @Override
    public void setTitle(final String title) {
        alert.setTitle(title);
    }

    @Override
    public String getHeaderText() {
        return alert.getHeaderText();
    }

    @Override
    public void setHeaderText(final String headerText) {
        alert.setHeaderText(headerText);
    }

    @Override
    public String getContentText() {
        return alert.getContentText();
    }

    @Override
    public void setContentText(final String contentText) {
        alert.setContentText(contentText);
    }

    @Override
    public void showAndWait() {
        alert.showAndWait();
    }
}
