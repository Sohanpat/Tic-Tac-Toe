/**
 * Represents the 3x3 tic-tac-toe grid.
 *
 * Cells are numbered 1–9, left-to-right, top-to-bottom:
 *   1 | 2 | 3
 *  ---+---+---
 *   4 | 5 | 6
 *  ---+---+---
 *   7 | 8 | 9
 *
 * Internally stored in a single char array of length 9.
 * An unmarked cell holds its digit character ('1'–'9').
 * A marked cell holds 'X' or 'O'.
 */
public class Board {

    public static final int SIZE         = 3;
    public static final int TOTAL_CELLS  = SIZE * SIZE;

    private final char[] cells;

    // ── Construction / reset ─────────────────────────────────────────────────

    public Board() {
        cells = new char[TOTAL_CELLS];
        reset();
    }

    /** Clears all marks and restores the numbered labels. */
    public void reset() {
        for (int i = 0; i < TOTAL_CELLS; i++) {
            cells[i] = (char) ('1' + i);
        }
    }

    // ── Moves ────────────────────────────────────────────────────────────────

    /**
     * Attempts to place a marker on the board.
     *
     * @param cellNumber 1–9
     * @param marker     'X' or 'O'
     * @return true if the move was accepted; false if the cell is taken or
     *         the number is out of range
     */
    public boolean placeMarker(int cellNumber, char marker) {
        if (!isInRange(cellNumber))   return false;
        if (isTaken(cellNumber))      return false;
        cells[cellNumber - 1] = marker;
        return true;
    }

    // ── State queries ────────────────────────────────────────────────────────

    /** Returns true if the given marker fills an entire row, column, or diagonal. */
    public boolean hasWon(char marker) {
        // Rows
        for (int r = 0; r < SIZE; r++) {
            if (rowComplete(r, marker)) return true;
        }
        // Columns
        for (int c = 0; c < SIZE; c++) {
            if (colComplete(c, marker)) return true;
        }
        // Diagonals
        return mainDiagComplete(marker) || antiDiagComplete(marker);
    }

    /** Returns true when every cell has been marked (no empty cells remain). */
    public boolean isFull() {
        for (char c : cells) {
            if (c != 'X' && c != 'O') return false;
        }
        return true;
    }

    /** Raw access to a cell value — used by tests and toString. */
    public char getCellValue(int cellNumber) {
        return cells[cellNumber - 1];
    }

    // ── Display ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        for (int r = 0; r < SIZE; r++) {
            sb.append("  ");
            for (int c = 0; c < SIZE; c++) {
                sb.append(" ").append(cells[r * SIZE + c]).append(" ");
                if (c < SIZE - 1) sb.append("|");
            }
            sb.append("\n");
            if (r < SIZE - 1) sb.append("  ---+---+---\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private boolean isInRange(int cellNumber) {
        return cellNumber >= 1 && cellNumber <= TOTAL_CELLS;
    }

    private boolean isTaken(int cellNumber) {
        char v = cells[cellNumber - 1];
        return v == 'X' || v == 'O';
    }

    private boolean rowComplete(int row, char m) {
        for (int c = 0; c < SIZE; c++) {
            if (cells[row * SIZE + c] != m) return false;
        }
        return true;
    }

    private boolean colComplete(int col, char m) {
        for (int r = 0; r < SIZE; r++) {
            if (cells[r * SIZE + col] != m) return false;
        }
        return true;
    }

    private boolean mainDiagComplete(char m) {
        for (int i = 0; i < SIZE; i++) {
            if (cells[i * SIZE + i] != m) return false;
        }
        return true;
    }

    private boolean antiDiagComplete(char m) {
        for (int i = 0; i < SIZE; i++) {
            if (cells[i * SIZE + (SIZE - 1 - i)] != m) return false;
        }
        return true;
    }
}
