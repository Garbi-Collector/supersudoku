package garbi.supersudoku.models;

import java.util.Arrays;

public class Sudoku {
    private static final int SIZE = 9;       // tamaño del tablero 9x9
    private static final int EMPTY = 0;      // valor que representa celda vacía
    private int[][] board;                   // el tablero en sí

    // --- Constructores ---
    public Sudoku() {
        board = new int[SIZE][SIZE];
    }

    public Sudoku(int[][] initialBoard) {
        if (initialBoard.length != SIZE || initialBoard[0].length != SIZE) {
            throw new IllegalArgumentException("El tablero debe ser 9x9.");
        }
        board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            board[i] = Arrays.copyOf(initialBoard[i], SIZE);
        }
    }

    // --- Métodos de acceso ---
    public int getCell(int row, int col) {
        return board[row][col];
    }

    public void setCell(int row, int col, int value) {
        if (value < 0 || value > SIZE) {
            throw new IllegalArgumentException("El valor debe estar entre 0 y 9.");
        }
        board[row][col] = value;
    }

    public int[][] getBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            copy[i] = Arrays.copyOf(board[i], SIZE);
        }
        return copy;
    }

    // --- Utilidades ---
    public boolean isEmpty(int row, int col) {
        return board[row][col] == EMPTY;
    }

    public void clearCell(int row, int col) {
        board[row][col] = EMPTY;
    }

    public void printBoard() {
        for (int r = 0; r < SIZE; r++) {
            if (r % 3 == 0 && r != 0) {
                System.out.println("------+-------+------");
            }
            for (int c = 0; c < SIZE; c++) {
                if (c % 3 == 0 && c != 0) System.out.print("| ");
                System.out.print((board[r][c] == EMPTY ? "." : board[r][c]) + " ");
            }
            System.out.println();
        }
    }

    // --- Validaciones básicas ---
    public boolean isValidMove(int row, int col, int value) {
        if (value == EMPTY) return true;

        // fila
        for (int c = 0; c < SIZE; c++) {
            if (board[row][c] == value) return false;
        }
        // columna
        for (int r = 0; r < SIZE; r++) {
            if (board[r][col] == value) return false;
        }
        // subcuadro 3x3
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[boxRow + r][boxCol + c] == value) return false;
            }
        }
        return true;
    }

    public boolean isComplete() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == EMPTY) return false;
            }
        }
        return true;
    }
}
