package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.WeightCommand;
import seedu.address.model.person.Weight;

public class WeightCommandParserTest {
    private final WeightCommandParser parser = new WeightCommandParser();
    private static final String VALID_WEIGHT = "70";

    @Test
    public void parse_indexSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_WEIGHT + VALID_WEIGHT;
        WeightCommand expectedCommand = new WeightCommand(INDEX_FIRST_PERSON, new Weight(VALID_WEIGHT));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValue_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_WEIGHT + "0";
        assertParseFailure(parser, userInput, Weight.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, WeightCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, WeightCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, WeightCommand.COMMAND_WORD + " " + PREFIX_WEIGHT + VALID_WEIGHT, expectedMessage);

        // no weight prefix
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " " + VALID_WEIGHT, expectedMessage);
    }
}
