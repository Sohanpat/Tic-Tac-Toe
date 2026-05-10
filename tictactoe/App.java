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

        printWelcome();

        // GameLog is created once per session and shared across all rounds
        GameLog log = null;

        boolean keepPlaying = true;
        while (keepPlaying) {
            Game game = buildGame(scanner);

            // Create the log on the first round, using whoever the players are
            if (log == null) {
                log = new GameLog(game.getPlayerOne(), game.getPlayerTwo());
            }

            playOneGame(game, scanner);
            log.record(game.getStatus());

            System.out.println(log.getSummary());

            keepPlaying = askPlayAgain(scanner);
            if (keepPlaying) {
                game.reset();
                System.out.println("\n--- New game started! ---");
            }
        }

        // Save log to file at the end of the session
        if (log != null) {
            String saved = log.saveToFile();
            System.out.println("Game log saved to: " + saved);
        }

        System.out.println("\nThanks for playing! Goodbye.\n");
        scanner.close();
    }

    // ── Pre-game menu ─────────────────────────────────────────────────────────

    private static Game buildGame(Scanner scanner) {
        int modeChoice = askMenuChoice(
                scanner,
                "Choose game mode:\n  1) Human vs Human\n  2) Human vs Computer",
                2
        );

        Player playerOne;
        Player playerTwo;

        if (modeChoice == 1) {
            playerOne = new Player("Player 1", 'X');
            playerTwo = new Player("Player 2", 'O');
        } else {
            int orderChoice = askMenuChoice(
                    scanner,
                    "Should the computer go first or second?\n  1) Computer goes first\n  2) Computer goes second",
                    2
            );

            if (orderChoice == 1) {
                playerOne = new ComputerPlayer("Computer", 'X');
                playerTwo = new Player("Player", 'O');
            } else {
                playerOne = new Player("Player", 'X');
                playerTwo = new ComputerPlayer("Computer", 'O');
            }
        }

        return new Game(playerOne, playerTwo);
    }

    private static int askMenuChoice(Scanner scanner, String prompt, int maxOption) {
        while (true) {
            System.out.println(prompt);
            System.out.print("Enter choice (1-" + maxOption + "): ");
            int choice = InputValidator.parseMenuChoice(scanner.nextLine(), maxOption);
            if (choice != -1) return choice;
            System.out.println("  ! Invalid choice. Please enter a number between 1 and " + maxOption + ".");
        }
    }

    // ── Game loop ─────────────────────────────────────────────────────────────

    private static void playOneGame(Game game, Scanner scanner) {
        System.out.println(game.getBoard());

        while (!game.isOver()) {
            Player current = game.getCurrentPlayer();

            if (current instanceof ComputerPlayer) {
                takeComputerTurn(game);
            } else {
                takeHumanTurn(game, scanner, current);
            }

            System.out.println(game.getBoard());
        }

        printResult(game);
    }

    private static void takeComputerTurn(Game game) {
        Player computer = game.getCurrentPlayer();
        Player opponent = (computer == game.getPlayerOne())
                ? game.getPlayerTwo()
                : game.getPlayerOne();

        int cell = ((ComputerPlayer) computer).chooseCell(game.getBoard(), opponent.getMarker());
        System.out.println(computer + " chooses cell " + cell + ".");
        game.takeTurn(cell);
    }

    private static void takeHumanTurn(Game game, Scanner scanner, Player current) {
        while (true) {
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

            break;
        }
    }

    // ── Output helpers ────────────────────────────────────────────────────────

    private static void printWelcome() {
        System.out.println("================================");
        System.out.println("       Welcome to Tic-Tac-Toe  ");
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
