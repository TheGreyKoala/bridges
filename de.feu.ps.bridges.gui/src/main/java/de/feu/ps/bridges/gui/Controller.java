package de.feu.ps.bridges.gui;

import de.feu.ps.bridges.facade.Facade;
import de.feu.ps.bridges.model.Puzzle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Tim Gremplewski
 */
public class Controller implements Initializable {

    @FXML
    private Pane mainPanel;

    private ResourceBundle bundle;
    private FileChooser fileChooser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(bundle.getString("puzzleExtensionFilter.description"), "*.bgs"));
    }

    public void newPuzzle(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("NewPuzzleDialog.fxml"), bundle);
        Stage dialogStage = new Stage();
        dialogStage.setTitle(bundle.getString("newPuzzleDialog.title"));
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);

        // TODO Set dialog modal

        //dialogStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

        // TODO Get controller from loader?

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public void loadPuzzle(ActionEvent actionEvent) {
        fileChooser.setTitle(bundle.getString("loadDialog.title"));
        File sourceFile = fileChooser.showOpenDialog(mainPanel.getScene().getWindow());

        if (sourceFile != null) {
            Puzzle puzzle = Facade.loadPuzzle(sourceFile);
            mainPanel.getChildren().clear();
            new PuzzleDrawer().drawPuzzle(puzzle, mainPanel);
            fileChooser.setInitialDirectory(sourceFile.getParentFile());
        }
    }

    public void savePuzzleAs(ActionEvent actionEvent) {
        // TODO Open at last location
        fileChooser.setTitle(bundle.getString("saveAsDialog.title"));
        File destinationFile = fileChooser.showSaveDialog(mainPanel.getScene().getWindow());

        if (destinationFile != null) {
            fileChooser.setInitialDirectory(destinationFile.getParentFile());
        }
    }

    public void exit(ActionEvent actionEvent) {
        // TODO Can we inject the stage?
        ((Stage) mainPanel.getScene().getWindow()).close();
    }
}
