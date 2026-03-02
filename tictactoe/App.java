import java.util.Scanner;

/**
 * App.java — the entry point and sole I/O layer.
 *
 * All game logic lives in Game, Board, Player, and InputValidator.
 * This class only reads from the console and writes to it.
 *
 * Compile:  javac *.java
 * Run:      java App
 */
public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Player playerOne = new Player("Player 1", 'X');
        Player playerTwo = new Player("Player 2", 'O');
        Game   game      = new Game(playerOne, playerTwo);

        printWelcome();

        boolean keepPlaying = true;
        while (keepPlaying) {
            playOneGame(game, scanner);
            keepPlaying = askPlayAgain(scanner);
            if (keepPlaying) {
                game.reset();
                System.out.println("\n--- New game started! ---");
            }
        }

        System.out.println("\nThanks for playing! Goodbye.\n");
        scanner.close();
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
