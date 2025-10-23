package seedu.address.logic.parser;

import java.util.Locale;

import seedu.address.logic.commands.SortByDeadlineCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a {@link SortByDeadlineCommand}.
 */
public class SortByDeadlineCommandParser implements Parser<SortByDeadlineCommand> {
    @Override
    public SortByDeadlineCommand parse(String args) throws ParseException {
        String t = args == null ? "" : args.trim().toLowerCase(Locale.ROOT);
        if (t.isEmpty() || t.equals("asc")) {
            return new SortByDeadlineCommand(true);
        }
        if (t.equals("desc")) {
            return new SortByDeadlineCommand(false);
        }
        throw new ParseException(SortByDeadlineCommand.MESSAGE_USAGE);
    }
}
