package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeadlineCommand;
import seedu.address.model.person.Deadline;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DeadlineCommandParserTest {
    private static final ZoneId ZONE = ZoneOffset.UTC;
    private static final Clock FIXED_CLOCK =
            Clock.fixed(LocalDate.of(2025, 1, 1).atStartOfDay(ZONE).toInstant(), ZONE);

    private final DeadlineCommandParser parser = new DeadlineCommandParser();

    @BeforeEach
    void freezeClock() {
        Deadline.useClock(FIXED_CLOCK);
    }

    @AfterEach
    void unfreezeClock() {
        Deadline.useClock(Clock.systemDefaultZone());
    }

    @Test
    public void parse_indexSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // pick a valid date: strictly after 2025-01-01 and ≤ 2026-01-01
        String nonEmptyDeadline = LocalDate.now(FIXED_CLOCK).plusDays(1).toString(); // 2025-01-02

        // have Deadline
        String userInput = targetIndex.getOneBased() + " " + PREFIX_DEADLINE + nonEmptyDeadline;
        DeadlineCommand expectedCommand =
                new DeadlineCommand(INDEX_FIRST_PERSON, Deadline.fromString(nonEmptyDeadline));
        assertParseSuccess(parser, userInput, expectedCommand);

        // no Deadline (clear)
        userInput = targetIndex.getOneBased() + " " + PREFIX_DEADLINE;
        expectedCommand = new DeadlineCommand(INDEX_FIRST_PERSON, Deadline.fromString(""));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeadlineCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, DeadlineCommand.COMMAND_WORD, expectedMessage);

        // no index (but has a syntactically valid future deadline)
        // If your test class already freezes the clock:
        String validFuture = LocalDate.now(FIXED_CLOCK).plusDays(1).toString();
        assertParseFailure(parser,
                DeadlineCommand.COMMAND_WORD + " " + PREFIX_DEADLINE + validFuture,
                expectedMessage);
    }

}
