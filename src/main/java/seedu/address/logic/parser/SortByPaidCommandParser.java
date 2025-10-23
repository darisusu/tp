package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortByPaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortByPaidCommand object
 */
public class SortByPaidCommandParser implements Parser<SortByPaidCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortByPaidCommand
     * and returns a SortByPaidCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortByPaidCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        
        if (!trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortByPaidCommand.MESSAGE_USAGE));
        }

        return new SortByPaidCommand();
    }
}
