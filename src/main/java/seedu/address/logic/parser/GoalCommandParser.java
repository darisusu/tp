package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOAL;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.GoalCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Goal;

/**
 * Parses input arguments and creates a new {@code GoalCommand} object
 */
public class GoalCommandParser implements Parser<GoalCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemarkCommand}
     * and returns a {@code GoalCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GoalCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GOAL);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GoalCommand.MESSAGE_USAGE), ive);
        }

        String goal = argMultimap.getValue(PREFIX_GOAL).orElse("");

        return new GoalCommand(index, new Goal(goal));
    }

}
