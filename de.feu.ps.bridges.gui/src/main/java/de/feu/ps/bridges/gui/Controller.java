package de.feu.ps.bridges.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Tim Gremplewski
 */
public class Controller implements Initializable {

    @FXML
    private Pane mainPanel;

    private Rotate rotate = Transform.rotate(45, 0, 0);

    @FXML
    protected void addCircle(MouseEvent event2) {
        Circle circle = new Circle(50, Color.BLUE);
        circle.relocate(0, 0);
        circle.setOnMouseClicked(event -> {
            Point2D transform = rotate.transform(event.getX(), event.getY());

            if (transform.getX() >= 0 && transform.getY() >= 0) {
                System.out.println("Brücke nach rechts");
            } else if (transform.getX() < 0 && transform.getY() >= 0) {
                System.out.println("Brücke nach unten");
            } else if (transform.getX() < 0 && transform.getY() < 0) {
                System.out.println("Brücke nach links");
            } else {
                System.out.println("Brücke nach oben");
            }
        });

        Line line1 = new Line(100, 0, 0, 100);
        line1.setFill(Color.RED);

        Line line2 = new Line(0, 0, 100, 100);
        line2.setFill(Color.RED);

        mainPanel.getChildren().addAll(circle, line1, line2);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPanel.setStyle("-fx-background-color: black;");
        mainPanel.setMinSize(100, 100);
    }
}
