package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Sorts the person list by paid status (unpaid first, paid second).
 */
public class SortByPaidCommand extends Command {

    public static final String COMMAND_WORD = "sortbypaid";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the person list by paid status (unpaid clients first, paid clients second).\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SORT_SUCCESS = "List sorted by paid status (unpaid first, paid second)";

    @Override
    public CommandResult execute(Model model) {
        model.resetPersonListOrder();
        model.sortPersonListByPaid();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SORT_SUCCESS, false, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortByPaidCommand)) {
            return false;
        }

        return true; // All SortByPaidCommand instances are equal since they have no parameters
    }
}
