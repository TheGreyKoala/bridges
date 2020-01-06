module de.feu.ps.bridges {
    requires java.logging;
    requires java.desktop;

    requires javafx.controls;
    requires javafx.fxml;

    opens de.feu.ps.bridges.gui.controller to javafx.fxml;
    exports de.feu.ps.bridges;
}
