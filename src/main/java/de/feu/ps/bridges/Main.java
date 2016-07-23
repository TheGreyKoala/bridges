package de.feu.ps.bridges;

import de.feu.ps.bridges.gui.controller.MainController;
import de.feu.ps.bridges.gui.gamestate.GameState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class that starts the gui application.
 */
public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Creates a new instance.
     */
    public Main() {
    }

    /**
     * Main entry point of the gui application.
     * @param args arguments - not used.
     */
    public static void main(final String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
        }
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("de.feu.ps.bridges.gui.bundles.Bridges");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/feu/ps/bridges/gui/MainFrame.fxml"), bundle);
        fxmlLoader.<MainController>setControllerFactory(param -> new MainController(new GameState(), primaryStage));

        Parent root = fxmlLoader.load();
        primaryStage.setTitle(bundle.getString("mainFrame.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
