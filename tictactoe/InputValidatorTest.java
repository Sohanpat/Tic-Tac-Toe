import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InputValidator.java
 *
 * Covers every validation rule described in the assignment:
 * null, empty, whitespace-only, valid digits, out-of-range,
 * decimals, letters, symbols, negative numbers, play-again parsing,
 * and menu-choice parsing.
 */
class InputValidatorTest {

    // ── parseCellNumber – valid inputs ────────────────────────────────────────

    @Test
    void parseCellNumber_1through9_returnsCorrectValue() {
        for (int i = 1; i <= 9; i++) {
            assertEquals(i, InputValidator.parseCellNumber(String.valueOf(i)),
                "Expected " + i + " for input \"" + i + "\"");
        }
    }

    @Test
    void parseCellNumber_leadingAndTrailingWhitespace_trimmedAndParsed() {
        assertEquals(5, InputValidator.parseCellNumber("  5  "));
    }

    // ── parseCellNumber – out-of-range ────────────────────────────────────────

    @Test
    void parseCellNumber_zero_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("0"));
    }

    @Test
    void parseCellNumber_ten_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("10"));
    }

    @Test
    void parseCellNumber_largeNumber_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("999"));
    }

    // ── parseCellNumber – null / blank ────────────────────────────────────────

    @Test
    void parseCellNumber_null_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber(null));
    }

    @Test
    void parseCellNumber_emptyString_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber(""));
    }

    @Test
    void parseCellNumber_whitespaceOnly_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("   "));
    }

    // ── parseCellNumber – letters / symbols / decimals ────────────────────────

    @Test
    void parseCellNumber_letters_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("abc"));
    }

    @Test
    void parseCellNumber_singleLetter_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("a"));
    }

    @Test
    void parseCellNumber_decimalNumber_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("2.5"));
    }

    @Test
    void parseCellNumber_decimalWithNoFraction_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("3.0"));
    }

    @Test
    void parseCellNumber_negativeNumber_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("-1"));
    }

    @Test
    void parseCellNumber_specialSymbols_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("!@#"));
    }

    @Test
    void parseCellNumber_digitWithSymbol_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseCellNumber("5!"));
    }

    @Test
    void parseCellNumber_justEnterNoInput_returnsNegativeOne() {
        // Simulates user pressing Enter with nothing typed
        assertEquals(-1, InputValidator.parseCellNumber("\n"));
    }

    // ── parsePlayAgain – yes variants ────────────────────────────────────────

    @Test
    void parsePlayAgain_lowercase_y_returnsTrue() {
        assertEquals(Boolean.TRUE, InputValidator.parsePlayAgain("y"));
    }

    @Test
    void parsePlayAgain_uppercase_Y_returnsTrue() {
        assertEquals(Boolean.TRUE, InputValidator.parsePlayAgain("Y"));
    }

    @Test
    void parsePlayAgain_lowercase_yes_returnsTrue() {
        assertEquals(Boolean.TRUE, InputValidator.parsePlayAgain("yes"));
    }

    @Test
    void parsePlayAgain_uppercase_YES_returnsTrue() {
        assertEquals(Boolean.TRUE, InputValidator.parsePlayAgain("YES"));
    }

    @Test
    void parsePlayAgain_yes_withWhitespace_returnsTrue() {
        assertEquals(Boolean.TRUE, InputValidator.parsePlayAgain("  yes  "));
    }

    // ── parsePlayAgain – no variants ──────────────────────────────────────────

    @Test
    void parsePlayAgain_lowercase_n_returnsFalse() {
        assertEquals(Boolean.FALSE, InputValidator.parsePlayAgain("n"));
    }

    @Test
    void parsePlayAgain_uppercase_N_returnsFalse() {
        assertEquals(Boolean.FALSE, InputValidator.parsePlayAgain("N"));
    }

    @Test
    void parsePlayAgain_lowercase_no_returnsFalse() {
        assertEquals(Boolean.FALSE, InputValidator.parsePlayAgain("no"));
    }

    @Test
    void parsePlayAgain_no_withWhitespace_returnsFalse() {
        assertEquals(Boolean.FALSE, InputValidator.parsePlayAgain("  no  "));
    }

    // ── parsePlayAgain – invalid ──────────────────────────────────────────────

    @Test
    void parsePlayAgain_null_returnsNull() {
        assertNull(InputValidator.parsePlayAgain(null));
    }

    @Test
    void parsePlayAgain_empty_returnsNull() {
        assertNull(InputValidator.parsePlayAgain(""));
    }

    @Test
    void parsePlayAgain_whitespaceOnly_returnsNull() {
        assertNull(InputValidator.parsePlayAgain("   "));
    }

    @Test
    void parsePlayAgain_randomWord_returnsNull() {
        assertNull(InputValidator.parsePlayAgain("maybe"));
    }

    @Test
    void parsePlayAgain_number_returnsNull() {
        assertNull(InputValidator.parsePlayAgain("1"));
    }

    @Test
    void parsePlayAgain_symbols_returnsNull() {
        assertNull(InputValidator.parsePlayAgain("!?"));
    }

    // ── parseMenuChoice – valid inputs ────────────────────────────────────────

    @Test
    void parseMenuChoice_validChoice_returnsValue() {
        assertEquals(1, InputValidator.parseMenuChoice("1", 2));
        assertEquals(2, InputValidator.parseMenuChoice("2", 2));
    }

    @Test
    void parseMenuChoice_whitespace_trimmedAndParsed() {
        assertEquals(1, InputValidator.parseMenuChoice("  1  ", 2));
    }

    @Test
    void parseMenuChoice_maxOption_returnsValue() {
        assertEquals(5, InputValidator.parseMenuChoice("5", 5));
    }

    // ── parseMenuChoice – out-of-range ───────────────────────────────────────

    @Test
    void parseMenuChoice_zero_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("0", 2));
    }

    @Test
    void parseMenuChoice_aboveMax_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("3", 2));
    }

    @Test
    void parseMenuChoice_largeNumber_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("999", 2));
    }

    // ── parseMenuChoice – null / blank ───────────────────────────────────────

    @Test
    void parseMenuChoice_null_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice(null, 2));
    }

    @Test
    void parseMenuChoice_emptyString_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("", 2));
    }

    @Test
    void parseMenuChoice_whitespaceOnly_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("   ", 2));
    }

    // ── parseMenuChoice – letters / symbols ──────────────────────────────────

    @Test
    void parseMenuChoice_letters_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("abc", 2));
    }

    @Test
    void parseMenuChoice_negativeNumber_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("-1", 2));
    }

    @Test
    void parseMenuChoice_decimal_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("1.0", 2));
    }

    @Test
    void parseMenuChoice_symbols_returnsNegativeOne() {
        assertEquals(-1, InputValidator.parseMenuChoice("!!", 2));
    }
}
