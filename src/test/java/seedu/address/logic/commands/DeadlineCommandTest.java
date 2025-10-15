package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.DeadlineCommand.MESSAGE_ARGUMENTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for DeadlineCommand.
 */
public class DeadlineCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute() {
        final String deadline = "2025-12-31";

        assertCommandFailure(new DeadlineCommand(INDEX_FIRST_PERSON, deadline), model,
                String.format(MESSAGE_ARGUMENTS, INDEX_FIRST_PERSON.getOneBased(), deadline));
    }

    @Test
    public void equals() {
        final DeadlineCommand standardCommand = new DeadlineCommand(INDEX_FIRST_PERSON, VALID_DEADLINE_AMY);

        // same values -> returns true
        DeadlineCommand commandWithSameValues = new DeadlineCommand(INDEX_FIRST_PERSON, VALID_DEADLINE_AMY);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new DeadlineCommand(INDEX_SECOND_PERSON, VALID_DEADLINE_AMY)));

        // different deadline -> returns false
        assertFalse(standardCommand.equals(new DeadlineCommand(INDEX_FIRST_PERSON, VALID_DEADLINE_BOB)));
    }
}