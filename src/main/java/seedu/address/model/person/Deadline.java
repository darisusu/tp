package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Deadline {

    public static final String MESSAGE_CONSTRAINTS =
            "Deadlines should be in the format yyyy-MM-dd and must be a valid calendar date.";

    // Matches strictly valid ISO-style dates like 2025-10-09
    public static final String VALIDATION_REGEX =
            "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final LocalDate date;

    /**
     * Constructs a {@code Deadline}.
     *
     * @param dateString A valid date string in yyyy-MM-dd format.
     * @throws NullPointerException if {@code dateString} is null.
     * @throws IllegalArgumentException if {@code dateString} does not match regex or is invalid.
     */
    public Deadline(String dateString) {
        Objects.requireNonNull(dateString, "Deadline cannot be null");
        if (!isValidDeadline(dateString)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        try {
            this.date = LocalDate.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Returns true if the given string is a valid deadline date.
     */
    public static boolean isValidDeadline(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the stored deadline as a {@code LocalDate}.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns true if the deadline is before today.
     */
    public boolean isOverdue() {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Returns true if the deadline is today.
     */
    public boolean isToday() {
        return date.equals(LocalDate.now());
    }

    @Override
    public String toString() {
        return date.format(FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Deadline
                && date.equals(((Deadline) other).date));
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
