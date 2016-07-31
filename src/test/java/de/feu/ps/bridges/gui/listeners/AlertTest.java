package de.feu.ps.bridges.gui.listeners;

import javafx.scene.control.Alert;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Tim Gremplewski
 */
abstract class AlertTest {

    private List<DummyAlertWrapper> alerts;
    private ResourceBundle resourceBundle;

    @Before
    public void setUp() throws Exception {
        alerts = new LinkedList<>();
        resourceBundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");
    }

    List<DummyAlertWrapper> getAlerts() {
        return alerts;
    }

    ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    class DummyAlertWrapper implements AlertWrapper {
        private final Alert.AlertType alertType;
        private String title;
        private String headerText;
        private String contentText;

        DummyAlertWrapper(final Alert.AlertType alertType) {
            this.alertType = alertType;
            alerts.add(this);
        }

        @Override
        public Alert.AlertType getAlertType() {
            return alertType;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setTitle(final String title) {
            this.title = title;
        }

        @Override
        public String getHeaderText() {
            return headerText;
        }

        @Override
        public void setHeaderText(final String headerText) {
            this.headerText = headerText;
        }

        @Override
        public String getContentText() {
            return contentText;
        }

        @Override
        public void setContentText(final String contentText) {
            this.contentText = contentText;
        }

        @Override
        public void showAndWait() {
        }
    }
}
