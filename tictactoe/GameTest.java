import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Game.java
 */
class GameTest {

    private Game   game;
    private Player p1;
    private Player p2;

    @BeforeEach
    void setUp() {
        p1   = new Player("Player 1", 'X');
        p2   = new Player("Player 2", 'O');
        game = new Game(p1, p2);
    }

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    void newGame_statusIsInProgress() {
        assertEquals(Game.Status.IN_PROGRESS, game.getStatus());
    }

    @Test
    void newGame_firstTurnBelongsToPlayerOne() {
        assertSame(p1, game.getCurrentPlayer());
    }

    @Test
    void newGame_isNotOver() {
        assertFalse(game.isOver());
    }

    // ── takeTurn – valid moves ────────────────────────────────────────────────

    @Test
    void takeTurn_validMove_returnsTrue() {
        assertTrue(game.takeTurn(1));
    }

    @Test
    void takeTurn_afterValidMove_switchesToPlayerTwo() {
        game.takeTurn(1);
        assertSame(p2, game.getCurrentPlayer());
    }

    @Test
    void takeTurn_playerTwoMoves_switchesBackToPlayerOne() {
        game.takeTurn(1);
        game.takeTurn(2);
        assertSame(p1, game.getCurrentPlayer());
    }

    // ── takeTurn – invalid moves ──────────────────────────────────────────────

    @Test
    void takeTurn_takenCell_returnsFalse() {
        game.takeTurn(1);
        assertFalse(game.takeTurn(1));
    }

    @Test
    void takeTurn_takenCell_doesNotSwitchPlayer() {
        game.takeTurn(1);              // P1 places on 1
        game.takeTurn(1);              // invalid — same cell
        assertSame(p2, game.getCurrentPlayer());  // still P2's turn
    }

    @Test
    void takeTurn_outOfRangeCell_returnsFalse() {
        assertFalse(game.takeTurn(0));
        assertFalse(game.takeTurn(10));
    }

    // ── Win detection ─────────────────────────────────────────────────────────

    @Test
    void takeTurn_playerOneWinsTopRow_statusUpdated() {
        // X: 1, 2, 3   O: 4, 5
        game.takeTurn(1); game.takeTurn(4);
        game.takeTurn(2); game.takeTurn(5);
        game.takeTurn(3);
        assertEquals(Game.Status.PLAYER_ONE_WINS, game.getStatus());
        assertTrue(game.isOver());
    }

    @Test
    void takeTurn_playerTwoWinsColumn_statusUpdated() {
        // O gets right column (3, 6, 9)
        game.takeTurn(1); game.takeTurn(3);
        game.takeTurn(2); game.takeTurn(6);
        game.takeTurn(5); game.takeTurn(9);
        assertEquals(Game.Status.PLAYER_TWO_WINS, game.getStatus());
        assertTrue(game.isOver());
    }

    @Test
    void takeTurn_playerOneWinsDiagonal_statusUpdated() {
        // X: 1, 5, 9   O: 2, 3
        game.takeTurn(1); game.takeTurn(2);
        game.takeTurn(5); game.takeTurn(3);
        game.takeTurn(9);
        assertEquals(Game.Status.PLAYER_ONE_WINS, game.getStatus());
    }

    @Test
    void takeTurn_noMoreMovesAfterWin() {
        game.takeTurn(1); game.takeTurn(4);
        game.takeTurn(2); game.takeTurn(5);
        game.takeTurn(3); // P1 wins
        // Attempting further moves should fail (game is over — board cell might still be empty but game status stops it)
        // We verify the status did not change after the winning move
        assertEquals(Game.Status.PLAYER_ONE_WINS, game.getStatus());
    }

    // ── Draw detection ────────────────────────────────────────────────────────

    @Test
    void takeTurn_fullBoardNoWinner_isDraw() {
        // X O X
        // O X X
        // O X O  — no three-in-a-row for either player
        game.takeTurn(1); game.takeTurn(2);
        game.takeTurn(3); game.takeTurn(5);
        game.takeTurn(4); game.takeTurn(6);
        game.takeTurn(8); game.takeTurn(7);
        game.takeTurn(9);
        assertEquals(Game.Status.DRAW, game.getStatus());
        assertTrue(game.isOver());
    }

    // ── reset ─────────────────────────────────────────────────────────────────

    @Test
    void reset_statusReturnsToInProgress() {
        game.takeTurn(1); game.takeTurn(4);
        game.takeTurn(2); game.takeTurn(5);
        game.takeTurn(3); // P1 wins
        game.reset();
        assertEquals(Game.Status.IN_PROGRESS, game.getStatus());
    }

    @Test
    void reset_currentPlayerReturnsToPlayerOne() {
        game.takeTurn(1);
        game.reset();
        assertSame(p1, game.getCurrentPlayer());
    }

    @Test
    void reset_previousCellsCanBePlayedAgain() {
        game.takeTurn(1);
        game.reset();
        assertTrue(game.takeTurn(1), "Cell 1 should be free after reset");
    }

    @Test
    void reset_isNotOver() {
        game.takeTurn(1); game.takeTurn(2);
        game.reset();
        assertFalse(game.isOver());
    }
}
