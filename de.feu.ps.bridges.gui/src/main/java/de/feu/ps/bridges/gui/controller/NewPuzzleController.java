package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.gui.gamestate.GameState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
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

    private final GameState gameState;
    private final Stage stage;

    public NewPuzzleController(final GameState gameState, final Stage stage) {
        this.gameState = gameState;
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Can this be done in the fxml file?
        columnsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 25));
        rowsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 25));
        islandsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 125));

        autoGenerateRadioButton.setSelected(true);
        autoModeSelected(null);

        manualIslandsCountCheckBox.setSelected(false);
        manualIslandsCountClicked(null);
    }

    public void autoModeSelected(ActionEvent actionEvent) {
        manualSettingsGridPane.setDisable(true);
    }

    public void manualModeSelected(ActionEvent actionEvent) {
        manualSettingsGridPane.setDisable(false);
    }

    public void manualIslandsCountClicked(ActionEvent actionEvent) {
        boolean manualIslandsCount = manualIslandsCountCheckBox.isSelected();
        islandsSpinner.setDisable(!manualIslandsCount);
    }

    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }

    public void ok(ActionEvent actionEvent) {
        if (autoGenerateRadioButton.isSelected()) {
            gameState.newPuzzle();
        } else {
            int columns = columnsSpinner.getValue();
            int rows = rowsSpinner.getValue();

            if (manualIslandsCountCheckBox.isSelected()) {
                int islands = islandsSpinner.getValue();
                gameState.newPuzzle(columns, rows, islands);
            } else {
                gameState.newPuzzle(columns, rows);
            }
        }
        stage.close();
    }
}
