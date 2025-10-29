package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODYFAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.BodyfatCommand;
import seedu.address.logic.commands.DeadlineCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Bodyfat;
import seedu.address.model.person.Deadline;

/**
 * Parses input arguments and creates a new {@code BodyfatCommand} object.
 */
public class BodyfatCommandParser implements Parser<BodyfatCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code BodyfatCommand}
     * and returns a {@code BodyfatCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public BodyfatCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_BODYFAT);
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BodyfatCommand.MESSAGE_USAGE), ive);
        }

        if (!argMultimap.getValue(PREFIX_BODYFAT).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    BodyfatCommand.MESSAGE_USAGE));
        }

        String raw = argMultimap.getValue(PREFIX_BODYFAT).get();
        if (!Bodyfat.isValidBodyfat(raw)) {
            throw new ParseException(Bodyfat.MESSAGE_CONSTRAINTS);
        }

        String bodyfatValue = argMultimap.getValue(PREFIX_BODYFAT).orElse("");
        Bodyfat bodyfat = new Bodyfat(bodyfatValue);

        return new BodyfatCommand(index, bodyfat);
    }
}
