package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEIGHT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.HeightCommand;
import seedu.address.model.person.Height;

public class HeightCommandParserTest {
    private final HeightCommandParser parser = new HeightCommandParser();
    private static final String VALID_HEIGHT = "170";

    @Test
    public void parse_indexSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_HEIGHT + VALID_HEIGHT;
        HeightCommand expectedCommand = new HeightCommand(INDEX_FIRST_PERSON, new Height(VALID_HEIGHT));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValue_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_HEIGHT + "-10";
        assertParseFailure(parser, userInput, Height.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, HeightCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, HeightCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, HeightCommand.COMMAND_WORD + " " + PREFIX_HEIGHT + VALID_HEIGHT, expectedMessage);

        // no height prefix
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " " + VALID_HEIGHT, expectedMessage);
    }

}
