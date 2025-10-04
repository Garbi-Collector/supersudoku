package garbi.supersudoku.service;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Gestor de escenas que mantiene la cortina siempre visible
 * durante las transiciones entre vistas.
 */
public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;
    private StackPane rootContainer;
    private Parent currentView;
    private AnimationService animationService = AnimationService.getInstance();

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Inicializa el SceneManager con el Stage principal
     */
    public void initialize(Stage stage, String initialFxmlPath) throws IOException {
        this.primaryStage = stage;

        // Contenedor principal que nunca cambia
        rootContainer = new StackPane();

        // Cargar vista inicial
        FXMLLoader loader = new FXMLLoader(getClass().getResource(initialFxmlPath));
        currentView = loader.load();
        rootContainer.getChildren().add(currentView);

        // Crear la escena
        Scene scene = new Scene(rootContainer, 600, 400);
        stage.setScene(scene);

        // Inicializar animaciones en la vista inicial
        Platform.runLater(() -> {
            animationService.initializeNumberRain((Pane) currentView);
            animationService.initializeCurtain((Pane) currentView, true);
            animationService.raiseCurtain(null);
        });
    }

    /**
     * Navega a una nueva vista con transición de cortina
     */
    public void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newView = loader.load();

            // Bajar cortina
            animationService.lowerCurtain(() -> {
                // Reemplazar la vista mientras la cortina está abajo
                rootContainer.getChildren().remove(currentView);
                rootContainer.getChildren().add(0, newView);
                currentView = newView;

                // Reinicializar animaciones en la nueva vista
                Platform.runLater(() -> {
                    animationService.initializeNumberRain((Pane) currentView);
                    animationService.initializeCurtain((Pane) currentView, true);
                    animationService.raiseCurtain(null);
                });
            });

        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error al cargar la vista: " + fxmlPath);
        }
    }
    /**
     * Navega a una nueva vista con transición de cortina
     * Solo usa la animación de cortina, sin lluvia de números
     */
    public void navigateToWithCurtainOnly(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newView = loader.load();

            // Bajar cortina
            animationService.lowerCurtain(() -> {
                // Reemplazar la vista mientras la cortina está abajo
                rootContainer.getChildren().remove(currentView);
                rootContainer.getChildren().add(0, newView);
                currentView = newView;
            });

        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error al cargar la vista: " + fxmlPath);
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
