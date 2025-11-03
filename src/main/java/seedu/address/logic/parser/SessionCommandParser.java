package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Session;

/**
 * Parses input arguments and creates a new {@code SessionCommand} object.
 */
public class SessionCommandParser implements Parser<SessionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code SessionCommand}
     * and returns a {@code SessionCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public SessionCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SESSION);

        final Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SessionCommand.MESSAGE_USAGE), pe);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SESSION);

        if (!argMultimap.getValue(PREFIX_SESSION).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SessionCommand.MESSAGE_USAGE));
        }

        Session session = ParserUtil.parseSession(argMultimap.getValue(PREFIX_SESSION).get());
        return new SessionCommand(index, session);
    }
}
