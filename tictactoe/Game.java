/**
 * Manages all game logic for a single round of tic-tac-toe.
 *
 * Responsibilities:
 *   - Track whose turn it is
 *   - Delegate moves to the Board
 *   - Detect wins and draws
 *   - Expose game status via the Status enum
 *
 * Does NOT handle any I/O — that lives in App.java.
 */
public class Game {

    // ── Status enum ──────────────────────────────────────────────────────────

    public enum Status {
        IN_PROGRESS,
        PLAYER_ONE_WINS,
        PLAYER_TWO_WINS,
        DRAW
    }

    // ── Fields ───────────────────────────────────────────────────────────────

    private final Board  board;
    private final Player playerOne;
    private final Player playerTwo;

    private Player currentPlayer;
    private Status status;

    // ── Constructor ──────────────────────────────────────────────────────────

    public Game(Player playerOne, Player playerTwo) {
        this.board     = new Board();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.currentPlayer = playerOne;
        this.status    = Status.IN_PROGRESS;
    }

    // ── Core action ──────────────────────────────────────────────────────────

    /**
     * Attempts to place the current player's marker on the given cell.
     *
     * @param cellNumber 1–9
     * @return true if the move was valid and applied; false if it was rejected
     */
    public boolean takeTurn(int cellNumber) {
        if (!board.placeMarker(cellNumber, currentPlayer.getMarker())) {
            return false;   // invalid move — caller should re-prompt
        }
        updateStatus();
        if (status == Status.IN_PROGRESS) {
            switchPlayer();
        }
        return true;
    }

    // ── Reset ────────────────────────────────────────────────────────────────

    /** Resets the board and game state so a new round can begin. */
    public void reset() {
        board.reset();
        currentPlayer = playerOne;
        status        = Status.IN_PROGRESS;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public boolean isOver()          { return status != Status.IN_PROGRESS; }
    public Status  getStatus()       { return status;        }
    public Player  getCurrentPlayer(){ return currentPlayer; }
    public Board   getBoard()        { return board;         }
    public Player  getPlayerOne()    { return playerOne;     }
    public Player  getPlayerTwo()    { return playerTwo;     }

    // ── Private helpers ──────────────────────────────────────────────────────

    private void updateStatus() {
        if      (board.hasWon(playerOne.getMarker())) status = Status.PLAYER_ONE_WINS;
        else if (board.hasWon(playerTwo.getMarker())) status = Status.PLAYER_TWO_WINS;
        else if (board.isFull())                       status = Status.DRAW;
        // otherwise stays IN_PROGRESS
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == playerOne) ? playerTwo : playerOne;
    }
}
