package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEIGHT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEIGHT_BOB;
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
import seedu.address.model.person.Weight;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code WeightCommand}.
 */
public class WeightCommandTest {

    private static final String WEIGHT_STUB = "80";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_updateWeightUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withWeight(WEIGHT_STUB).build();

        WeightCommand weightCommand = new WeightCommand(INDEX_FIRST_PERSON, new Weight(WEIGHT_STUB));

        String expectedMessage = String.format(WeightCommand.MESSAGE_UPDATE_WEIGHT_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(weightCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withWeight(WEIGHT_STUB).build();

        WeightCommand weightCommand = new WeightCommand(INDEX_FIRST_PERSON, new Weight(WEIGHT_STUB));

        String expectedMessage = String.format(WeightCommand.MESSAGE_UPDATE_WEIGHT_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(weightCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        WeightCommand weightCommand = new WeightCommand(outOfBoundIndex, new Weight(VALID_WEIGHT_BOB));

        assertCommandFailure(weightCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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

        WeightCommand weightCommand = new WeightCommand(outOfBoundIndex, new Weight(VALID_WEIGHT_BOB));

        assertCommandFailure(weightCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final WeightCommand standardCommand = new WeightCommand(INDEX_FIRST_PERSON, new Weight(VALID_WEIGHT_AMY));

        // same values -> returns true
        WeightCommand commandWithSameValues = new WeightCommand(INDEX_FIRST_PERSON, new Weight(VALID_WEIGHT_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new WeightCommand(INDEX_SECOND_PERSON, new Weight(VALID_WEIGHT_AMY))));

        // different weight -> returns false
        assertFalse(standardCommand.equals(new WeightCommand(INDEX_FIRST_PERSON, new Weight(VALID_WEIGHT_BOB))));
    }
}