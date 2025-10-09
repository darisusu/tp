package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents whether a Person has paid.
 * Guarantees: immutable; is valid as declared in {@link #isValidPaid(String)}
 */
public class Paid {

    public static final String MESSAGE_CONSTRAINTS =
            "Paid status should be either 'true' or 'false' (case-insensitive).";

    public final boolean value;

    /**
     * Constructs a {@code Paid}.
     *
     * @param paid A valid paid status.
     */
    public Paid(String paid) {
        requireNonNull(paid);
        String trimmed = paid.trim().toLowerCase();
        if (!trimmed.equals("true") && !trimmed.equals("false")) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.value = Boolean.parseBoolean(trimmed);
    }

    /**
     * Returns true if a given string is a valid paid status.
     */
    public static boolean isValidPaid(String paid) {
        if (paid == null) {
            return false;
        }
        String trimmed = paid.trim().toLowerCase();
        return trimmed.equals("true") || trimmed.equals("false");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Paid
                && value == ((Paid) other).value);
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }
}
