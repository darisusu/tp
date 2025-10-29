package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SESSION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code SessionCommand}.
 */
public class SessionCommandTest {

    private static final String SESSION_STUB = "MONTHLY:05 18:30";

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_updateSessionUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withSession(SESSION_STUB).build();

        SessionCommand sessionCommand = new SessionCommand(INDEX_FIRST_PERSON, Session.fromString(SESSION_STUB));

        String expectedMessage = String.format(SessionCommand.MESSAGE_UPDATE_SESSION_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(sessionCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withSession(SESSION_STUB).build();

        SessionCommand sessionCommand = new SessionCommand(INDEX_FIRST_PERSON, Session.fromString(SESSION_STUB));

        String expectedMessage = String.format(SessionCommand.MESSAGE_UPDATE_SESSION_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(sessionCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        SessionCommand sessionCommand = new SessionCommand(outOfBoundIndex, Session.fromString(SESSION_STUB));

        assertCommandFailure(sessionCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book.
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        SessionCommand sessionCommand = new SessionCommand(outOfBoundIndex, Session.fromString(SESSION_STUB));

        assertCommandFailure(sessionCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_conflictingSession_failure() {
        Session conflictingSession = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()).getSession();
        SessionCommand sessionCommand = new SessionCommand(INDEX_FIRST_PERSON, conflictingSession);

        assertCommandFailure(sessionCommand, model, AddCommand.MESSAGE_CONFLICTING_SESSION);
    }

    @Test
    public void equals() {
        final SessionCommand standardCommand = new SessionCommand(INDEX_FIRST_PERSON, Session.fromString(VALID_SESSION_AMY));

        // same values -> returns true
        SessionCommand commandWithSameValues = new SessionCommand(INDEX_FIRST_PERSON, Session.fromString(VALID_SESSION_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new SessionCommand(INDEX_SECOND_PERSON, Session.fromString(VALID_SESSION_AMY))));

        // different session -> returns false
        assertFalse(standardCommand.equals(new SessionCommand(INDEX_FIRST_PERSON, Session.fromString(SESSION_STUB))));
    }
}
