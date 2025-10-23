package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.PersonDeadlineComparator;

import static java.util.Objects.requireNonNull;

public class SortByDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "sortbydeadline";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the person list by deadline (earliest first by default). \n"
            + "Parameters: [asc|desc]\n"
            + "Example: " + COMMAND_WORD + " desc";

    public static final String MESSAGE_SUCCESS_ASC = "Sorted by deadline (earliest first).";
    public static final String MESSAGE_SUCCESS_DESC = "Sorted by deadline (latest first).";

    private final boolean ascending;

    public SortByDeadlineCommand(boolean ascending) {
        this.ascending = ascending;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.setPersonListComparator(new PersonDeadlineComparator(ascending));
        return new CommandResult(ascending ? MESSAGE_SUCCESS_ASC : MESSAGE_SUCCESS_DESC);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SortByDeadlineCommand
                && ((SortByDeadlineCommand) other).ascending == this.ascending;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(ascending);
    }
}
