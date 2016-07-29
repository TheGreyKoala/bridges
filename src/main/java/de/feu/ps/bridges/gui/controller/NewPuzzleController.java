package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.gui.model.GameState;
import de.feu.ps.bridges.gui.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the new puzzle dialog.
 * @author Tim Gremplewski
 */
public class NewPuzzleController implements Initializable {

    @FXML
    private Spinner<Integer> columnsSpinner;

    @FXML
    private Spinner<Integer> rowsSpinner;

    @FXML
    private Spinner<Integer> islandsSpinner;

    @FXML
    private RadioButton autoGenerateRadioButton;

    @FXML
    private GridPane manualSettingsGridPane;

    @FXML
    private CheckBox manualIslandsCountCheckBox;

    private final Model model;
    private final Stage stage;

    /**
     * Creates a new instance.
     * @param model the {@link GameState} to use.
     * @param stage the {@link Stage} to use.
     * @throws NullPointerException if model or stage is null.
     */
    NewPuzzleController(final Model model, final Stage stage) {
        this.model = Objects.requireNonNull(model, "Parameter 'model' must not be null.");
        this.stage = Objects.requireNonNull(stage, "Parameter 'stage' must not be null.");
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        columnsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 25));
        columnsSpinner.valueProperty().addListener((a, b, c) -> updateValidIslandsRange());

        rowsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 25));
        rowsSpinner.valueProperty().addListener((a, b, c) -> updateValidIslandsRange());

        updateValidIslandsRange();

        autoGenerateRadioButton.setSelected(true);
        updateSelectedMode(null);

        manualIslandsCountCheckBox.setSelected(false);
        updateManualIslandsArea(null);
    }

    private void updateValidIslandsRange() {
        final int max = (int) (0.2 * columnsSpinner.getValue() * rowsSpinner.getValue());
        islandsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, max));
    }

    /**
     * Invoked when the user selected the auto generation mode.
     * @param actionEvent the event.
     */
    public void updateSelectedMode(final ActionEvent actionEvent) {
        manualSettingsGridPane.setDisable(autoGenerateRadioButton.isSelected());
    }

    /**
     * Invoked when the user clicked the manual islands count checkbox.
     * @param actionEvent the event.
     */
    public void updateManualIslandsArea(final ActionEvent actionEvent) {
        islandsSpinner.setDisable(!manualIslandsCountCheckBox.isSelected());
    }

    /**
     * Invoked when the user clicked the cancel button.
     * @param actionEvent the event.
     */
    public void cancel(final ActionEvent actionEvent) {
        stage.close();
    }

    /**
     * Invoked when the user clicked the ok button.
     * @param actionEvent the event.
     */
    public void ok(final ActionEvent actionEvent) {
        if (autoGenerateRadioButton.isSelected()) {
            model.newPuzzle();
        } else {
            int columns = columnsSpinner.getValue();
            int rows = rowsSpinner.getValue();

            if (manualIslandsCountCheckBox.isSelected()) {
                int islands = islandsSpinner.getValue();
                model.newPuzzle(columns, rows, islands);
            } else {
                model.newPuzzle(columns, rows);
            }
        }
        stage.close();
    }
}
