package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Age;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code AgeCommand}.
 */
public class AgeCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_addAge_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getGoal(),
                personToEdit.getHeight(),
                personToEdit.getWeight(),
                new Age("30"),
                personToEdit.getGender(),
                personToEdit.getDeadline(),
                personToEdit.getPaymentStatus(),
                personToEdit.getBodyfat(),
                personToEdit.getSession(),
                personToEdit.getTags()
        );

        AgeCommand ageCommand = new AgeCommand(INDEX_FIRST_PERSON, new Age("30"));

        String expectedMessage = String.format(AgeCommand.MESSAGE_ADD_AGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(ageCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AgeCommand ageCommand = new AgeCommand(outOfBoundIndex, new Age("25"));

        assertCommandFailure(ageCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidAge_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Age("150")); // >120 not allowed
        assertThrows(IllegalArgumentException.class, () -> new Age("-5")); // negative not allowed
        assertThrows(IllegalArgumentException.class, () -> new Age("abc")); // non-numeric
    }

    @Test
    public void equals() {
        final AgeCommand standardCommand = new AgeCommand(INDEX_FIRST_PERSON, new Age("25"));

        // same values → returns true
        AgeCommand commandWithSameValues = new AgeCommand(INDEX_FIRST_PERSON, new Age("25"));
        assertEquals(standardCommand, commandWithSameValues);

        // same object → returns true
        assertEquals(standardCommand, standardCommand);

        // null → returns false
        assertThrows(NullPointerException.class, () -> new AgeCommand(null, null));

        // different index → returns false
        AgeCommand differentIndexCommand = new AgeCommand(INDEX_SECOND_PERSON, new Age("25"));
        assertEquals(false, standardCommand.equals(differentIndexCommand));

        // different age → returns false
        AgeCommand differentAgeCommand = new AgeCommand(INDEX_FIRST_PERSON, new Age("40"));
        assertEquals(false, standardCommand.equals(differentAgeCommand));
    }
}
