package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class GoalCommand extends Command {

    public static final String COMMAND_WORD = "goal";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the goal of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing goal will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "goal/ [goal]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "goal/ Lose 10kg.";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
            "Goal command not implemented yet";


    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
