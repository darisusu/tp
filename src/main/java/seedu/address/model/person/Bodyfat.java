package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's body fat percentage in the address book.
 * Guarantees: immutable; is always valid
 */
public class Bodyfat {

    /*
     * Error message if body fat is in incorrect format.
     */
    public static final String MESSAGE_CONSTRAINTS =
            "Bodyfat should be a number between 5.0 and 60.0, with at most 1 decimal place "
                    + "(e.g. 18.5 or 25)";

    private static final String VALIDATION_REGEX = "^[0-9]{1,2}(\\.[0-9])?$|^60(\\.0)?$";

    public final double value;

    /**
     * Constructs a {@code Bodyfat}.
     *
     * @param bodyfat A valid body fat percentage.
     */
    public Bodyfat(String bodyfat) {
        requireNonNull(bodyfat);
        checkArgument(isValidBodyfat(bodyfat), MESSAGE_CONSTRAINTS);
        value = Double.parseDouble(bodyfat);
    }

    /**
     * Returns true if a given string is a valid body fat percentage.
     * Acceptable range: 5.0–60.0 inclusive, up to 1 decimal place.
     */
    public static boolean isValidBodyfat(String bodyfatInput) {

        if (!bodyfatInput.matches(VALIDATION_REGEX)) {
            return false;
        }

        try {
            double bodyfat = Double.parseDouble(bodyfatInput);
            return bodyfat >= 5.0 && bodyfat <= 60.0;
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

        if (!(other instanceof Bodyfat)) {
            return false;
        }

        Bodyfat otherBodyfat = (Bodyfat) other;
        return Double.compare(value, otherBodyfat.value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}
