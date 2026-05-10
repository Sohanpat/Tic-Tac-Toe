import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An opportunistic computer-controlled player.
 *
 * On each turn, chooses a cell by checking these rules in order:
 *
 *   1. If the board is empty (first move of the game), pick a random corner.
 *   2. If it is the second move of the game AND the center is free, take it.
 *   3. If there is a winning move available, take it.
 *   4. If the opponent has a winning move available, block it.
 *   5. Otherwise, pick a random empty cell.
 *
 * The class is self-contained: call {@link #chooseCell(Board, char)} with
 * the current board state and the opponent's marker to get the chosen cell
 * number (1–9).  The caller is responsible for actually placing the marker
 * via {@link Board#placeMarker(int, char)}.
 */
public class ComputerPlayer extends Player {

    private static final int   CENTER  = 5;
    private static final int[] CORNERS = {1, 3, 7, 9};

    private final Random random;

    // ── Constructor ──────────────────────────────────────────────────────────

    public ComputerPlayer(String name, char marker) {
        super(name, marker);
        this.random = new Random();
    }

    /** Package-private constructor that accepts a seeded Random for testing. */
    ComputerPlayer(String name, char marker, Random random) {
        super(name, marker);
        this.random = random;
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Decides which cell to mark this turn.
     *
     * @param board          the current board (not yet modified)
     * @param opponentMarker the marker belonging to the human/other player
     * @return a cell number 1–9 that is guaranteed to be empty
     */
    public int chooseCell(Board board, char opponentMarker) {

        // Rule 1 — first move: pick a random corner
        if (isBoardEmpty(board)) {
            return randomCorner();
        }

        // Rule 2 — second move: take the center if free
        if (isSecondMove(board) && isCellFree(board, CENTER)) {
            return CENTER;
        }

        // Rule 3 — win if possible
        int winning = findWinningMove(board, getMarker());
        if (winning != -1) return winning;

        // Rule 4 — block opponent from winning
        int blocking = findWinningMove(board, opponentMarker);
        if (blocking != -1) return blocking;

        // Rule 5 — random empty cell
        return randomEmptyCell(board);
    }

    // ── Strategy helpers ─────────────────────────────────────────────────────

    /**
     * Returns true when no cell on the board has been marked yet.
     */
    private boolean isBoardEmpty(Board board) {
        for (int cell = 1; cell <= Board.TOTAL_CELLS; cell++) {
            char v = board.getCellValue(cell);
            if (v == 'X' || v == 'O') return false;
        }
        return true;
    }

    /**
     * Returns true when exactly one cell has been marked (so the computer's
     * upcoming move will be the second move of the entire game).
     */
    private boolean isSecondMove(Board board) {
        int marked = 0;
        for (int cell = 1; cell <= Board.TOTAL_CELLS; cell++) {
            char v = board.getCellValue(cell);
            if (v == 'X' || v == 'O') marked++;
        }
        return marked == 1;
    }

    /**
     * Scans every empty cell and returns the first one that, if marked with
     * {@code marker}, would satisfy {@link Board#hasWon(char)}.
     *
     * @return the winning cell number, or -1 if none exists
     */
    private int findWinningMove(Board board, char marker) {
        for (int cell = 1; cell <= Board.TOTAL_CELLS; cell++) {
            if (!isCellFree(board, cell)) continue;

            board.placeMarker(cell, marker);          // try the move
            boolean wins = board.hasWon(marker);
            board.undoMarker(cell);                   // always undo

            if (wins) return cell;
        }
        return -1;
    }

    private boolean isCellFree(Board board, int cell) {
        char v = board.getCellValue(cell);
        return v != 'X' && v != 'O';
    }

    private int randomCorner() {
        return CORNERS[random.nextInt(CORNERS.length)];
    }

    private int randomEmptyCell(Board board) {
        List<Integer> empty = new ArrayList<>();
        for (int cell = 1; cell <= Board.TOTAL_CELLS; cell++) {
            if (isCellFree(board, cell)) empty.add(cell);
        }
        return empty.get(random.nextInt(empty.size()));
    }
}
