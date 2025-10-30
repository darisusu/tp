package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.BodyfatCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Bodyfat;

/**
 * Tests for {@link BodyfatCommandParser}.
 */
public class BodyfatCommandParserTest {

    private final BodyfatCommandParser parser = new BodyfatCommandParser();

    @Test
    public void parse_validValue_success() throws Exception {
        BodyfatCommand cmd = parser.parse("1 bf/18.5");
        assertEquals(new BodyfatCommand(Index.fromOneBased(1), new Bodyfat("18.5")), cmd);

        BodyfatCommand integer = parser.parse("2 bf/25");
        assertEquals(new BodyfatCommand(Index.fromOneBased(2), new Bodyfat("25")), integer);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 18.5"));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("0 bf/18.5"));
        assertThrows(ParseException.class, () -> parser.parse("-1 bf/18.5"));
        assertThrows(ParseException.class, () -> parser.parse("abc bf/18.5"));
    }

    @Test
    public void parse_invalidBodyfat_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 bf/100"));
        assertThrows(ParseException.class, () -> parser.parse("1 bf/4.9"));
        assertThrows(ParseException.class, () -> parser.parse("1 bf/18.55"));
        assertThrows(ParseException.class, () -> parser.parse("1 bf/"));
    }
}
