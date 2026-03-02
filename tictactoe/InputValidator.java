/**
 * Stateless utility class that validates and parses all console input.
 *
 * Keeping this logic here (rather than in App.java) makes it easy to
 * unit-test every edge case without touching the game loop.
 */
public class InputValidator {

    // Not meant to be instantiated
    private InputValidator() {}

    // ── Cell number ──────────────────────────────────────────────────────────

    /**
     * Parses a raw string into a valid cell number (1–9).
     *
     * Rejects: null, blank, whitespace-only, decimals, letters,
     *          symbols, negative numbers, and out-of-range values.
     *
     * @return the integer 1–9 on success, or -1 on any invalid input
     */
    public static int parseCellNumber(String input) {
        if (input == null || input.trim().isEmpty()) return -1;

        String trimmed = input.trim();

        // Must be purely digits (no minus sign, no dot, no letters)
        if (!trimmed.matches("[0-9]+")) return -1;

        try {
            int value = Integer.parseInt(trimmed);
            return (value >= 1 && value <= 9) ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ── Play-again prompt ────────────────────────────────────────────────────

    /**
     * Parses the user's response to "play again?" (y/yes/n/no, case-insensitive).
     *
     * @return Boolean.TRUE for yes, Boolean.FALSE for no,
     *         or null if the input is unrecognised (caller should re-prompt)
     */
    public static Boolean parsePlayAgain(String input) {
        if (input == null || input.trim().isEmpty()) return null;

        String s = input.trim().toLowerCase();
        if (s.equals("y") || s.equals("yes")) return Boolean.TRUE;
        if (s.equals("n") || s.equals("no"))  return Boolean.FALSE;
        return null;
    }
}
