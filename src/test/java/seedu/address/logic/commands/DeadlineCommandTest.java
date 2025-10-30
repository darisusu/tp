package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Deadline;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for DeadlineCommand.
 */
public class DeadlineCommandTest {

    private static final ZoneId ZONE = ZoneOffset.UTC;
    private static final Clock FIXED_CLOCK = Clock.fixed(LocalDate.of(2025, 1, 1)
            .atStartOfDay(ZONE).toInstant(), ZONE);

    private Model model;

    @BeforeEach
    void setUp() {
        Deadline.useClock(FIXED_CLOCK);
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @AfterEach
    void tearDown() {
        Deadline.useClock(Clock.systemDefaultZone());
    }

    @Test
    void execute_invalidIndex_throwsCommandException() {
        // Empty address book -> filtered list size = 0
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        DeadlineCommand cmd = new DeadlineCommand(INDEX_FIRST_PERSON, Deadline.empty());

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void equals() {
        Deadline setA = Deadline.fromString("2025-12-31");
        Deadline setB = Deadline.fromString("2026-01-01");
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

    @Test
    void execute_addDeadlineUnfilteredList_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withDeadline("2025-06-15").build();

        Deadline newDeadline = Deadline.fromString("2025-06-15");
        DeadlineCommand command = new DeadlineCommand(INDEX_FIRST_PERSON, newDeadline);

        String expectedMessage =
                String.format(DeadlineCommand.MESSAGE_ADD_DEADLINE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    void execute_clearDeadlineUnfilteredList_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withDeadline("").build();

        DeadlineCommand command = new DeadlineCommand(INDEX_FIRST_PERSON, Deadline.empty());

        String expectedMessage =
                String.format(DeadlineCommand.MESSAGE_DELETE_DEADLINE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }
}
