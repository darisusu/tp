package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Swap main content window to display dashboard
 */
public class DashboardCommand extends Command {
    public static final String COMMAND_WORD = "dashboard";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switches to dashboard.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_DASHBOARD_MESSAGE = "Showing dashboard.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_DASHBOARD_MESSAGE, false, false, false, true);
    }
}
