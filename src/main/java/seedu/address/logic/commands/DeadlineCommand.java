package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Deadline;
import seedu.address.model.person.Person;

/**
 * Changes the deadline of an existing person in the address book.
 */
public class DeadlineCommand extends Command {
    public static final String COMMAND_WORD = "deadline";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the deadline of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing deadline will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "dl/ [DEADLINE]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "dl/ 2025-12-31 ";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Deadline: %2$s";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
            "Deadline command not implemented yet";

    public static final String MESSAGE_ADD_DEADLINE_SUCCESS = "Updated deadline of Person: %1$s";
    public static final String MESSAGE_DELETE_DEADLINE_SUCCESS = "Removed deadline from Person: %1$s";
    public static final String MESSAGE_DEADLINE_UNCHANGED = "Payment deadline unchanged - Same date!: %1$s";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";

    private final Index index;
    private final Deadline deadline;

    /**
     * @param index of the person in the filtered person list to edit the deadline
     * @param deadline of the person to be updated to
     */
    public DeadlineCommand(Index index, Deadline deadline) {
        requireAllNonNull(index, deadline);

        this.index = index;
        this.deadline = deadline;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getGoal(),
                personToEdit.getHeight(),
                personToEdit.getWeight(),
                personToEdit.getAge(),
                personToEdit.getGender(),
                deadline,
                personToEdit.getPaymentStatus(),
                personToEdit.getBodyfat(),
                personToEdit.getSession(),
                personToEdit.getTags());

        Deadline oldDeadline = personToEdit.getDeadline();
        if (deadline.equals(oldDeadline)) {
            return new CommandResult(String.format(
                    MESSAGE_DEADLINE_UNCHANGED,
                    Messages.format(personToEdit)) + warningSuffix(oldDeadline));
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson) + warningSuffix(deadline));
    }

    /**
     * Generates a command execution success message based on whether
     * the deadline is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !deadline.isEmpty() ? MESSAGE_ADD_DEADLINE_SUCCESS : MESSAGE_DELETE_DEADLINE_SUCCESS;
        return String.format(message, Messages.format(personToEdit));
    }

    private static String warningSuffix(Deadline d) {
        if (d == null || d.isEmpty()) return "";
        boolean past = d.isPastOrToday();
        boolean far = d.isMoreThanYearsAhead(1);
        if (!past && !far) return "";
        String reason = past ? "the date is in the past" : "the date is more than 1 year ahead";
        return System.lineSeparator() + "Note: " + reason + "!";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeadlineCommand)) {
            return false;
        }

        DeadlineCommand e = (DeadlineCommand) other;
        return index.equals(e.index)
                && deadline.equals(e.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, deadline);
    }
}
