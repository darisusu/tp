package seedu.address.logic.parser;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

public final class PrefixGuards {
    private PrefixGuards() {}

    private static final Pattern ANY_PREFIX_PATTERN =
            Pattern.compile("(?<=\\s|^)([A-Za-z]+/)");

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
