package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.GoalCommand;
import seedu.address.model.person.Goal;

public class GoalCommandParserTest {
    private GoalCommandParser parser = new GoalCommandParser();
    private final String nonEmptyGoal = "Some goal.";

    @Test
    public void parse_indexSpecified_success() {
        // have goal
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_GOAL + nonEmptyGoal;
        GoalCommand expectedCommand = new GoalCommand(INDEX_FIRST_PERSON, new Goal(nonEmptyGoal));
        assertParseSuccess(parser, userInput, expectedCommand);

        // no goal
        userInput = targetIndex.getOneBased() + " " + PREFIX_GOAL;
        expectedCommand = new GoalCommand(INDEX_FIRST_PERSON, new Goal(""));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, GoalCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, GoalCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, GoalCommand.COMMAND_WORD + " " + nonEmptyGoal, expectedMessage);
    }
}