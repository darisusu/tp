package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Goal;

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

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Remark: %2$s";

    private final Index index;
    private final Goal goal;

    /**
     * @param index of the person in the filtered person list to edit the remark
     * @param goal of the person to be updated to
     */
    public GoalCommand(Index index, Goal goal) {
        requireAllNonNull(index, goal);

        this.index = index;
        this.goal = goal;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(
                String.format(MESSAGE_ARGUMENTS, index.getOneBased(), goal));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GoalCommand)) {
            return false;
        }

        GoalCommand e = (GoalCommand) other;
        return index.equals(e.index)
                && goal.equals(e.goal);
    }
}
