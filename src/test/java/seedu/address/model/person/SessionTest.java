package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class SessionTest {

    @Test
    public void fromString_validWeeklyMultiSlot_returnsCanonical() {
        Session session = Session.fromString("weekly:mon-1800-1930-tue-1800-1900");
        assertEquals("WEEKLY:MON-1800-1930-TUE-1800-1900", session.toStorageString());
    }

    @Test
    public void fromString_legacyWeekly_returnsCanonical() {
        Session session = Session.fromString("weekly:monday 18:00");
        assertEquals("WEEKLY:MON-1800-1800", session.toStorageString());
    }

    @Test
    public void fromString_validOneOff_returnsCanonical() {
        String date = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        Session session = Session.fromString(date + " 09:30");
        assertEquals(date + " 09:30", session.toStorageString());
    }

    @Test
    public void fromString_invalidFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                Session.MESSAGE_CONSTRAINTS_FORMAT, () -> Session.fromString("invalid"));
    }

    @Test
    public void fromString_invalidTime_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                Session.MESSAGE_CONSTRAINTS_TIME, () -> Session.fromString("WEEKLY:MONDAY 25:00"));
    }

    @Test
    public void fromString_invalidDay_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                Session.MESSAGE_CONSTRAINTS_DAY, () -> Session.fromString("WEEKLY:FUNDAY 18:00"));
    }

    @Test
    public void fromString_invalidTimeRange_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                Session.MESSAGE_CONSTRAINTS_TIME_RANGE, () -> Session.fromString("WEEKLY:MON-1900-1800"));
    }

    @Test
    public void fromString_overlappingWeeklySlots_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                Session.MESSAGE_CONSTRAINTS_OVERLAP, () -> Session.fromString("WEEKLY:MON-1800-1900-MON-1830-1930"));
    }

    @Test
    public void fromString_pastDate_throwsIllegalArgumentException() {
        String pastDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        assertThrows(IllegalArgumentException.class,
                Session.MESSAGE_CONSTRAINTS_PAST_DATE, () -> Session.fromString(pastDate + " 10:00"));
    }

    @Test
    public void conflictsWith_sameWeekly_returnsTrue() {
        Session first = Session.fromString("WEEKLY:MON-1800-1930");
        Session second = Session.fromString("weekly:mon-1800-1930");
        assertTrue(first.conflictsWith(second));
    }

    @Test
    public void conflictsWith_weeklyAndOneOffMatching_returnsTrue() {
        Session weekly = Session.fromString("WEEKLY:FRI-0900-1100");
        // Account for if test runs on a Friday but after 0930
        String upcomingFriday = LocalDate.now()
                .plusDays(((5 - LocalDate.now().getDayOfWeek().getValue() + 7) % 7 + 7) % 7)
                .plusWeeks(((5 - LocalDate.now().getDayOfWeek().getValue()) % 7 == 0) ? 1 : 0)
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
        Session oneOff = Session.fromString(upcomingFriday + " 09:30");
        assertTrue(weekly.conflictsWith(oneOff));
    }

    @Test
    public void conflictsWith_differentSessions_returnsFalse() {
        Session weekly = Session.fromString("WEEKLY:MON-0900-1000");
        Session otherWeekly = Session.fromString("WEEKLY:TUE-1000-1100");
        assertFalse(weekly.conflictsWith(otherWeekly));
    }
}
