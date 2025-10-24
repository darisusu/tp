package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.DashboardCommand.SHOWING_DASHBOARD_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class DashboardCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_dashboard_success() {
        CommandResult expectedCommandResult =
                new CommandResult(SHOWING_DASHBOARD_MESSAGE, false, false, false, true);
        assertCommandSuccess(new DashboardCommand(), model, expectedCommandResult, expectedModel);
    }
}
