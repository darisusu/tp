package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortBySessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new {@link SortBySessionCommand} object.
 * <p>
 * Usage: {@code sortbysession}
 * <ul>
 *   <li>No parameters are accepted.</li>
 *   <li>Typing any extra text (e.g., flags or keywords) will result in a usage error.</li>
 * </ul>
 * When executed, the command sorts clients by their next upcoming session time
 * (earliest first), using the current date-time as the reference. Descending order is not supported.
 */
public class SortBySessionCommandParser implements Parser<SortBySessionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@link SortBySessionCommand}
     * and returns a {@link SortBySessionCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public SortBySessionCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();

        if (!trimmed.isEmpty()) {
            // Reject any extra tokens since this command takes no arguments
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, SortBySessionCommand.MESSAGE_USAGE));
        }

        return new SortBySessionCommand();
    }
}
