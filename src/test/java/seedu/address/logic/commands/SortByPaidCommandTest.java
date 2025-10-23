package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;

public class SortByPaidCommandTest {

    @Test
    public void equals() {
        SortByPaidCommand sortPaidCommand = new SortByPaidCommand();

        // same object -> returns true
        assertTrue(sortPaidCommand.equals(sortPaidCommand));

        // same type -> returns true
        assertTrue(sortPaidCommand.equals(new SortByPaidCommand()));

        // different types -> returns false
        assertFalse(sortPaidCommand.equals(1));

        // null -> returns false
        assertFalse(sortPaidCommand.equals(null));
    }

    @Test
    public void execute_sortPaid_success() {
        ModelStub modelStub = new ModelStub();
        SortByPaidCommand sortPaidCommand = new SortByPaidCommand();

        CommandResult commandResult = sortPaidCommand.execute(modelStub);

        assertEquals(SortByPaidCommand.MESSAGE_SORT_SUCCESS, commandResult.getFeedbackToUser());
        assertTrue(modelStub.isSortPersonListByPaidCalled());
    }

    /**
     * A Model stub that tracks whether sortPersonListByPaid has been called.
     */
    private static class ModelStub implements Model {
        private boolean sortPersonListByPaidCalled = false;

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            // Allow this method to be called without throwing exception
        }

        @Override
        public boolean hasSessionConflict(Person person, Person toIgnore) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasSessionConflict(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortPersonListByPaid() {
            sortPersonListByPaidCalled = true;
        }

        public boolean isSortPersonListByPaidCalled() {
            return sortPersonListByPaidCalled;
        }
    }
}
