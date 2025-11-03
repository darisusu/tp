package seedu.address.logic.parser;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Guard utilities for validating CLI argument prefixes.
 * <p>
 * Scans an argument string for tokens that look like prefixes (letters followed by '/')
 * appearing at word boundaries, and throws a {@link ParseException} if any such token
 * is not among the allowed/known prefixes for a command.
 */
public final class PrefixGuards {
    private static final Pattern ANY_PREFIX_PATTERN =
            Pattern.compile("(?<=\\s|^)([A-Za-z]+/)");
    private PrefixGuards() {}



    /**
     * Ensures the given argument string contains no unknown/typo prefixes.
     *
     * @param args          the raw arguments string, e.g. {@code "n/John p/123 x/bad"}
     * @param knownPrefixes a set of allowed prefix lexemes (e.g., {@code "n/", "p/", "e/"}).
     * @throws ParseException if a token that looks like a prefix is not in {@code knownPrefixes}
     */
    public static void ensureNoUnknownPrefixes(String args, Set<String> knownPrefixes) throws ParseException {
        Matcher m = ANY_PREFIX_PATTERN.matcher(args);
        while (m.find()) {
            String raw = m.group(1); // e.g., "x/"
            if (!knownPrefixes.contains(raw)) {
                String valid = String.join(" ", knownPrefixes);
                throw new ParseException(String.format(Messages.MESSAGE_UNKNOWN_PREFIX, raw, valid));
            }
        }
    }
}
