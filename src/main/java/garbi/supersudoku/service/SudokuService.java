package garbi.supersudoku.service;

import garbi.supersudoku.models.Sudoku;

public class SudokuService {

    // Resuelve un sudoku usando backtracking
    public boolean solve(Sudoku sudoku) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (sudoku.isEmpty(row, col)) {
                    for (int num = 1; num <= 9; num++) {
                        if (sudoku.isValidMove(row, col, num)) {
                            sudoku.setCell(row, col, num);

                            if (solve(sudoku)) {
                                return true;
                            }

                            sudoku.clearCell(row, col); // backtrack
                        }
                    }
                    return false; // no hay número válido
                }
            }
        }
        return true; // tablero completo
    }

    // Verifica si el sudoku tiene UNA sola solución
    public int countSolutions(Sudoku sudoku) {
        return countSolutionsHelper(sudoku, 0, 0, 0);
    }

    private int countSolutionsHelper(Sudoku sudoku, int row, int col, int count) {
        if (count > 1) return count; // cortar si ya hay más de una solución

        if (row == 9) return count + 1; // tablero completo = 1 solución encontrada

        int nextRow = (col == 8) ? row + 1 : row;
        int nextCol = (col + 1) % 9;

        if (!sudoku.isEmpty(row, col)) {
            return countSolutionsHelper(sudoku, nextRow, nextCol, count);
        }

        for (int num = 1; num <= 9; num++) {
            if (sudoku.isValidMove(row, col, num)) {
                sudoku.setCell(row, col, num);
                count = countSolutionsHelper(sudoku, nextRow, nextCol, count);
                sudoku.clearCell(row, col);
            }
        }
        return count;
    }

    public Sudoku generateFullSudoku() {
        Sudoku sudoku = new Sudoku();
        fillBoard(sudoku);
        return sudoku;
    }

    private boolean fillBoard(Sudoku sudoku) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (sudoku.isEmpty(row, col)) {
                    int[] numbers = java.util.stream.IntStream.rangeClosed(1, 9)
                            .toArray();
                    shuffleArray(numbers);

                    for (int num : numbers) {
                        if (sudoku.isValidMove(row, col, num)) {
                            sudoku.setCell(row, col, num);
                            if (fillBoard(sudoku)) return true;
                            sudoku.clearCell(row, col);
                        }
                    }
                    return false; // no se pudo llenar esta celda
                }
            }
        }
        return true;
    }

    private void shuffleArray(int[] array) {
        java.util.Random random = new java.util.Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public Sudoku generatePuzzle(int clues) {
        Sudoku sudoku = generateFullSudoku();
        java.util.Random random = new java.util.Random();

        int attempts = 81; // límite para no quedarse en loop
        while (attempts > 0 && countFilledCells(sudoku) > clues) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (!sudoku.isEmpty(row, col)) {
                int backup = sudoku.getCell(row, col);
                sudoku.clearCell(row, col);

                if (countSolutions(sudoku) != 1) {
                    sudoku.setCell(row, col, backup); // revertir si no queda única solución
                }
            }
            attempts--;
        }
        return sudoku;
    }

    private int countFilledCells(Sudoku sudoku) {
        int count = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (!sudoku.isEmpty(r, c)) count++;
            }
        }
        return count;
    }

}
