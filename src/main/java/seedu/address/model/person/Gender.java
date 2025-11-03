package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Map;

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
            + "prefer not to say (or m/f/o/nb/pns). "
            + "Case-insensitive inputs are accepted.";

    private static final Map<String, String> NORMALIZED_GENDER_MAP = Map.ofEntries(
            Map.entry("male", "male"),
            Map.entry("m", "male"),
            Map.entry("female", "female"),
            Map.entry("f", "female"),
            Map.entry("other", "other"),
            Map.entry("o", "other"),
            Map.entry("non-binary", "non-binary"),
            Map.entry("nb", "non-binary"),
            Map.entry("prefer not to say", "prefer not to say"),
            Map.entry("pns", "prefer not to say"));

    public final String value; // the gender value

    /**
     * Constructs a {@code Gender}.
     *
     * @param gender A valid gender.
     */
    public Gender(String gender) {
        requireNonNull(gender);
        checkArgument(isValidGender(gender), MESSAGE_CONSTRAINTS);
        this.value = normalizeGender(gender);
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

        String normalizedKey = genderInput.toLowerCase().trim();
        return NORMALIZED_GENDER_MAP.containsKey(normalizedKey);
    }

    private static String normalizeGender(String genderInput) {
        String normalizedKey = genderInput.toLowerCase().trim();
        return NORMALIZED_GENDER_MAP.get(normalizedKey);
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
