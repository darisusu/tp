package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GOAL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GOAL_BOB;
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
import seedu.address.model.person.Goal;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class GoalCommandTest {

    private static final String GOAL_STUB = "Fly in the sky";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addGoalUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withGoal(GOAL_STUB).build();

        GoalCommand remarkCommand = new GoalCommand(INDEX_FIRST_PERSON, new Goal(editedPerson.getGoal().value));

        String expectedMessage = String.format(GoalCommand.MESSAGE_ADD_GOAL_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteGoalUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withGoal("").build();

        GoalCommand goalCommand = new GoalCommand(INDEX_FIRST_PERSON,
                new Goal(editedPerson.getGoal().toString()));

        String expectedMessage = String.format(GoalCommand.MESSAGE_DELETE_GOAL_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(goalCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withGoal(GOAL_STUB).build();

        GoalCommand remarkCommand = new GoalCommand(INDEX_FIRST_PERSON, new Goal(editedPerson.getGoal().value));

        String expectedMessage = String.format(GoalCommand.MESSAGE_ADD_GOAL_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        GoalCommand remarkCommand = new GoalCommand(outOfBoundIndex, new Goal(VALID_GOAL_BOB));

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        GoalCommand goalCommand = new GoalCommand(outOfBoundIndex, new Goal(VALID_GOAL_BOB));
        assertCommandFailure(goalCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final GoalCommand standardCommand = new GoalCommand(INDEX_FIRST_PERSON, new Goal(VALID_GOAL_AMY));

        // same values -> returns true
        GoalCommand commandWithSameValues = new GoalCommand(INDEX_FIRST_PERSON, new Goal(VALID_GOAL_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new GoalCommand(INDEX_SECOND_PERSON, new Goal(VALID_GOAL_AMY))));

        // different remark -> returns false
        assertFalse(standardCommand.equals(new GoalCommand(INDEX_FIRST_PERSON, new Goal(VALID_GOAL_BOB))));
    }
}
