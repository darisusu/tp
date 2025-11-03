package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClientCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DashboardCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses single-word arguments and returns their respective commands
 */
public class SingleWordCommandParser implements Parser<Command> {
    /**
     * Parses all single-word commands to ensure no additional arguments inserted.
     * @param args the input argument containing the command.
     * @return the respective command of argument given.
     * @throws ParseException if any arguments are given after the command.
     */
    @Override
    public Command parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        String[] tokens = trimmedArgs.split("\\s+");
        String commandWord = tokens[0];

        if (tokens.length > 1) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    String.format("`%s` is a single-word command and takes no arguments.", commandWord)
            ));
        }

        return switch (trimmedArgs.toLowerCase()) {
        case "list" -> new ListCommand();
        case "clear" -> new ClearCommand();
        case "exit" -> new ExitCommand();
        case "help" -> new HelpCommand();
        case "dashboard" -> new DashboardCommand();
        case "client" -> new ClientCommand();
        default -> throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, trimmedArgs));
        };
    }
}
