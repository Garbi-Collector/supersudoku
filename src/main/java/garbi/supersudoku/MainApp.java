package garbi.supersudoku;

import garbi.supersudoku.models.Sudoku;
import garbi.supersudoku.service.SceneManager;
import garbi.supersudoku.service.SudokuService;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Super Sudoku");

        // Tamaño inicial fijo
        primaryStage.setWidth(700);
        primaryStage.setHeight(600);

// Evitar que el usuario redimensione manualmente, pero permitir maximizar
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(600);
// NO seteamos maxWidth ni maxHeight

        // Inicializar el SceneManager con la vista del menú
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.initialize(primaryStage, "/garbi/supersudoku/menu.fxml");

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
