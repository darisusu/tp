package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeadlineCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Deadline;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;

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

        // 2) The dl/ prefix is compulsory for this command.
        //    (Value may be "" to clear, but the prefix itself must be present.)
        if (!map.getValue(PREFIX_DEADLINE).isPresent()) {
            throw new ParseException(String.format(DeadlineCommand.MESSAGE_INVALID_COMMAND_FORMAT,
                    DeadlineCommand.MESSAGE_USAGE));
        }

        // 3) Validate & build the value object ("" => clear)
        String raw = map.getValue(PREFIX_DEADLINE).get();   // present by now
        if (!Deadline.isValidDeadline(raw)) {
            throw new ParseException(Deadline.MESSAGE_CONSTRAINTS);
        }

        Deadline deadline = Deadline.fromString(raw);
        return new DeadlineCommand(index, deadline);
    }
}
