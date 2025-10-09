package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's goal in the address book.
 * Guarantees: immutable; is always valid
 */
public class Goal {
    public static final String MESSAGE_CONSTRAINTS =
            "Goals should not be longer than 100 characters.";
    public static final int MAX_LENGTH = 100;
    public final String value;

    /**
     * Constructs a {@code Goal}.
     *
     * @param goal A valid goal.
     */
    public Goal(String goal) {
        requireNonNull(goal);
        checkArgument(isValidGoal(goal), MESSAGE_CONSTRAINTS);
        value = goal;
    }

    /**
     * Returns true if the given string is a valid goal (<= 100 characters).
     */
    public static boolean isValidGoal(String test) {
        return test.length() <= MAX_LENGTH;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Goal // instanceof handles nulls
                && value.equals(((Goal) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
