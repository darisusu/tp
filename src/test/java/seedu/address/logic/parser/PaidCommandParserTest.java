package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Paid;

/**
 * Tests for {@link PaidCommandParser}.
 */
public class PaidCommandParserTest {

    private final PaidCommandParser parser = new PaidCommandParser();

    @Test
    public void parse_validValues_success() throws Exception {
        PaidCommand trueCommand = parser.parse("1 paid/true");
        assertEquals(new PaidCommand(Index.fromOneBased(1), new Paid("true")), trueCommand);

        PaidCommand spaced = parser.parse("2 paid/ True ");
        assertEquals(new PaidCommand(Index.fromOneBased(2), new Paid("True")), spaced);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 true"));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("0 paid/true"));
        assertThrows(ParseException.class, () -> parser.parse("-1 paid/false"));
        assertThrows(ParseException.class, () -> parser.parse("abc paid/true"));
    }

    @Test
    public void parse_invalidPaidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 paid/yes"));
        assertThrows(ParseException.class, () -> parser.parse("1 paid/"));
        assertThrows(ParseException.class, () -> parser.parse("1 paid/tru"));
    }
}
