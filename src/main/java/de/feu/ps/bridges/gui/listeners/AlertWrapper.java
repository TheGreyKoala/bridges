package de.feu.ps.bridges.gui.listeners;

import javafx.scene.control.Alert;

/**
 * This interface defines a wrapper for {@link Alert}.
 * It is not possible to instantiate {@link Alert} during tests.
 * Neither it is possible to override {@link Alert#showAndWait()}.
 *
 * Therefore during tests we create a dummy wrapper,
 * that does not work on an {@link Alert}.
 *
 * On the other hand, during runtime we use an implementation,
 * that delegates all calls to an internally instantiated {@link Alert}.
 *
 * That way all classes that use this interface do not have to be adjusted for tests.
 *
 * @author Tim Gremplewski
 */
interface AlertWrapper {

    /**
     * Get the alert type.
     * @return the alert type.
     */
    Alert.AlertType getAlertType();

    /**
     * Get the alert title.
     * @return the alert title.
     */
    String getTitle();

    /**
     * set the alert title.
     * @param title the alert title.
     */
    void setTitle(String title);

    /**
     * Get the alert header text.
     * @return the alert header text.
     */
    String getHeaderText();

    /**
     * Set the alert header text.
     * @param headerText the alert header text.
     */
    void setHeaderText(String headerText);

    /**
     * Get the alert content text.
     * @return the alert content text.
     */
    String getContentText();

    /**
     * Set the alert content text.
     * @param contentText the alert content text.
     */
    void setContentText(String contentText);

    /**
     * Show the alert and wait.
     */
    void showAndWait();
}
