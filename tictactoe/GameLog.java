import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * GameLog.java — tracks match history and persists it to disk.
 *
 * Responsibilities:
 *   - Count wins for each player and total draws
 *   - Store a per-round summary for the saved file
 *   - Write a formatted report to "game_log.txt" on request
 */
public class GameLog {

    // ── Inner record ─────────────────────────────────────────────────────────

    /** Immutable summary of one completed round. */
    private static class RoundResult {
        final int    roundNumber;
        final String outcome;   // e.g. "Player 1 (X) wins" or "Draw"

        RoundResult(int roundNumber, String outcome) {
            this.roundNumber = roundNumber;
            this.outcome     = outcome;
        }
    }

    // ── Fields ───────────────────────────────────────────────────────────────

    private final Player playerOne;
    private final Player playerTwo;

    private int p1Wins;
    private int p2Wins;
    private int draws;

    private final List<RoundResult> rounds = new ArrayList<>();

    // ── Constructor ──────────────────────────────────────────────────────────

    public GameLog(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    // ── Recording results ─────────────────────────────────────────────────────

    /**
     * Records the outcome of a completed round.
     *
     * @param status the final Game.Status (must not be IN_PROGRESS)
     */
    public void record(Game.Status status) {
        int roundNumber = rounds.size() + 1;
        String outcome;

        switch (status) {
            case PLAYER_ONE_WINS:
                p1Wins++;
                outcome = playerOne + " wins";
                break;
            case PLAYER_TWO_WINS:
                p2Wins++;
                outcome = playerTwo + " wins";
                break;
            default:
                draws++;
                outcome = "Draw";
                break;
        }

        rounds.add(new RoundResult(roundNumber, outcome));
    }

    // ── Display ───────────────────────────────────────────────────────────────

    /**
     * Returns a nicely formatted statistics summary for console output.
     */
    public String getSummary() {
        int totalRounds = rounds.size();
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════╗\n");
        sb.append("║          SCOREBOARD              ║\n");
        sb.append("╠══════════════════════════════════╣\n");
        sb.append(String.format("║  %-20s  %5d win%-2s║%n",
                playerOne.toString(), p1Wins, p1Wins == 1 ? " " : "s"));
        sb.append(String.format("║  %-20s  %5d win%-2s║%n",
                playerTwo.toString(), p2Wins, p2Wins == 1 ? " " : "s"));
        sb.append(String.format("║  %-20s  %5d tie%-2s║%n",
                "Draws", draws, draws == 1 ? " " : "s"));
        sb.append("╠══════════════════════════════════╣\n");
        sb.append(String.format("║  Rounds played: %-17d║%n", totalRounds));
        sb.append("╚══════════════════════════════════╝");
        return sb.toString();
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    /**
     * Writes the full game log to "game_log.txt" in the working directory.
     *
     * @return the path of the file written, or an error message
     */
    public String saveToFile() {
        String filename = "game_log.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            pw.println("========================================");
            pw.println("         TIC-TAC-TOE GAME LOG          ");
            pw.println("========================================");
            pw.println("Session saved: " + timestamp);
            pw.println();
            pw.println("Players");
            pw.println("  " + playerOne);
            pw.println("  " + playerTwo);
            pw.println();
            pw.println("Round-by-Round Results");
            pw.println("----------------------------------------");
            if (rounds.isEmpty()) {
                pw.println("  No rounds were completed.");
            } else {
                for (RoundResult r : rounds) {
                    pw.printf("  Round %2d : %s%n", r.roundNumber, r.outcome);
                }
            }
            pw.println();
            pw.println("Final Totals");
            pw.println("----------------------------------------");
            pw.printf("  %-22s %d win%s%n",
                    playerOne + ":", p1Wins, p1Wins == 1 ? "" : "s");
            pw.printf("  %-22s %d win%s%n",
                    playerTwo + ":", p2Wins, p2Wins == 1 ? "" : "s");
            pw.printf("  %-22s %d%n", "Draws:", draws);
            pw.printf("  %-22s %d%n", "Total rounds played:", rounds.size());
            pw.println("========================================");
        } catch (IOException e) {
            return "ERROR: Could not save game log — " + e.getMessage();
        }
        return filename;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int getP1Wins() { return p1Wins; }
    public int getP2Wins() { return p2Wins; }
    public int getDraws()  { return draws;  }
}
