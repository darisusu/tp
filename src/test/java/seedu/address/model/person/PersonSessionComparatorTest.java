package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

/**
 * Tests for {@link PersonSessionComparator}.
 */
public class PersonSessionComparatorTest {

    private static final ZoneId ZONE = ZoneOffset.UTC;

    @Test
    public void compare_upcomingSessions_ordersBySoonest() {
        Clock clock = Clock.fixed(Instant.parse("2098-01-01T00:00:00Z"), ZONE);
        Session earlySession = Session.fromString("2099-01-01 10:00", clock);
        Session laterSession = Session.fromString("2099-02-01 10:00", clock);

        Person early = buildPersonWithSession("Alice", earlySession);
        Person late = buildPersonWithSession("Bob", laterSession);

        PersonSessionComparator comparator = new PersonSessionComparator();

        assertTrue(comparator.compare(early, late) < 0);
        assertTrue(comparator.compare(late, early) > 0);
    }

    @Test
    public void compare_personWithoutUpcomingSession_sortsAfterUpcoming() {
        MutableClock mutableClock = new MutableClock(Instant.parse("2099-01-01T00:00:00Z"), ZONE);
        Session expiredSession = Session.fromString("2099-01-05 12:00", mutableClock);
        // Advance the clock so that the one-off session is now in the past
        mutableClock.setInstant(Instant.parse("2100-01-01T00:00:00Z"));

        Clock futureClock = Clock.fixed(Instant.parse("2099-01-01T00:00:00Z"), ZONE);
        Session upcomingSession = Session.fromString("2099-02-01 12:00", futureClock);

        Person noUpcoming = buildPersonWithSession("Cara", expiredSession);
        Person upcoming = buildPersonWithSession("Dan", upcomingSession);

        PersonSessionComparator comparator = new PersonSessionComparator();

        assertTrue(comparator.compare(noUpcoming, upcoming) > 0);
        assertTrue(comparator.compare(upcoming, noUpcoming) < 0);
    }

    @Test
    public void compare_bothWithoutUpcomingSessions_equal() {
        MutableClock clockA = new MutableClock(Instant.parse("2099-01-01T00:00:00Z"), ZONE);
        Session pastSessionA = Session.fromString("2099-01-05 08:00", clockA);
        clockA.setInstant(Instant.parse("2100-01-01T00:00:00Z"));

        MutableClock clockB = new MutableClock(Instant.parse("2099-01-01T00:00:00Z"), ZONE);
        Session pastSessionB = Session.fromString("2099-01-06 08:00", clockB);
        clockB.setInstant(Instant.parse("2100-01-01T00:00:00Z"));

        Person first = buildPersonWithSession("Eve", pastSessionA);
        Person second = buildPersonWithSession("Finn", pastSessionB);

        assertEquals(0, new PersonSessionComparator().compare(first, second));
    }

    @Test
    public void compare_nullSession_consideredAfterUpcoming() {
        Clock clock = Clock.fixed(Instant.parse("2099-01-01T00:00:00Z"), ZONE);
        Session upcomingSession = Session.fromString("2099-03-01 12:00", clock);

        Person withSession = buildPersonWithSession("Gail", upcomingSession);
        Person withoutSession = buildPersonWithNullSession("Hank");

        PersonSessionComparator comparator = new PersonSessionComparator();

        assertTrue(comparator.compare(withoutSession, withSession) > 0);
        assertTrue(comparator.compare(withSession, withoutSession) < 0);
    }

    private Person buildPersonWithSession(String name, Session session) {
        Person template = new PersonBuilder().withName(name).build();
        return new Person(template.getName(), template.getPhone(), template.getEmail(), template.getAddress(),
                template.getGoal(), template.getHeight(), template.getWeight(), template.getAge(),
                template.getGender(), template.getDeadline(), template.getPaymentStatus(), template.getBodyfat(),
                session, new HashSet<>(template.getTags()));
    }

    private Person buildPersonWithNullSession(String name) {
        Person template = new PersonBuilder().withName(name).build();
        return new Person(template.getName(), template.getPhone(), template.getEmail(), template.getAddress(),
                template.getGoal(), template.getHeight(), template.getWeight(), template.getAge(),
                template.getGender(), template.getDeadline(), template.getPaymentStatus(), template.getBodyfat(),
                template.getSession(), new HashSet<>(template.getTags())) {
            @Override
            public Session getSession() {
                return null;
            }
        };
    }

    private static class MutableClock extends Clock {
        private Instant instant;
        private final ZoneId zone;

        private MutableClock(Instant instant, ZoneId zone) {
            this.instant = instant;
            this.zone = zone;
        }

        void setInstant(Instant instant) {
            this.instant = instant;
        }

        @Override
        public ZoneId getZone() {
            return zone;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return new MutableClock(instant, zone);
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
