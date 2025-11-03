package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.GenderCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Gender;

/**
 * Parses input arguments and creates a new {@code GenderCommand} object
 */
public class GenderCommandParser implements Parser<GenderCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code GenderCommand}
     * and returns a {@code GenderCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GenderCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GENDER);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GenderCommand.MESSAGE_USAGE), ive);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GENDER);

        String genderRaw = argMultimap.getValue(PREFIX_GENDER).orElse("");
        // Trim and validate via ParserUtil for consistent behavior across commands
        Gender gender = ParserUtil.parseGender(genderRaw);
        return new GenderCommand(index, gender);
    }
}
