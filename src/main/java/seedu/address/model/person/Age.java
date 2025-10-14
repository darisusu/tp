package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's age in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAge(String)}
 */
public class Age {

    /*
     * Error message if age is in incorrect format.
     */
    public static final String MESSAGE_CONSTRAINTS = "Age must be a whole number between 1 and 120";

    public final int value; // the age in years

    /**
     * Constructs a {@code Age}.
     *
     * @param age A valid age in years.
     */
    public Age(String age) {
        requireNonNull(age);
        checkArgument(isValidAge(age), MESSAGE_CONSTRAINTS);
        this.value = Integer.parseInt(age);
    }

    /**
     * Returns true if a given string is a valid age.
     * Only positive numbers between 1 and 120.
     */
    public static boolean isValidAge(String ageInput) {
        try {
            int age = Integer.parseInt(ageInput);
            return age >= 1 && age <= 120;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Age)) {
            return false;
        }

        Age otherAge = (Age) other;
        return value == otherAge.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
