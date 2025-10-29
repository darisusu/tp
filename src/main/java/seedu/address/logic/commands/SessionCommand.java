package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;

/**
 * Updates the training session schedule of an existing person in the address book.
 */
public class SessionCommand extends Command {

    public static final String COMMAND_WORD = "session";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the session of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing session will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "s/SESSION\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "s/WEEKLY:MON-1800-1930";

    public static final String MESSAGE_UPDATE_SESSION_SUCCESS = "Updated session for Person: %1$s";

    private final Index index;
    private final Session session;

    /**
     * @param index of the person in the filtered person list to edit the session
     * @param session of the person to be updated to
     */
    public SessionCommand(Index index, Session session) {
        requireAllNonNull(index, session);
        this.index = index;
        this.session = session;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getGoal(),
                personToEdit.getHeight(),
                personToEdit.getWeight(),
                personToEdit.getAge(),
                personToEdit.getGender(),
                personToEdit.getDeadline(),
                personToEdit.getPaymentStatus(),
                personToEdit.getBodyfat(),
                session,
                personToEdit.getTags());

        if (model.hasSessionConflict(editedPerson, personToEdit)) {
            throw new CommandException(AddCommand.MESSAGE_CONFLICTING_SESSION);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_UPDATE_SESSION_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SessionCommand)) {
            return false;
        }

        SessionCommand otherCommand = (SessionCommand) other;
        return index.equals(otherCommand.index)
                && session.equals(otherCommand.session);
    }
}
