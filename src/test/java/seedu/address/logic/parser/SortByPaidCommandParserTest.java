package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortByPaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class SortByPaidCommandParserTest {

    private SortByPaidCommandParser parser = new SortByPaidCommandParser();

    @Test
    public void parse_emptyArgs_returnsSortByPaidCommand() throws Exception {
        SortByPaidCommand expectedCommand = new SortByPaidCommand();
        SortByPaidCommand actualCommand = parser.parse("");
        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    public void parse_whitespaceOnly_returnsSortByPaidCommand() throws Exception {
        SortByPaidCommand expectedCommand = new SortByPaidCommand();
        SortByPaidCommand actualCommand = parser.parse("   ");
        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    public void parse_nonEmptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("some arguments"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("paid"));
    }
}
