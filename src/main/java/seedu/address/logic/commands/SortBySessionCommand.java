package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.PersonSessionComparator;

public class SortBySessionCommand extends Command {
    public static final String COMMAND_WORD = "sortbysession";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the person list by sessions (earliest first by default). \n"
            + "Example: " + COMMAND_WORD + " desc";
    public static final String MESSAGE_SUCCESS = "List sorted by sessions (upcoming first)";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.setPersonListComparator(new PersonSessionComparator());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortBySessionCommand)) {
            return false;
        }

        return true; // All SortBySessionCommand instances are equal since they have no parameters
    }
}
