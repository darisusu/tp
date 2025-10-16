package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AGE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AgeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Age;

/**
 * Parses input arguments and creates a new AgeCommand object
 */
public class AgeCommandParser implements Parser<AgeCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code AgeCommand}
     * and returns a {@code AgeCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AgeCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_AGE);

        // 1) Parse index, but normalize any index error to MESSAGE_USAGE
        final Index index;
        try {
            index = ParserUtil.parseIndex(map.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(AgeCommand.MESSAGE_INVALID_COMMAND_FORMAT,
                    AgeCommand.MESSAGE_USAGE), pe);
        }

        // 2) The age/ prefix is compulsory for this command.
        //    (Value may be "" to clear, but the prefix itself must be present.)
        if (!map.getValue(PREFIX_AGE).isPresent()) {
            throw new ParseException(String.format(AgeCommand.MESSAGE_INVALID_COMMAND_FORMAT,
                    AgeCommand.MESSAGE_USAGE));
        }

        // 3) Validate & build the value object ("" => clear)
        String raw = map.getValue(PREFIX_AGE).get(); // present by now
        if (!Age.isValidAge(raw)) {
            throw new ParseException(Age.MESSAGE_CONSTRAINTS);
        }

        Age age = new Age(raw);
        return new AgeCommand(index, age);
    }
}
