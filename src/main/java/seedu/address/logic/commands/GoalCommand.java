package seedu.address.logic.commands;

import seedu.address.model.Model;

public class GoalCommand extends Command {
    public static final String COMMAND_WORD = "goal";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Hello from goal");
    }
}
