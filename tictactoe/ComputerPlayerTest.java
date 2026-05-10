import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ComputerPlayer.java
 *
 * Each test exercises one of the five strategy rules in isolation.
 * A seeded (fixed) Random is injected so corner/random choices are
 * deterministic during testing.
 */
class ComputerPlayerTest {

    private static final char COMPUTER_MARKER = 'X';
    private static final char OPPONENT_MARKER = 'O';

    // Seeded Random that always returns index 0 — first corner (cell 1)
    private static final Random FIXED_RANDOM = new Random(0) {
        @Override public int nextInt(int bound) { return 0; }
    };

    private Board          board;
    private ComputerPlayer computer;

    @BeforeEach
    void setUp() {
        board    = new Board();
        computer = new ComputerPlayer("Computer", COMPUTER_MARKER, FIXED_RANDOM);
    }

    // ── Rule 1: empty board → corner ─────────────────────────────────────────

    @Test
    void rule1_emptyBoard_choosesACorner() {
        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertTrue(isCorner(cell), "Expected a corner cell but got " + cell);
    }

    @Test
    void rule1_emptyBoard_doesNotModifyBoard() {
        computer.chooseCell(board, OPPONENT_MARKER);
        // Board should still be fully empty
        for (int i = 1; i <= 9; i++) {
            char v = board.getCellValue(i);
            assertNotEquals('X', v, "Cell " + i + " should still be unmarked");
            assertNotEquals('O', v, "Cell " + i + " should still be unmarked");
        }
    }

    // ── Rule 2: second move → center ─────────────────────────────────────────

    @Test
    void rule2_secondMove_centerFree_choosesCenter() {
        board.placeMarker(1, OPPONENT_MARKER);   // opponent went first
        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertEquals(5, cell);
    }

    @Test
    void rule2_secondMove_centerTaken_doesNotChooseCenter() {
        board.placeMarker(1, OPPONENT_MARKER);
        board.placeMarker(5, OPPONENT_MARKER);   // center already taken
        // Should skip rule 2 and fall through (no win/block either, so random)
        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertNotEquals(5, cell, "Center is taken, should not choose it");
    }

    // ── Rule 3: take the winning move ────────────────────────────────────────

    @Test
    void rule3_computerCanWin_takesWinningCell() {
        // Computer has two in the top row (1, 2) — cell 3 wins
        board.placeMarker(1, COMPUTER_MARKER);
        board.placeMarker(2, COMPUTER_MARKER);
        board.placeMarker(5, OPPONENT_MARKER);   // filler to make it not "second move"
        board.placeMarker(6, OPPONENT_MARKER);   // filler

        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertEquals(3, cell);
    }

    @Test
    void rule3_computerCanWinColumn_takesWinningCell() {
        // Computer has cells 1 and 4; cell 7 wins the left column
        board.placeMarker(1, COMPUTER_MARKER);
        board.placeMarker(4, COMPUTER_MARKER);
        board.placeMarker(2, OPPONENT_MARKER);   // fillers
        board.placeMarker(3, OPPONENT_MARKER);

        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertEquals(7, cell);
    }

    @Test
    void rule3_computerCanWinDiagonal_takesWinningCell() {
        // Computer has 1, 5; cell 9 wins the main diagonal
        board.placeMarker(1, COMPUTER_MARKER);
        board.placeMarker(5, COMPUTER_MARKER);
        board.placeMarker(2, OPPONENT_MARKER);
        board.placeMarker(3, OPPONENT_MARKER);

        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertEquals(9, cell);
    }

    // ── Rule 4: block the opponent ───────────────────────────────────────────

    @Test
    void rule4_opponentCanWin_blocksThreateningCell() {
        // Opponent has two in the top row (1, 2) — computer must block cell 3
        board.placeMarker(1, OPPONENT_MARKER);
        board.placeMarker(2, OPPONENT_MARKER);
        board.placeMarker(5, COMPUTER_MARKER);   // fillers so not rule 1/2
        board.placeMarker(9, COMPUTER_MARKER);

        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertEquals(3, cell);
    }

    @Test
    void rule4_opponentCanWinColumn_blocksThreateningCell() {
        // Opponent has 3 and 6; cell 9 completes right column
        board.placeMarker(3, OPPONENT_MARKER);
        board.placeMarker(6, OPPONENT_MARKER);
        board.placeMarker(1, COMPUTER_MARKER);
        board.placeMarker(2, COMPUTER_MARKER);

        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertEquals(9, cell);
    }

    // ── Rule 3 beats Rule 4: win over block ──────────────────────────────────

    @Test
    void rule3_beatRule4_choosesWinOverBlock() {
        // Computer can win (cells 1, 2 → cell 3) AND opponent threatens (7, 8 → cell 9)
        // Rule 3 should take priority
        board.placeMarker(1, COMPUTER_MARKER);
        board.placeMarker(2, COMPUTER_MARKER);
        board.placeMarker(7, OPPONENT_MARKER);
        board.placeMarker(8, OPPONENT_MARKER);
        board.placeMarker(5, OPPONENT_MARKER);   // center filler

        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertEquals(3, cell, "Computer should win rather than block");
    }

    // ── Rule 5: random empty cell ────────────────────────────────────────────

    @Test
    void rule5_noSpecialCondition_choosesEmptyCell() {
        // Scatter a few marks with no winning/blocking opportunity
        board.placeMarker(1, OPPONENT_MARKER);
        board.placeMarker(5, COMPUTER_MARKER);
        board.placeMarker(9, OPPONENT_MARKER);

        int cell = computer.chooseCell(board, OPPONENT_MARKER);

        // Must be empty (not already taken)
        char v = board.getCellValue(cell);
        assertNotEquals('X', v, "Chosen cell should be empty");
        assertNotEquals('O', v, "Chosen cell should be empty");
    }

    @Test
    void rule5_choosesValidCellNumber() {
        board.placeMarker(1, OPPONENT_MARKER);
        board.placeMarker(5, COMPUTER_MARKER);
        board.placeMarker(9, OPPONENT_MARKER);

        int cell = computer.chooseCell(board, OPPONENT_MARKER);
        assertTrue(cell >= 1 && cell <= 9, "Chosen cell must be in range 1–9");
    }

    // ── chooseCell does not mutate the board ──────────────────────────────────

    @Test
    void chooseCell_neverModifiesBoard() {
        board.placeMarker(1, OPPONENT_MARKER);
        board.placeMarker(2, OPPONENT_MARKER);   // opponent threatens top row

        // Capture every cell value before
        char[] before = new char[9];
        for (int i = 1; i <= 9; i++) before[i - 1] = board.getCellValue(i);

        computer.chooseCell(board, OPPONENT_MARKER);

        // All values must match after
        for (int i = 1; i <= 9; i++) {
            assertEquals(before[i - 1], board.getCellValue(i),
                    "Cell " + i + " was modified by chooseCell");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean isCorner(int cell) {
        return cell == 1 || cell == 3 || cell == 7 || cell == 9;
    }
}
