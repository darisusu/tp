package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's height in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidHeight(String)}
 */
public class Height {

    /*
     * Error message if height is in incorrect format.
     */
    public static final String MESSAGE_CONSTRAINTS = "Height should be a positive integer in cm (e.g. 170)";

    public final int value; // the height in cm and int

    /* Constructs a {@code Height}.
     *
     * @param height A valid height in cm.
     */
    public Height(String height) {
        requireNonNull(height);
        checkArgument(isValidHeight(height), MESSAGE_CONSTRAINTS);
        value = Integer.parseInt(height);
    }

    /**
     * Returns true if a given string is a valid height.
     * Only positive numbers under 300cm.
     */
    public static boolean isValidHeight(String heightInput) {
        try {
            int height = Integer.parseInt(heightInput);
            return height > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value + " cm";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Height)) {
            return false;
        }

        Height otherHeight = (Height) other;
        return value == otherHeight.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

}
