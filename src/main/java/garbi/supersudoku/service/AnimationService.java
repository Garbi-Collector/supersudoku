package garbi.supersudoku.service;

import javafx.animation.TranslateTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Random;

/**
 * Servicio singleton para gestionar animaciones compartidas entre vistas.
 * Similar a un servicio en Angular, mantiene el estado de las animaciones
 * y permite reutilizarlas en cualquier controlador.
 */
public class AnimationService {

    private static AnimationService instance;
    private Pane curtainPane;
    private Pane numbersPane;
    private Timeline numberRainTimeline;
    private Random random = new Random();

    // Singleton
    private AnimationService() {}

    public static AnimationService getInstance() {
        if (instance == null) {
            instance = new AnimationService();
        }
        return instance;
    }

    /**
     * Inicializa el fondo de lluvia de números en el contenedor dado
     */
    public void initializeNumberRain(Pane rootPane) {
        // Si ya existe un numbersPane, lo removemos primero
        if (numbersPane != null && numbersPane.getParent() != null) {
            ((Pane) numbersPane.getParent()).getChildren().remove(numbersPane);
        }

        numbersPane = new Pane();
        numbersPane.setPrefSize(rootPane.getPrefWidth(), rootPane.getPrefHeight());
        numbersPane.setMouseTransparent(true);

        // Insertar al inicio para que esté detrás de todo
        rootPane.getChildren().add(0, numbersPane);

        startNumberRain();
    }

    /**
     * Inicializa la cortina de transición (sobrecarga para compatibilidad)
     */
    public void initializeCurtain(Pane rootPane) {
        initializeCurtain(rootPane, false);
    }

    /**
     * Inicializa la cortina de transición
     * @param startDown si true, la cortina empieza abajo (cubriendo todo)
     */
    public void initializeCurtain(Pane rootPane, boolean startDown) {
        // Si ya existe una cortina, la removemos
        if (curtainPane != null && curtainPane.getParent() != null) {
            ((Pane) curtainPane.getParent()).getChildren().remove(curtainPane);
        }

        double width = rootPane.getWidth() > 0 ? rootPane.getWidth() : 600;
        double height = rootPane.getHeight() > 0 ? rootPane.getHeight() : 400;

        curtainPane = new Pane();
        curtainPane.setPrefSize(width, height);
        curtainPane.setStyle("-fx-background-color: #1a1a1a;");
        curtainPane.setLayoutX(0);
        curtainPane.setLayoutY(0);

        // Si startDown=true, la cortina empieza cubriendo la pantalla
        // Si startDown=false, empieza arriba (fuera de vista)
        curtainPane.setTranslateY(startDown ? 0 : -height);
        curtainPane.setMouseTransparent(false);

        populateCurtainWithNumbers(width, height);

        // Agregar la cortina al final para que esté encima de todo
        rootPane.getChildren().add(curtainPane);
        curtainPane.toFront();
    }

    /**
     * Levanta la cortina con animación
     */
    public void raiseCurtain(Runnable onFinished) {
        if (curtainPane == null) return;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.8), curtainPane);
        tt.setFromY(0);
        tt.setToY(-curtainPane.getPrefHeight());
        tt.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });

        System.out.println("Subiendo cortina");
        tt.play();
    }

    /**
     * Baja la cortina con animación
     */
    public void lowerCurtain(Runnable onFinished) {
        if (curtainPane == null) return;

        curtainPane.toFront();
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.8), curtainPane);
        tt.setFromY(-curtainPane.getPrefHeight());
        tt.setToY(0);
        tt.setOnFinished(e -> {
            Timeline delay = new Timeline(new KeyFrame(Duration.millis(200), ev -> {
                if (onFinished != null) onFinished.run();
            }));
            delay.play();
        });

        System.out.println("Bajando cortina");
        tt.play();
    }

    /**
     * Detiene la lluvia de números
     */
    public void stopNumberRain() {
        if (numberRainTimeline != null) {
            numberRainTimeline.stop();
        }
    }

    /**
     * Limpia todos los recursos del servicio
     */
    public void cleanup() {
        stopNumberRain();
        if (numbersPane != null && numbersPane.getParent() != null) {
            ((Pane) numbersPane.getParent()).getChildren().remove(numbersPane);
        }
        if (curtainPane != null && curtainPane.getParent() != null) {
            ((Pane) curtainPane.getParent()).getChildren().remove(curtainPane);
        }
    }

    // Métodos privados auxiliares

    private void startNumberRain() {
        if (numberRainTimeline != null) {
            numberRainTimeline.stop();
        }

        numberRainTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (numbersPane.getWidth() <= 0) return;

            Text number = new Text(String.valueOf(random.nextInt(10)));
            number.setX(random.nextDouble() * numbersPane.getWidth());
            number.setY(0);
            number.setFill(randomColor());
            number.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");

            numbersPane.getChildren().add(number);

            Timeline fall = new Timeline(new KeyFrame(Duration.millis(50), ev -> {
                number.setY(number.getY() + 5);
                if (number.getY() > numbersPane.getHeight()) {
                    numbersPane.getChildren().remove(number);
                }
            }));
            fall.setCycleCount(Timeline.INDEFINITE);
            fall.play();
        }));
        numberRainTimeline.setCycleCount(Timeline.INDEFINITE);
        numberRainTimeline.play();
    }

    private void populateCurtainWithNumbers(double width, double height) {
        for (int i = 0; i < 150; i++) {
            Text number = new Text(String.valueOf(random.nextInt(10)));
            number.setX(random.nextDouble() * width);
            number.setY(random.nextDouble() * height);
            number.setFill(randomColor());
            number.setOpacity(0.7);
            number.setStyle("-fx-font-size:" + (15 + random.nextInt(20)) + "px; -fx-font-weight:bold;");
            curtainPane.getChildren().add(number);
        }
    }

    private Color randomColor() {
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.PURPLE};
        return colors[random.nextInt(colors.length)];
    }
}