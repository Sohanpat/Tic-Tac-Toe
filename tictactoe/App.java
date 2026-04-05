import java.util.Scanner;

/**
 * App.java — the entry point and sole I/O layer.
 *
 * All game logic lives in Game, Board, Player, and InputValidator.
 * Statistics tracking lives in GameLog.
 * This class only reads from the console and writes to it.
 *
 * Compile:  javac *.java
 * Run:      java App
 */
public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Player  playerOne = new Player("Player 1", 'X');
        Player  playerTwo = new Player("Player 2", 'O');
        Game    game      = new Game(playerOne, playerTwo);
        GameLog log       = new GameLog(playerOne, playerTwo);

        printWelcome();

        boolean keepPlaying = true;
        while (keepPlaying) {
            playOneGame(game, scanner);

            // Record the result and show the updated scoreboard
            log.record(game.getStatus());
            System.out.println(log.getSummary());
            System.out.println();

            keepPlaying = askPlayAgain(scanner);
            if (keepPlaying) {
                // Loser goes first next round; draws keep the same starting order
                Player nextFirst = determineNextFirstPlayer(game);
                game.resetWithFirstPlayer(nextFirst);
                System.out.println("\n--- New game started! " + nextFirst + " goes first. ---");
            }
        }

        // Save game log to disk before exiting
        String savedPath = log.saveToFile();
        System.out.println("\nGame log saved to: " + savedPath);
        System.out.println("Thanks for playing! Goodbye.\n");
        scanner.close();
    }

    // ── Turn order helper ─────────────────────────────────────────────────────

    /**
     * Returns the player who should go first in the next round.
     * The loser goes first; on a draw the current starting player is unchanged
     * (which defaults back to Player 1 via the existing reset logic).
     */
    private static Player determineNextFirstPlayer(Game game) {
        switch (game.getStatus()) {
            case PLAYER_ONE_WINS:
                return game.getPlayerTwo();   // P2 lost → P2 goes first
            case PLAYER_TWO_WINS:
                return game.getPlayerOne();   // P1 lost → P1 goes first
            default:
                // Draw — keep Player 1 as the default first player
                return game.getPlayerOne();
        }
    }

    // ── Game loop ─────────────────────────────────────────────────────────────

    private static void playOneGame(Game game, Scanner scanner) {
        System.out.println(game.getBoard());

        while (!game.isOver()) {
            Player current = game.getCurrentPlayer();
            System.out.print(current + " — enter a cell (1-9): ");

            String raw  = scanner.nextLine();
            int    cell = InputValidator.parseCellNumber(raw);

            if (cell == -1) {
                System.out.println("  ! Invalid input. Please enter a whole number between 1 and 9.");
                System.out.println(game.getBoard());
                continue;
            }

            boolean accepted = game.takeTurn(cell);
            if (!accepted) {
                System.out.println("  ! Cell " + cell + " is already taken. Choose an empty cell.");
                System.out.println(game.getBoard());
                continue;
            }

            System.out.println(game.getBoard());
        }

        printResult(game);
    }

    // ── Output helpers ────────────────────────────────────────────────────────

    private static void printWelcome() {
        System.out.println("================================");
        System.out.println("       Welcome to Tic-Tac-Toe  ");
        System.out.println("    Player 1 = X  |  Player 2 = O");
        System.out.println("================================");
        System.out.println("Cells are numbered 1-9:");
        System.out.println("   1 | 2 | 3");
        System.out.println("  ---+---+---");
        System.out.println("   4 | 5 | 6");
        System.out.println("  ---+---+---");
        System.out.println("   7 | 8 | 9");
        System.out.println();
    }

    private static void printResult(Game game) {
        switch (game.getStatus()) {
            case PLAYER_ONE_WINS:
                System.out.println("*** " + game.getPlayerOne() + " wins! Congratulations! ***\n");
                break;
            case PLAYER_TWO_WINS:
                System.out.println("*** " + game.getPlayerTwo() + " wins! Congratulations! ***\n");
                break;
            case DRAW:
                System.out.println("*** It's a draw! Well played by both. ***\n");
                break;
            default:
                break;
        }
    }

    private static boolean askPlayAgain(Scanner scanner) {
        while (true) {
            System.out.print("Play again? (y/n): ");
            Boolean answer = InputValidator.parsePlayAgain(scanner.nextLine());
            if (answer != null) return answer;
            System.out.println("  ! Please enter 'y' or 'n'.");
        }
    }
}
