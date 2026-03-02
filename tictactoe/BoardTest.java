import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Board.java
 *
 * Compile (with JUnit JAR in lib/):
 *   javac -cp lib/junit-platform-console-standalone.jar *.java
 * Run:
 *   java -jar lib/junit-platform-console-standalone.jar --class-path . --select-class BoardTest
 */
class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    void newBoard_cellsAreNumbered1Through9() {
        for (int i = 1; i <= 9; i++) {
            assertEquals((char)('0' + i), board.getCellValue(i),
                "Cell " + i + " should start with its digit label");
        }
    }

    @Test
    void newBoard_isNotFull() {
        assertFalse(board.isFull());
    }

    @Test
    void newBoard_noOneHasWon() {
        assertFalse(board.hasWon('X'));
        assertFalse(board.hasWon('O'));
    }

    // ── placeMarker – valid moves ─────────────────────────────────────────────

    @Test
    void placeMarker_validCell_returnsTrue() {
        assertTrue(board.placeMarker(5, 'X'));
    }

    @Test
    void placeMarker_validCell_updatesTheCellValue() {
        board.placeMarker(5, 'X');
        assertEquals('X', board.getCellValue(5));
    }

    @Test
    void placeMarker_allNineCells_allAccepted() {
        for (int i = 1; i <= 9; i++) {
            Board fresh = new Board();
            assertTrue(fresh.placeMarker(i, 'X'), "Cell " + i + " should be placeable");
        }
    }

    // ── placeMarker – invalid moves ───────────────────────────────────────────

    @Test
    void placeMarker_cellAlreadyTaken_returnsFalse() {
        board.placeMarker(1, 'X');
        assertFalse(board.placeMarker(1, 'O'));
    }

    @Test
    void placeMarker_cellAlreadyTaken_doesNotOverwrite() {
        board.placeMarker(1, 'X');
        board.placeMarker(1, 'O');
        assertEquals('X', board.getCellValue(1));
    }

    @Test
    void placeMarker_cellNumberZero_returnsFalse() {
        assertFalse(board.placeMarker(0, 'X'));
    }

    @Test
    void placeMarker_cellNumberTen_returnsFalse() {
        assertFalse(board.placeMarker(10, 'X'));
    }

    @Test
    void placeMarker_negativeCell_returnsFalse() {
        assertFalse(board.placeMarker(-1, 'X'));
    }

    // ── hasWon – rows ─────────────────────────────────────────────────────────

    @Test
    void hasWon_topRow_returnsTrue() {
        board.placeMarker(1, 'X');
        board.placeMarker(2, 'X');
        board.placeMarker(3, 'X');
        assertTrue(board.hasWon('X'));
    }

    @Test
    void hasWon_middleRow_returnsTrue() {
        board.placeMarker(4, 'O');
        board.placeMarker(5, 'O');
        board.placeMarker(6, 'O');
        assertTrue(board.hasWon('O'));
    }

    @Test
    void hasWon_bottomRow_returnsTrue() {
        board.placeMarker(7, 'X');
        board.placeMarker(8, 'X');
        board.placeMarker(9, 'X');
        assertTrue(board.hasWon('X'));
    }

    // ── hasWon – columns ──────────────────────────────────────────────────────

    @Test
    void hasWon_leftColumn_returnsTrue() {
        board.placeMarker(1, 'X');
        board.placeMarker(4, 'X');
        board.placeMarker(7, 'X');
        assertTrue(board.hasWon('X'));
    }

    @Test
    void hasWon_middleColumn_returnsTrue() {
        board.placeMarker(2, 'O');
        board.placeMarker(5, 'O');
        board.placeMarker(8, 'O');
        assertTrue(board.hasWon('O'));
    }

    @Test
    void hasWon_rightColumn_returnsTrue() {
        board.placeMarker(3, 'X');
        board.placeMarker(6, 'X');
        board.placeMarker(9, 'X');
        assertTrue(board.hasWon('X'));
    }

    // ── hasWon – diagonals ────────────────────────────────────────────────────

    @Test
    void hasWon_mainDiagonal_returnsTrue() {
        board.placeMarker(1, 'X');
        board.placeMarker(5, 'X');
        board.placeMarker(9, 'X');
        assertTrue(board.hasWon('X'));
    }

    @Test
    void hasWon_antiDiagonal_returnsTrue() {
        board.placeMarker(3, 'O');
        board.placeMarker(5, 'O');
        board.placeMarker(7, 'O');
        assertTrue(board.hasWon('O'));
    }

    // ── hasWon – no win ───────────────────────────────────────────────────────

    @Test
    void hasWon_partialRow_returnsFalse() {
        board.placeMarker(1, 'X');
        board.placeMarker(2, 'X');
        assertFalse(board.hasWon('X'));
    }

    @Test
    void hasWon_mixedRow_returnsFalse() {
        board.placeMarker(1, 'X');
        board.placeMarker(2, 'O');
        board.placeMarker(3, 'X');
        assertFalse(board.hasWon('X'));
        assertFalse(board.hasWon('O'));
    }

    // ── isFull ────────────────────────────────────────────────────────────────

    @Test
    void isFull_allCellsMarked_returnsTrue() {
        char[] marks = {'X','O','X','O','X','O','O','X','O'};
        for (int i = 0; i < 9; i++) board.placeMarker(i + 1, marks[i]);
        assertTrue(board.isFull());
    }

    @Test
    void isFull_oneCellEmpty_returnsFalse() {
        for (int i = 1; i <= 8; i++) board.placeMarker(i, i % 2 == 0 ? 'O' : 'X');
        assertFalse(board.isFull());
    }

    // ── reset ─────────────────────────────────────────────────────────────────

    @Test
    void reset_clearsAllMarkers() {
        board.placeMarker(1, 'X');
        board.placeMarker(9, 'O');
        board.reset();
        assertEquals('1', board.getCellValue(1));
        assertEquals('9', board.getCellValue(9));
    }

    @Test
    void reset_boardIsNoLongerFull() {
        char[] marks = {'X','O','X','O','X','O','O','X','O'};
        for (int i = 0; i < 9; i++) board.placeMarker(i + 1, marks[i]);
        board.reset();
        assertFalse(board.isFull());
    }
}
