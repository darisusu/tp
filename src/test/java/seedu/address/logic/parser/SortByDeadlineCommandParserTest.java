package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortByDeadlineCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class SortByDeadlineCommandParserTest {

    private final SortByDeadlineCommandParser parser = new SortByDeadlineCommandParser();

    @Test
    public void parse_empty_returnsAscending() throws Exception {
        SortByDeadlineCommand cmd = parser.parse("");
        // equals() checks the ascending flag
        assertEquals(new SortByDeadlineCommand(true), cmd);
    }

    @Test
    public void parse_asc_returnsAscending() throws Exception {
        assertEquals(new SortByDeadlineCommand(true), parser.parse("asc"));
        assertEquals(new SortByDeadlineCommand(true), parser.parse("  ASC  "));
    }

    @Test
    public void parse_desc_returnsDescending() throws Exception {
        assertEquals(new SortByDeadlineCommand(false), parser.parse("desc"));
        assertEquals(new SortByDeadlineCommand(false), parser.parse("  DeSc  "));
    }

    @Test
    public void parse_invalid_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("fastest"));
        assertThrows(ParseException.class, () -> parser.parse("ascending please"));
        assertThrows(ParseException.class, () -> parser.parse("1"));
    }
}
