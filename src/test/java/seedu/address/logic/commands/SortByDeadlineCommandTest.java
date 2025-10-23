package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SortByDeadlineCommandTest {

    @Test
    public void execute_sortAscending_reordersListEarliestFirst() throws Exception {
        // Persons: p2 (no deadline), p1 (Dec), p3 (Jan)
        Person p1 = new PersonBuilder().withName("Bob").withDeadline("2025-12-01").build();
        Person p2 = new PersonBuilder().withName("Cara").withDeadline("").build();
        Person p3 = new PersonBuilder().withName("Alice").withDeadline("2025-01-15").build();

        AddressBook ab = new AddressBook();
        ab.setPersons(Arrays.asList(p2, p1, p3)); // Start unsorted

        Model model = new ModelManager(ab, new UserPrefs());

        Command cmd = new SortByDeadlineCommand(true);
        CommandResult result = cmd.execute(model);

        // Expect earliest first: p3 (Jan), p1 (Dec), p2 (none)
        List<Person> actual = new ArrayList<>(model.getFilteredPersonList());
        assertEquals(Arrays.asList(p3, p1, p2), actual);
        assertEquals("Sorted by deadline (earliest first).", result.getFeedbackToUser());
    }

    @Test
    public void execute_sortDescending_reordersListLatestFirst() throws Exception {
        Person p1 = new PersonBuilder().withName("Bob").withDeadline("2025-12-01").build();
        Person p2 = new PersonBuilder().withName("Cara").withDeadline("").build();
        Person p3 = new PersonBuilder().withName("Alice").withDeadline("2025-01-15").build();

        AddressBook ab = new AddressBook();
        ab.setPersons(Arrays.asList(p3, p2, p1)); // Start unsorted

        Model model = new ModelManager(ab, new UserPrefs());

        Command cmd = new SortByDeadlineCommand(false);
        CommandResult result = cmd.execute(model);

        // Expect latest first: p1 (Dec), p3 (Jan), p2 (none)
        List<Person> actual = new ArrayList<>(model.getFilteredPersonList());
        assertEquals(Arrays.asList(p1, p3, p2), actual);
        assertEquals("Sorted by deadline (latest first).", result.getFeedbackToUser());
    }

    @Test
    public void equals_and_hashCode() {
        SortByDeadlineCommand asc = new SortByDeadlineCommand(true);
        SortByDeadlineCommand asc2 = new SortByDeadlineCommand(true);
        SortByDeadlineCommand desc = new SortByDeadlineCommand(false);

        // basic equals
        assertEquals(asc, asc2);
        // distinct
        org.junit.jupiter.api.Assertions.assertNotEquals(asc, desc);
        org.junit.jupiter.api.Assertions.assertNotEquals(asc, null);
        org.junit.jupiter.api.Assertions.assertNotEquals(asc, "string");

        // hashCode contract for equal objects
        assertEquals(asc.hashCode(), asc2.hashCode());
    }
}
