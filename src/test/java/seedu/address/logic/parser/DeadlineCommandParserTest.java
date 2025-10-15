package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeadlineCommand;
import seedu.address.model.person.Deadline;

public class DeadlineCommandParserTest {
    private DeadlineCommandParser parser = new DeadlineCommandParser();
    private final String nonEmptyDeadline = "2024-12-31";

    @Test
    public void parse_indexSpecified_success() {
        // have Deadline
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_DEADLINE + nonEmptyDeadline;
        DeadlineCommand expectedCommand = new DeadlineCommand(INDEX_FIRST_PERSON, Deadline.fromString(nonEmptyDeadline));
        assertParseSuccess(parser, userInput, expectedCommand);

        // no Deadline
        userInput = targetIndex.getOneBased() + " " + PREFIX_DEADLINE;
        expectedCommand = new DeadlineCommand(INDEX_FIRST_PERSON, Deadline.fromString(""));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeadlineCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, DeadlineCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, DeadlineCommand.COMMAND_WORD + " " + nonEmptyDeadline, expectedMessage);
    }
}
