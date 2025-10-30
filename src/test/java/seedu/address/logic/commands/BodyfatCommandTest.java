package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BODYFAT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BODYFAT_BOB;
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
import seedu.address.model.person.Bodyfat;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code BodyfatCommand}.
 */
public class BodyfatCommandTest {

    private static final String BODYFAT_STUB = "18.5";

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_updateBodyfatUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withBodyfat(BODYFAT_STUB).build();

        BodyfatCommand bodyfatCommand = new BodyfatCommand(INDEX_FIRST_PERSON, new Bodyfat(BODYFAT_STUB));

        String expectedMessage = String.format(BodyfatCommand.MESSAGE_ADD_BODYFAT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(bodyfatCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withBodyfat(BODYFAT_STUB).build();

        BodyfatCommand bodyfatCommand = new BodyfatCommand(INDEX_FIRST_PERSON, new Bodyfat(BODYFAT_STUB));

        String expectedMessage = String.format(BodyfatCommand.MESSAGE_ADD_BODYFAT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(bodyfatCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        BodyfatCommand bodyfatCommand = new BodyfatCommand(outOfBoundIndex, new Bodyfat(VALID_BODYFAT_BOB));

        assertCommandFailure(bodyfatCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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

        BodyfatCommand bodyfatCommand = new BodyfatCommand(outOfBoundIndex, new Bodyfat(VALID_BODYFAT_BOB));

        assertCommandFailure(bodyfatCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final BodyfatCommand standardCommand = new BodyfatCommand(INDEX_FIRST_PERSON, new Bodyfat(VALID_BODYFAT_AMY));

        // same values -> returns true
        BodyfatCommand commandWithSameValues = new BodyfatCommand(INDEX_FIRST_PERSON, new Bodyfat(VALID_BODYFAT_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new BodyfatCommand(INDEX_SECOND_PERSON, new Bodyfat(VALID_BODYFAT_AMY))));

        // different bodyfat -> returns false
        assertFalse(standardCommand.equals(new BodyfatCommand(INDEX_FIRST_PERSON, new Bodyfat(VALID_BODYFAT_BOB))));
    }
}
