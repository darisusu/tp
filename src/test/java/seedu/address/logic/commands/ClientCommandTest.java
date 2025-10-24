package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ClientCommand.SHOWING_CLIENT_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ClientCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_dashboard_success() {
        CommandResult expectedCommandResult =
                new CommandResult(SHOWING_CLIENT_MESSAGE, false, false, true, false);
        assertCommandSuccess(new ClientCommand(), model, expectedCommandResult, expectedModel);
    }
}
