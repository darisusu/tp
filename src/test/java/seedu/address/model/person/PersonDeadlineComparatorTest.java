package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonDeadlineComparatorTest {

    private static final ZoneId ZONE = ZoneOffset.UTC;
    private static final Clock FIXED_CLOCK =
            Clock.fixed(LocalDate.of(2025, 1, 1).atStartOfDay(ZONE).toInstant(), ZONE);

    @BeforeEach
    void freezeClock() {
        Deadline.useClock(FIXED_CLOCK);
    }

    @AfterEach
    void unfreezeClock() {
        Deadline.useClock(Clock.systemDefaultZone());
    }

    @Test
    public void compare_ascending_ordersByEarliestAndNoDeadlineLast() {
        Person early = new PersonBuilder().withName("Alice")
                .withDeadline("2025-01-15").build();
        Person late = new PersonBuilder().withName("Bob")
                .withDeadline("2025-12-01").build();
        Person none = new PersonBuilder().withName("Cara")
                .withDeadline("") // clear / none
                .build();

        List<Person> list = Arrays.asList(none, late, early);
        list.sort(new PersonDeadlineComparator(true));

        // Expect: early (Jan 15) < late (Dec 1) < none
        assertEquals(Arrays.asList(early, late, none), list);
    }

    @Test
    public void compare_descending_ordersByLatestAndNoDeadlineLast() {
        Person early = new PersonBuilder().withName("Alice")
                .withDeadline("2025-01-15").build();
        Person late = new PersonBuilder().withName("Bob")
                .withDeadline("2025-12-01").build();
        Person none = new PersonBuilder().withName("Cara")
                .withDeadline("") // clear / none
                .build();

        List<Person> list = Arrays.asList(early, none, late);
        list.sort(new PersonDeadlineComparator(false));

        // Expect: late (Dec 1) > early (Jan 15) > none
        assertEquals(Arrays.asList(none, late, early), list);
    }

    @Test
    public void compare_bothNoDeadline_equal() {
        Person p1 = new PersonBuilder().withName("X").withDeadline("").build();
        Person p2 = new PersonBuilder().withName("Y").withDeadline("").build();

        int cmpAsc = new PersonDeadlineComparator(true).compare(p1, p2);
        int cmpDesc = new PersonDeadlineComparator(false).compare(p1, p2);

        assertEquals(0, cmpAsc);
        assertEquals(0, cmpDesc);
    }
}
