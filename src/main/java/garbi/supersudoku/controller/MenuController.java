package garbi.supersudoku.controller;

import garbi.supersudoku.service.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

public class MenuController {

    @FXML
    private Button btnJugar, btnRecords, btnSalir;
    @FXML
    private Hyperlink githubLink;

    private SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    public void initialize() {
        btnJugar.setOnAction(e -> sceneManager.navigateTo("/garbi/supersudoku/dificultad.fxml"));
        btnRecords.setOnAction(e -> System.out.println("Ir a records..."));
        btnSalir.setOnAction(e -> Platform.exit());

        githubLink.setOnAction(e -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://github.com/Garbi-Collector"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}