module garbi.supersudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    // Abrimos los paquetes al FXMLLoader
    opens garbi.supersudoku to javafx.fxml;
    opens garbi.supersudoku.controller to javafx.fxml;

    exports garbi.supersudoku;
}
