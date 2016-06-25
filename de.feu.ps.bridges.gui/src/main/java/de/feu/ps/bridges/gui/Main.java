package de.feu.ps.bridges.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tim on 05.04.16.
 */
public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");
        Parent root = FXMLLoader.load(getClass().getResource("MainFrame.fxml"), bundle);
        primaryStage.setTitle(bundle.getString("mainFrame.title"));
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();
    }
}
