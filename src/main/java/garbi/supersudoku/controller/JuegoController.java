package garbi.supersudoku.controller;

import garbi.supersudoku.models.GameConfig;
import garbi.supersudoku.models.Sudoku;
import garbi.supersudoku.service.SudokuService;
import garbi.supersudoku.service.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class JuegoController {
    @FXML
    private GridPane sudokuGrid;
    @FXML
    private Button btnNuevoJuego;
    @FXML
    private Button btnVerificar;
    @FXML
    private Button btnBack;
    @FXML
    private Pane numbersPane;
    @FXML
    private Label lblDificultad; // Opcional: para mostrar la dificultad actual

    private SudokuService sudokuService;
    private Sudoku sudoku;
    private Sudoku solucion; // Guardamos la solución completa
    private TextField[][] textFields;
    private SceneManager sceneManager = SceneManager.getInstance();
    private GameConfig gameConfig;

    @FXML
    public void initialize() {
        sudokuService = new SudokuService();
        gameConfig = GameConfig.getInstance();
        textFields = new TextField[9][9];

        // Mostrar dificultad actual (opcional)
        if (lblDificultad != null) {
            lblDificultad.setText("Dificultad: " + gameConfig.getDifficultyName());
        }

        // Configurar botón de regreso
        btnBack.setOnAction(e -> sceneManager.navigateTo("/garbi/supersudoku/dificultad.fxml"));

        inicializarTablero();
        generarNuevoJuego();
    }

    private void inicializarTablero() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField textField = new TextField();
                textField.setPrefSize(50, 50);
                textField.setAlignment(Pos.CENTER);
                textField.setFont(javafx.scene.text.Font.font("Arial", 20));

                // Limitar entrada a un solo dígito
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("[1-9]?")) {
                        textField.setText(oldValue);
                    }
                });

                // Estilo mejorado para las celdas
                String style = "-fx-border-color: #8b7bb8; -fx-border-width: 1; " +
                        "-fx-background-color: white; " +
                        "-fx-text-fill: #4b2e83; " +
                        "-fx-font-weight: bold;";

                // Bordes más gruesos para separar los bloques 3x3
                if (row % 3 == 0 && row != 0) style += "-fx-border-width: 3 1 1 1;";
                if (col % 3 == 0 && col != 0) style += "-fx-border-width: 1 1 1 3;";
                if (row == 0) style += "-fx-border-width: 3 1 1 1;";
                if (col == 0) style += "-fx-border-width: 1 1 1 3;";

                textField.setStyle(style);
                textFields[row][col] = textField;
                sudokuGrid.add(textField, col, row);
            }
        }
    }

    private void actualizarTablero() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int valor = sudoku.getCell(row, col);
                TextField tf = textFields[row][col];

                if (valor != 0) {
                    tf.setText(String.valueOf(valor));
                    tf.setEditable(false);
                    // Color similar al gradiente dorado de los botones
                    tf.setStyle(tf.getStyle().replaceAll("-fx-background-color: [^;]+;",
                            "-fx-background-color: #fffae3;"));
                } else {
                    tf.setText("");
                    tf.setEditable(true);
                    tf.setStyle(tf.getStyle().replaceAll("-fx-background-color: [^;]+;",
                            "-fx-background-color: white;"));
                }
            }
        }
    }

    @FXML
    private void generarNuevoJuego() {
        // Generar sudoku completo
        solucion = sudokuService.generateFullSudoku();

        // Crear copia para el puzzle
        sudoku = new Sudoku(solucion.getBoard());

        // Eliminar celdas según la dificultad
        int cellsToRemove = gameConfig.getCellsToRemove();
        eliminarCeldasAleatorias(sudoku, cellsToRemove);

        actualizarTablero();
    }

    /**
     * Elimina N celdas aleatorias del sudoku
     */
    private void eliminarCeldasAleatorias(Sudoku sudoku, int count) {
        java.util.Random random = new java.util.Random();
        java.util.Set<String> celdasEliminadas = new java.util.HashSet<>();

        while (celdasEliminadas.size() < count) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            String key = row + "," + col;

            if (!celdasEliminadas.contains(key)) {
                sudoku.clearCell(row, col);
                celdasEliminadas.add(key);
            }
        }
    }

    @FXML
    private void verificarSolucion() {
        // Leer valores del tablero
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = textFields[row][col].getText();
                if (!text.isEmpty()) {
                    sudoku.setCell(row, col, Integer.parseInt(text));
                }
            }
        }

        // Verificar si está completo
        if (!sudoku.isComplete()) {
            mostrarMensaje("Incompleto", "Aún faltan celdas por completar.");
            return;
        }

        // Verificar si es válido
        boolean esValido = true;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int valor = sudoku.getCell(row, col);
                sudoku.clearCell(row, col);
                if (!sudoku.isValidMove(row, col, valor)) {
                    esValido = false;
                }
                sudoku.setCell(row, col, valor);
            }
        }

        if (esValido) {
            mostrarMensaje("¡Correcto!",
                    "¡Felicidades! Has resuelto el Sudoku " +
                            gameConfig.getDifficultyName() + " correctamente.");
        } else {
            mostrarMensaje("Incorrecto",
                    "Hay errores en tu solución. Revisa las reglas del Sudoku.");
        }
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
        );
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}