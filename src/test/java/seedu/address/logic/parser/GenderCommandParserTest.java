package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.GenderCommand;
import seedu.address.model.person.Gender;

public class GenderCommandParserTest {
    private final GenderCommandParser parser = new GenderCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " " + PREFIX_GENDER + "male";
        GenderCommand expected = new GenderCommand(index, new Gender("male"));
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_withIncidentalSpaces_success() {
        Index index = INDEX_FIRST_PERSON;
        String input1 = index.getOneBased() + " " + PREFIX_GENDER + "  Female";
        String input2 = index.getOneBased() + " " + PREFIX_GENDER + "nb  ";
        assertParseSuccess(parser, input1, new GenderCommand(index, new Gender("female")));
        assertParseSuccess(parser, input2, new GenderCommand(index, new Gender("nb")));
    }

    @Test
    public void parse_invalidValue_failure() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " " + PREFIX_GENDER + "unknown";
        assertParseFailure(parser, input, Gender.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, GenderCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, GenderCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, GenderCommand.COMMAND_WORD + " " + PREFIX_GENDER + "male", expectedMessage);

        // no gender prefix
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " male", expectedMessage);
    }
}


