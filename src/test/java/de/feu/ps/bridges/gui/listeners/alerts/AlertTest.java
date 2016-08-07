package de.feu.ps.bridges.gui.listeners.alerts;

import javafx.scene.control.Alert;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;

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

    void assertSingleAlert(final Alert.AlertType alertType, final String title, final String headerText, final String contentText) {
        final List<DummyAlertWrapper> alerts = getAlerts();
        assertEquals("Unexpected number of alerts.", 1, alerts.size());

        final DummyAlertWrapper alert = alerts.get(0);
        assertEquals("Unexpected alert type.", alertType, alert.getAlertType());
        assertEquals("Unexpected title.", title, alert.getTitle());
        assertEquals("Unexpected header text.", headerText, alert.getHeaderText());
        assertEquals("Unexpected content text.", contentText, alert.getContentText());
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
