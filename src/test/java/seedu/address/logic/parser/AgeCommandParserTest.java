package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AgeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Age;

/**
 * Tests for {@link AgeCommandParser}.
 */
public class AgeCommandParserTest {

    private final AgeCommandParser parser = new AgeCommandParser();

    @Test
    public void parse_valid_noSpaces_success() throws Exception {
        AgeCommand cmd = parser.parse("1 age/25");
        assertEquals(new AgeCommand(Index.fromOneBased(1), new Age("25")), cmd);
    }

    @Test
    public void parse_valid_withIncidentalSpaces_success() throws Exception {
        AgeCommand cmd1 = parser.parse("1 age/ 25");
        assertEquals(new AgeCommand(Index.fromOneBased(1), new Age("25")), cmd1);

        AgeCommand cmd2 = parser.parse("1 age/25  ");
        assertEquals(new AgeCommand(Index.fromOneBased(1), new Age("25")), cmd2);
    }

    @Test
    public void parse_clearWithEmptyValue_success() throws Exception {
        AgeCommand cmd = parser.parse("1 age/");
        // null age represents clearing the field
        assertEquals(new AgeCommand(Index.fromOneBased(1), null), cmd);
    }

    @Test
    public void parse_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 age/abc"));
        assertThrows(ParseException.class, () -> parser.parse("1 age/0"));
        assertThrows(ParseException.class, () -> parser.parse("1 age/121"));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 25"));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("0 age/25"));
        assertThrows(ParseException.class, () -> parser.parse("-1 age/25"));
        assertThrows(ParseException.class, () -> parser.parse("xyz age/25"));
    }
}


