package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SessionCommand;
import seedu.address.model.person.Session;

public class SessionCommandParserTest {
    private static final String VALID_SESSION = "WEEKLY:MON-1800-1900";
    private final SessionCommandParser parser = new SessionCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_SESSION + VALID_SESSION;
        SessionCommand expectedCommand = new SessionCommand(INDEX_FIRST_PERSON, Session.fromString(VALID_SESSION));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValue_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_SESSION + "2025/01/01 25:00";
        assertParseFailure(parser, userInput, Session.MESSAGE_CONSTRAINTS_FORMAT);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SessionCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, SessionCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, SessionCommand.COMMAND_WORD + " " + PREFIX_SESSION + VALID_SESSION, expectedMessage);

        // no session prefix
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " " + VALID_SESSION, expectedMessage);
    }
}
