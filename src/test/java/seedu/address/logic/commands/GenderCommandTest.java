package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GENDER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GENDER_BOB;
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
import seedu.address.model.person.Gender;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model)
 * and unit tests for {@code GenderCommand}.
 */
public class GenderCommandTest {

    private static final String GENDER_STUB = "male";

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_updateGenderUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withGender(GENDER_STUB).build();

        GenderCommand genderCommand = new GenderCommand(INDEX_FIRST_PERSON, new Gender(GENDER_STUB));

        String expectedMessage = String.format(GenderCommand.MESSAGE_ADD_GENDER_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(genderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withGender(GENDER_STUB).build();

        GenderCommand genderCommand = new GenderCommand(INDEX_FIRST_PERSON, new Gender(GENDER_STUB));

        String expectedMessage = String.format(GenderCommand.MESSAGE_ADD_GENDER_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(genderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_abbreviation_expandsToFullForm() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withGender("prefer not to say").build();

        GenderCommand genderCommand = new GenderCommand(INDEX_FIRST_PERSON, new Gender("pns"));

        String expectedMessage = String.format(GenderCommand.MESSAGE_ADD_GENDER_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(genderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        GenderCommand genderCommand = new GenderCommand(outOfBoundIndex, new Gender(VALID_GENDER_BOB));

        assertCommandFailure(genderCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        GenderCommand genderCommand = new GenderCommand(outOfBoundIndex, new Gender(VALID_GENDER_BOB));

        assertCommandFailure(genderCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final GenderCommand standardCommand = new GenderCommand(INDEX_FIRST_PERSON, new Gender(VALID_GENDER_AMY));

        // same values -> returns true
        GenderCommand commandWithSameValues = new GenderCommand(INDEX_FIRST_PERSON, new Gender(VALID_GENDER_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new GenderCommand(INDEX_SECOND_PERSON, new Gender(VALID_GENDER_AMY))));

        // different gender -> returns false
        assertFalse(standardCommand.equals(new GenderCommand(INDEX_FIRST_PERSON, new Gender(VALID_GENDER_BOB))));
    }
}
