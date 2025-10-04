package garbi.supersudoku.models;

/**
 * Configuración del juego según la dificultad seleccionada
 */
public class GameConfig {
    private final DifficultyLevel difficulty;
    private final int cellsToRemove;

    public enum DifficultyLevel {
        NORMAL("Normal", 2),
        EXPERTO("Experto", 20);

        private final String displayName;
        private final int cellsToRemove;

        DifficultyLevel(String displayName, int cellsToRemove) {
            this.displayName = displayName;
            this.cellsToRemove = cellsToRemove;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getCellsToRemove() {
            return cellsToRemove;
        }
    }

    // Singleton para mantener la configuración actual
    private static GameConfig instance;

    private GameConfig(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
        this.cellsToRemove = difficulty.getCellsToRemove();
    }

    public static void setDifficulty(DifficultyLevel difficulty) {
        instance = new GameConfig(difficulty);
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig(DifficultyLevel.NORMAL); // default
        }
        return instance;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public int getCellsToRemove() {
        return cellsToRemove;
    }

    public String getDifficultyName() {
        return difficulty.getDisplayName();
    }
}