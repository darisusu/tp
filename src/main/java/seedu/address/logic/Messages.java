package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_UNKNOWN_PREFIX = "Unknown prefix: %s\nValid prefixes: %s";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone().toString().isEmpty() ? "Not Specified" : person.getPhone())
                .append("; Email: ")
                .append(person.getEmail().toString().isEmpty() ? "Not Specified" : person.getEmail())
                .append("; Address: ")
                .append(person.getAddress().toString().isEmpty() ? "Not Specified" : person.getAddress())
                .append("; Goal: ")
                .append(person.getGoal().toString().isEmpty() ? "Not Specified" : person.getGoal())
                .append("; Height: ")
                .append(person.getHeight().toString().isEmpty() ? "Not Specified" : person.getHeight())
                .append("; Weight: ")
                .append(person.getWeight().toString().isEmpty() ? "Not Specified" : person.getWeight())
                .append("; Age: ")
                .append(person.getAge().toString().isEmpty() ? "Not Specified" : person.getAge())
                .append("; Gender: ")
                .append(person.getGender().toString().isEmpty() ? "Not Specified" : person.getGender())
                .append("; Deadline: ")
                .append(person.getDeadline().toStorageString().isEmpty() ? "Not Specified" : person.getDeadline())
                .append("; Paid: ")
                .append(person.getPaymentStatus().toString().isEmpty() ? "Not Specified" : person.getPaymentStatus())
                .append("; Session: ")
                .append(person.getSession())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
