package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.DeadlineCommand.MESSAGE_ARGUMENTS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Deadline;

/**
 * Contains integration tests (interaction with the Model) and unit tests for DeadlineCommand.
 */
public class DeadlineCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    void execute_invalidIndex_throwsCommandException() {
        // Empty address book -> filtered list size = 0
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        DeadlineCommand cmd = new DeadlineCommand(INDEX_FIRST_PERSON, Deadline.empty());

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void equals() {
        Deadline setA  = Deadline.fromString("2025-12-31");
        Deadline setB  = Deadline.fromString("2026-01-01");
        Deadline clear = Deadline.empty();

        DeadlineCommand base = new DeadlineCommand(INDEX_FIRST_PERSON, setA);

        // same values -> true
        assertTrue(base.equals(new DeadlineCommand(INDEX_FIRST_PERSON, setA)));

        // different index -> false
        assertFalse(base.equals(new DeadlineCommand(INDEX_SECOND_PERSON, setA)));

        // different payload -> false
        assertFalse(base.equals(new DeadlineCommand(INDEX_FIRST_PERSON, setB)));
        assertFalse(base.equals(new DeadlineCommand(INDEX_FIRST_PERSON, clear)));

        // null / different type -> false
        assertFalse(base.equals(null));
        assertFalse(base.equals(new ClearCommand()));
    }
}