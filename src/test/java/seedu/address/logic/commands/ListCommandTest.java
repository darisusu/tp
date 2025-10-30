package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_notFiltered_switchesToClientView() {
        CommandResult expected = new CommandResult(ListCommand.MESSAGE_SUCCESS,
                /* showHelp */ false,
                /* exit */ false,
                /* showClient */ true,
                /* showSession */ false);
        assertCommandSuccess(new ListCommand(), model, expected, expectedModel);
    }

    @Test
    public void execute_filtered_switchesToClientView() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        CommandResult expected = new CommandResult(ListCommand.MESSAGE_SUCCESS,
                /* showHelp */ false,
                /* exit */ false,
                /* showClient */ true,
                /* showSession */ false);
        assertCommandSuccess(new ListCommand(), model, expected, expectedModel);
    }

    @Test
    public void execute_switchesToClientView_setsUiFlagsCorrectly() {
        // Execute directly to inspect CommandResult flags
        ListCommand command = new ListCommand();
        CommandResult result = command.execute(model);

        assertEquals(ListCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        // Mirrors ClientCommand: (message, showHelp=false, exit=false, showClientList=true, showSessionList=false)
        assertFalse(result.isShowHelp());
        assertFalse(result.isExit());
        assertTrue(result.isClient());
        assertFalse(result.isDashboard());
    }
}
