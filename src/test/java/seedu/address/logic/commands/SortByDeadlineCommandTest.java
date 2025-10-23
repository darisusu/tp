package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.ArrayList;
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
        Person p1 = new PersonBuilder(BENSON).withDeadline("2025-12-01").build();
        Person p2 = new PersonBuilder(CARL).withDeadline("").build();
        Person p3 = new PersonBuilder(ALICE).withDeadline("2025-01-15").build();

        AddressBook ab = new AddressBook();
        // Keep original (unsorted) insertion order:
        ab.addPerson(p2);
        ab.addPerson(p1);
        ab.addPerson(p3);

        Model model = new ModelManager(ab, new UserPrefs());
        CommandResult result = new SortByDeadlineCommand(true).execute(model);

        assertEquals(List.of(p3, p1, p2), new ArrayList<>(model.getFilteredPersonList()));
        assertEquals("Sorted by deadline (earliest first).", result.getFeedbackToUser());
    }

    @Test
    public void execute_sortDescending_reordersListLatestFirst() throws Exception {
        Person p1 = new PersonBuilder(BENSON).withDeadline("2025-12-01").build();
        Person p2 = new PersonBuilder(CARL).withDeadline("").build();
        Person p3 = new PersonBuilder(ALICE).withDeadline("2025-01-15").build();

        AddressBook ab = new AddressBook();
        // Keep original (unsorted) insertion order:
        ab.addPerson(p2);
        ab.addPerson(p1);
        ab.addPerson(p3);

        Model model = new ModelManager(ab, new UserPrefs());
        CommandResult result = new SortByDeadlineCommand(false).execute(model);

        assertEquals(List.of(p2, p1, p3), new ArrayList<>(model.getFilteredPersonList()));
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
