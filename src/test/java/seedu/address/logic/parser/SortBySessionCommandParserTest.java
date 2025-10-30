package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortBySessionCommand;

/**
 * Unit tests for {@link SortBySessionCommandParser}.
 */
public class SortBySessionCommandParserTest {

    private final SortBySessionCommandParser parser = new SortBySessionCommandParser();

    @Test
    public void parse_emptyArgs_success() {
        // empty string
        assertParseSuccess(parser, "", new SortBySessionCommand());

        // whitespace only
        assertParseSuccess(parser, "   \t  \n  ", new SortBySessionCommand());
    }

    @Test
    public void parse_extraArgs_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SortBySessionCommand.MESSAGE_USAGE);

        // any non-empty trailing text should fail
        assertParseFailure(parser, "desc", expectedMessage);
        assertParseFailure(parser, " --flag", expectedMessage);
        assertParseFailure(parser, "  unexpected tokens here ", expectedMessage);
    }

    @Test
    public void parse_nullArgs_success() {
        // Defensive: parser treats null as empty input
        assertParseSuccess(parser, null, new SortBySessionCommand());
    }
}
