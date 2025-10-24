package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Swap main content window to display client list
 */
public class ClientCommand extends Command {
    public static final String COMMAND_WORD = "client";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switches to client list.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_CLIENT_MESSAGE = "Showing client list.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_CLIENT_MESSAGE, false, false, true, false);
    }
}
