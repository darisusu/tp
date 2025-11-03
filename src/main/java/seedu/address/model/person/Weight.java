package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's weight in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidWeight(String)}
 */
public class Weight {

    public static final String MESSAGE_CONSTRAINTS =
            "Weight should be a positive number (up to 2 decimal places), between 20 and 500 (inclusive), " +
                    "and should not be blank.";

    // Regex for: 1 or more digits, optionally followed by a decimal and 1 or 2 digits.
    public static final String VALIDATION_REGEX = "\\d+(\\.\\d{1,2})?";

    public final Double value;

    /**
     * Constructs a {@code Weight}.
     *
     * @param weightString The weight input as a string.
     */
    public Weight(String weightString) {
        requireNonNull(weightString);
        checkArgument(isValidWeight(weightString), MESSAGE_CONSTRAINTS);
        this.value = Double.parseDouble(weightString);
    }

    /**
     * Returns true if a given string is a valid weight format and a positive number.
     */
    public static boolean isValidWeight(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }
        try {
            double weightValue = Double.parseDouble(test);
            // Set lower and upper limits for weight
            return (weightValue >= 20) && (weightValue <= 500);
        } catch (NumberFormatException e) {
            return false; // Should be caught by regex, but as a fallback
        }
    }

    @Override
    public String toString() {
        // Format to display up to 2 decimal places
        return String.format("%.2f kg", value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Weight)) {
            return false;
        }

        Weight otherWeight = (Weight) other;
        // Compare the Double values
        return value.equals(otherWeight.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
