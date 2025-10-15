package seedu.address.model.person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's deadline in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDeadline(String)}
 */
public class Deadline {

    public static final String MESSAGE_CONSTRAINTS =
            "Deadlines should be in the format yyyy-MM-dd, or left empty to clear";

    // Matches strictly valid ISO-style dates like 2025-10-09
    public static final String VALIDATION_REGEX = "^$|^\\d{4}-\\d{2}-\\d{2}$";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Optional<LocalDate> value;

    private Deadline(Optional<LocalDate> value) {
        this.value = requireNonNull(value);
    }

    public static Deadline fromString(String raw) {
        if (!isValidDeadline(raw)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        String s = raw.trim();
        return s.isEmpty() ? Deadline.empty() : Deadline.of(LocalDate.parse(s));
    }


    /**
     * Returns true if {@code raw} is a valid deadline.
     * Valid inputs:
     *  - "" or whitespace-only (interpreted as 'clear')
     *  - A real calendar date in ISO format YYYY-MM-DD (e.g., 2025-12-31)
     */
    public static boolean isValidDeadline(String raw) {
        Objects.requireNonNull(raw);
        String s = raw.trim();

        // shape check (allows empty)
        if (!s.matches(VALIDATION_REGEX)) {
            return false;
        }
        // empty is valid (clear)
        if (s.isEmpty()) {
            return true;
        }
        // calendar check (rejects impossible dates, e.g., 2025-02-30)
        try {
            LocalDate.parse(s);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    public static Deadline of(LocalDate date) {
        return new Deadline(Optional.of(requireNonNull(date)));
    }

    public static Deadline empty() {
        return new Deadline(Optional.empty());
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public Optional<LocalDate> asOptional() {
        return value;
    }

    public String toStorageString() {
        return value.map(LocalDate::toString).orElse("");
    }

    @Override
    public String toString() {
        return toStorageString();
    }

    @Override
    public boolean equals(Object o) {
        return (this == o) ||
                (o instanceof Deadline
                && value.equals(((Deadline)o).value));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Constructs a {@code Deadline}.
     *
     * @param dateString A valid date string in yyyy-MM-dd format.
     * @throws NullPointerException if {@code dateString} is null.
     * @throws IllegalArgumentException if {@code dateString} does not match regex or is invalid.
     *//*
    public Deadline(String dateString) {
        requireNonNull(dateString, "Deadline cannot be null");
        if (!isValidDeadline(dateString)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        try {
            this.value = LocalDate.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }*/

    /**
     * Returns true if the given string is a valid deadline date.
     *//*
    public static boolean isValidDeadline(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    *//**
     * Returns the stored deadline as a {@code LocalDate}.
     *//*
    public LocalDate getDate() {
        return value;
    }

    public String getDateString() {
        return value.toString();
    }

    *//**
     * Returns true if the deadline is before today.
     *//*
    public boolean isOverdue() {
        return value.isBefore(LocalDate.now());
    }

    *//**
     * Returns true if the deadline is today.
     *//*
    public boolean isToday() {
        return value.equals(LocalDate.now());
    }

    @Override
    public String toString() {
        return value.format(FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Deadline
                && value.equals(((Deadline) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }*/
}
