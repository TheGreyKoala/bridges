package de.feu.ps.bridges.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Tim Gremplewski
 */
public class NewPuzzleController implements Initializable {

    @FXML
    private Spinner<Integer> widthSpinner;

    @FXML
    private Spinner<Integer> heightSpinner;

    @FXML
    private Spinner<Integer> islandsCountSpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Can this be done in the fxml file?
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 25));
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 25));
        islandsCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 125));
    }
}
