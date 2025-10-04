package garbi.supersudoku.controller;

import garbi.supersudoku.models.GameConfig;
import garbi.supersudoku.service.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DificultadController {
    @FXML
    private Button btnBack, btnNormal, btnExperto;

    private SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    public void initialize() {
        btnBack.setOnAction(e -> sceneManager.navigateTo("/garbi/supersudoku/menu.fxml"));

        // Configurar dificultad Normal (2 celdas vacías)
        btnNormal.setOnAction(e -> {
            GameConfig.setDifficulty(GameConfig.DifficultyLevel.NORMAL);
            sceneManager.navigateToWithCurtainOnly("/garbi/supersudoku/Juego.fxml");
        });

        // Configurar dificultad Experto (20 celdas vacías)
        btnExperto.setOnAction(e -> {
            GameConfig.setDifficulty(GameConfig.DifficultyLevel.EXPERTO);
            sceneManager.navigateToWithCurtainOnly("/garbi/supersudoku/Juego.fxml");
        });
    }
}