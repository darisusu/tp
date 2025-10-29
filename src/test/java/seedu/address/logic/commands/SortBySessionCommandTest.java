package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;

/**
 * Unit tests for {@link SortBySessionCommand}.
 */
public class SortBySessionCommandTest {

    @Test
    public void equals() {
        SortBySessionCommand sortBySessionCommand = new SortBySessionCommand();

        // same object -> returns true
        assertTrue(sortBySessionCommand.equals(sortBySessionCommand));

        // same type -> returns true
        assertTrue(sortBySessionCommand.equals(new SortBySessionCommand()));

        // different types -> returns false
        assertFalse(sortBySessionCommand.equals(1));

        // null -> returns false
        assertFalse(sortBySessionCommand.equals(null));
    }

    @Test
    public void execute_sortBySession_success() throws Exception {
        ModelStub modelStub = new ModelStub();
        SortBySessionCommand command = new SortBySessionCommand();

        CommandResult result = command.execute(modelStub);

        assertEquals(SortBySessionCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(modelStub.isSetPersonListComparatorCalled());
    }

    /**
     * Model stub to track whether setPersonListComparator has been called.
     */
    private static class ModelStub implements Model {
        private boolean setPersonListComparatorCalled = false;

        @Override
        public void setPersonListComparator(Comparator<Person> comparator) {
            setPersonListComparatorCalled = true;
        }

        public boolean isSetPersonListComparatorCalled() {
            return setPersonListComparatorCalled;
        }

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
            // No action needed in stub
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
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetPersonListOrder() {
            // No action needed in stub
        }

    }
}
