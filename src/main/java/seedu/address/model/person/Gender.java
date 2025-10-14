package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's gender in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGender(String)}
 */
public class Gender {

    /*
     * Error message if gender is in incorrect format.
     */
    public static final String MESSAGE_CONSTRAINTS =
            "Gender must be one of: male, female, other, non-binary, "
            + "prefer not to say (or m/f/o/nb/pns)";

    public final String value; // the gender value

    /**
     * Constructs a {@code Gender}.
     *
     * @param gender A valid gender.
     */
    public Gender(String gender) {
        requireNonNull(gender);
        checkArgument(isValidGender(gender), MESSAGE_CONSTRAINTS);
        this.value = gender.toLowerCase();
    }

    /**
     * Returns true if a given string is a valid gender.
     * Accepts: male, female, other, non-binary, prefer not to say
     * Also accepts: m, f, o, nb, pns (abbreviations)
     */
    public static boolean isValidGender(String genderInput) {
        if (genderInput == null || genderInput.trim().isEmpty()) {
            return false;
        }

        String normalized = genderInput.toLowerCase().trim();

        // Full forms
        if (normalized.equals("male") || normalized.equals("female")
                || normalized.equals("other") || normalized.equals("non-binary")
                || normalized.equals("prefer not to say")) {
            return true;
        }

        // Abbreviations
        if (normalized.equals("m") || normalized.equals("f")
                || normalized.equals("o") || normalized.equals("nb")
                || normalized.equals("pns")) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Gender)) {
            return false;
        }

        Gender otherGender = (Gender) other;
        return value.equals(otherGender.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
