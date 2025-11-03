package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeadlineCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Deadline;

/**
 * Parses input arguments and creates a new DeadlineCommand object
 */
public class DeadlineCommandParser implements Parser<DeadlineCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code DeadlineCommand}
     * and returns a {@code DeadlineCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public DeadlineCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_DEADLINE);

        // 1) Parse index, but normalize any index error to MESSAGE_USAGE
        final Index index;
        try {
            index = ParserUtil.parseIndex(map.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(DeadlineCommand.MESSAGE_INVALID_COMMAND_FORMAT,
                    DeadlineCommand.MESSAGE_USAGE), pe);
        }
        map.verifyNoDuplicatePrefixesFor(PREFIX_DEADLINE);

        // 2) The dl/ prefix is compulsory for this command.
        //    (Value may be "" to clear, but the prefix itself must be present.)
        if (!map.getValue(PREFIX_DEADLINE).isPresent()) {
            throw new ParseException(String.format(DeadlineCommand.MESSAGE_INVALID_COMMAND_FORMAT,
                    DeadlineCommand.MESSAGE_USAGE));
        }

        // 3) Validate & build the value object ("" => clear) using ParserUtil for detailed messages
        String raw = map.getValue(PREFIX_DEADLINE).get(); // present by now
        Deadline deadline = ParserUtil.parseDeadline(raw);
        return new DeadlineCommand(index, deadline);
    }
}
