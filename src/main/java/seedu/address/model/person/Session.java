package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a training session slot for a person.
 * Guarantees: immutable; is valid as declared in {@link #fromString(String)}
 */
public class Session {

    public static final String MESSAGE_CONSTRAINTS_FORMAT = "Error: Session format must be either: "
            + "YYYY-MM-DD HH:MM, [WEEKLY|BIWEEKLY]:DAY-START-END(-DAY-START-END)*, "
            + "or MONTHLY:DD HH:MM.";
    public static final String MESSAGE_CONSTRAINTS_TIME = "Error: Invalid time. Use 24-hour format (HH:mm or HHmm).";
    public static final String MESSAGE_CONSTRAINTS_DAY =
            "Error: Invalid day. Use MONDAY, TUESDAY, or their three-letter abbreviations.";
    public static final String MESSAGE_CONSTRAINTS_PAST_DATE = "Error: Session date cannot be in the past.";
    public static final String MESSAGE_CONSTRAINTS_TIME_RANGE =
            "Error: Session end time must be after the start time.";
    public static final String MESSAGE_CONSTRAINTS_OVERLAP =
            "Error: Weekly session slots must not overlap.";

    private static final Pattern ONE_OFF_PATTERN = Pattern.compile(
            "^(?<date>\\d{4}-\\d{2}-\\d{2})\\s+(?<time>\\d{2}:\\d{2})$");
    private static final Pattern MONTHLY_PATTERN = Pattern.compile(
            "^(?<type>MONTHLY):(?<day>\\d{1,2})\\s+(?<time>\\d{2}:\\d{2})$", Pattern.CASE_INSENSITIVE);
    private static final Pattern LEGACY_RECURRING_PATTERN = Pattern.compile(
            "^(?<type>WEEKLY|BIWEEKLY):(?<day>[^\\s]+)\\s+(?<time>\\d{2}:\\d{2})$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern MULTI_SLOT_PATTERN = Pattern.compile(
            "^(?<type>WEEKLY|BIWEEKLY):(?<slots>.+)$", Pattern.CASE_INSENSITIVE);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_FORMATTER_COMPACT = DateTimeFormatter.ofPattern("HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final int MONTH_LOOKAHEAD = 24; // For conflict checks between monthly and weekly/biweekly sessions

    private final SessionType type;
    private final LocalDateTime oneOffDateTime;
    private final int dayOfMonth;
    private final LocalTime time;
    private final List<RecurringSlot> recurringSlots;
    private final String value;

    private final Clock clock;

    private Session(SessionType type, LocalDateTime oneOffDateTime,
            int dayOfMonth, LocalTime time, List<RecurringSlot> recurringSlots, String value, Clock clock) {
        this.type = type;
        this.oneOffDateTime = oneOffDateTime;
        this.dayOfMonth = dayOfMonth;
        this.time = time;
        this.recurringSlots = List.copyOf(requireNonNull(recurringSlots));
        this.value = value;
        this.clock = clock;
    }

    /**
     * Creates a {@code Session} by parsing the provided {@code raw} string.
     */
    public static Session fromString(String raw) {
        return fromString(raw, Clock.systemDefaultZone());
    }

    /**
     * Internal factory that allows injecting a {@link Clock} for testing.
     */
    static Session fromString(String raw, Clock clock) {
        requireNonNull(raw);
        requireNonNull(clock);
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_FORMAT);
        }

        Matcher oneOffMatcher = ONE_OFF_PATTERN.matcher(trimmed);
        if (oneOffMatcher.matches()) {
            LocalDate date = parseDate(oneOffMatcher.group("date"));
            LocalTime time = parseTime(oneOffMatcher.group("time"));
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            if (dateTime.isBefore(LocalDateTime.now(clock))) {
                throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_PAST_DATE);
            }
            String canonical = dateTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"));
            return new Session(SessionType.ONE_OFF, dateTime, -1, time, List.of(), canonical, clock);
        }

        Matcher monthlyMatcher = MONTHLY_PATTERN.matcher(trimmed);
        if (monthlyMatcher.matches()) {
            SessionType type = SessionType.fromString(monthlyMatcher.group("type"));
            LocalTime time = parseTime(monthlyMatcher.group("time"));
            int dayOfMonth = parseDayOfMonth(monthlyMatcher.group("day"));
            String canonicalMonthly = type + ":" + String.format(Locale.ROOT, "%02d", dayOfMonth)
                    + " " + formatTime(time);
            return new Session(type, null, dayOfMonth, time, List.of(), canonicalMonthly, clock);
        }

        Matcher legacyRecurringMatcher = LEGACY_RECURRING_PATTERN.matcher(trimmed);
        if (legacyRecurringMatcher.matches()) {
            SessionType type = SessionType.fromString(legacyRecurringMatcher.group("type"));
            DayOfWeek day = parseDayOfWeek(legacyRecurringMatcher.group("day"));
            LocalTime start = parseTime(legacyRecurringMatcher.group("time"));
            RecurringSlot slot = new RecurringSlot(day, start, start);
            List<RecurringSlot> sortedSlots = sortRecurringSlots(List.of(slot));
            String canonical = buildRecurringCanonical(type, sortedSlots);
            return new Session(type, null, -1, null, sortedSlots, canonical, clock);
        }

        Matcher multiSlotMatcher = MULTI_SLOT_PATTERN.matcher(trimmed);
        if (multiSlotMatcher.matches()) {
            SessionType type = SessionType.fromString(multiSlotMatcher.group("type"));
            List<RecurringSlot> slots = parseRecurringSlots(multiSlotMatcher.group("slots"));
            validateRecurringSlots(slots);
            List<RecurringSlot> sortedSlots = sortRecurringSlots(slots);
            String canonical = buildRecurringCanonical(type, sortedSlots);
            return new Session(type, null, -1, null, sortedSlots, canonical, clock);
        }

        throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_FORMAT);
    }

    /**
     * Returns true if the given {@code raw} string can be parsed into a {@code Session}.
     */
    public static boolean isValidSession(String raw) {
        try {
            fromString(raw);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_FORMAT, ex);
        }
    }

    private static LocalTime parseTime(String timeStr) {
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_TIME, ex);
        }
    }

    private static LocalTime parseSlotTime(String timeStr) {
        String trimmed = timeStr.trim();
        try {
            if (trimmed.contains(":")) {
                return LocalTime.parse(trimmed, TIME_FORMATTER);
            }
            return LocalTime.parse(trimmed, TIME_FORMATTER_COMPACT);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_TIME, ex);
        }
    }

    private static DayOfWeek parseDayOfWeek(String raw) {
        String trimmed = raw.trim().toUpperCase(Locale.ROOT);
        switch (trimmed) {
        case "MON":
        case "MONDAY":
            return DayOfWeek.MONDAY;
        case "TUE":
        case "TUES":
        case "TUESDAY":
            return DayOfWeek.TUESDAY;
        case "WED":
        case "WEDNESDAY":
            return DayOfWeek.WEDNESDAY;
        case "THU":
        case "THUR":
        case "THURS":
        case "THURSDAY":
            return DayOfWeek.THURSDAY;
        case "FRI":
        case "FRIDAY":
            return DayOfWeek.FRIDAY;
        case "SAT":
        case "SATURDAY":
            return DayOfWeek.SATURDAY;
        case "SUN":
        case "SUNDAY":
            return DayOfWeek.SUNDAY;
        default:
            try {
                return DayOfWeek.valueOf(trimmed);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_DAY, ex);
            }
        }
    }

    private static int parseDayOfMonth(String raw) {
        try {
            int value = Integer.parseInt(raw);
            if (value < 1 || value > 31) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_FORMAT, ex);
        }
    }

    private static String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    private static String formatTimeCompact(LocalTime time) {
        return time.format(TIME_FORMATTER_COMPACT);
    }

    private static List<RecurringSlot> parseRecurringSlots(String slotsRaw) {
        String sanitized = slotsRaw.replaceAll("\\s+", "");
        if (sanitized.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_FORMAT);
        }
        String[] tokens = sanitized.split("-");
        if (tokens.length < 3 || tokens.length % 3 != 0) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_FORMAT);
        }

        List<RecurringSlot> slots = new ArrayList<>();
        for (int i = 0; i < tokens.length; i += 3) {
            DayOfWeek day = parseDayOfWeek(tokens[i]);
            LocalTime start = parseSlotTime(tokens[i + 1]);
            LocalTime end = parseSlotTime(tokens[i + 2]);
            if (!end.isAfter(start)) {
                throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_TIME_RANGE);
            }
            slots.add(new RecurringSlot(day, start, end));
        }
        return slots;
    }

    private static void validateRecurringSlots(List<RecurringSlot> slots) {
        if (slots.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_FORMAT);
        }
        List<RecurringSlot> sorted = sortRecurringSlots(slots);
        for (int i = 1; i < sorted.size(); i++) {
            RecurringSlot previous = sorted.get(i - 1);
            RecurringSlot current = sorted.get(i);
            if (previous.overlaps(current)) {
                throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_OVERLAP);
            }
        }
    }

    private static String buildRecurringCanonical(SessionType type, List<RecurringSlot> slots) {
        List<RecurringSlot> sorted = sortRecurringSlots(slots);
        List<String> parts = new ArrayList<>(sorted.size());
        for (RecurringSlot slot : sorted) {
            parts.add(slot.toCanonicalString());
        }
        return type + ":" + String.join("-", parts);
    }

    private static List<RecurringSlot> sortRecurringSlots(List<RecurringSlot> slots) {
        List<RecurringSlot> sorted = new ArrayList<>(slots);
        sorted.sort(Comparator.naturalOrder());
        return sorted;
    }

    public SessionType getType() {
        return type;
    }

    public String toStorageString() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, oneOffDateTime, dayOfMonth, time, recurringSlots);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Session)) {
            return false;
        }

        Session otherSession = (Session) other;
        return type == otherSession.type
                && Objects.equals(oneOffDateTime, otherSession.oneOffDateTime)
                && dayOfMonth == otherSession.dayOfMonth
                && Objects.equals(time, otherSession.time)
                && Objects.equals(recurringSlots, otherSession.recurringSlots);
    }

    /**
     * Returns true if this session conflicts with {@code other}.
     */
    public boolean conflictsWith(Session other) {
        requireNonNull(other);
        if (this.type == SessionType.ONE_OFF) {
            return other.occursOn(oneOffDateTime);
        }
        if (other.type == SessionType.ONE_OFF) {
            return this.occursOn(other.oneOffDateTime);
        }

        if (this.type == SessionType.MONTHLY && other.type == SessionType.MONTHLY) {
            return this.dayOfMonth == other.dayOfMonth && this.time.equals(other.time);
        }

        if (this.type == SessionType.MONTHLY) {
            return conflictsMonthlyWithRecurring(this, other);
        }
        if (other.type == SessionType.MONTHLY) {
            return conflictsMonthlyWithRecurring(other, this);
        }

        // Both weekly or biweekly
        return hasOverlappingSlots(other);
    }

    private boolean occursOn(LocalDateTime candidate) {
        switch (type) {
        case ONE_OFF:
            return oneOffDateTime.equals(candidate);
        case WEEKLY:
        case BIWEEKLY:
            return recurringSlots.stream().anyMatch(slot -> slot.occursOn(candidate));
        case MONTHLY:
            return candidate.getDayOfMonth() == dayOfMonth && candidate.toLocalTime().equals(time);
        default:
            throw new IllegalStateException("Unhandled session type: " + type);
        }
    }

    private boolean hasOverlappingSlots(Session other) {
        for (RecurringSlot slot : recurringSlots) {
            for (RecurringSlot otherSlot : other.recurringSlots) {
                if (slot.overlaps(otherSlot)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the next upcoming LocalDateTime this session occurs at, relative to the session's clock.
     * If no upcoming occurrence exists (e.g. a one-off in the past), returns Optional.empty().
     */
    public Optional<LocalDateTime> getNextOccurrence() {
        LocalDateTime now = LocalDateTime.now(clock);

        switch (type) {
        case ONE_OFF:
            if (oneOffDateTime != null && (oneOffDateTime.isAfter(now) || oneOffDateTime.isEqual(now))) {
                return Optional.of(oneOffDateTime);
            }
            return Optional.empty();

        case WEEKLY:
        case BIWEEKLY:
            if (recurringSlots.isEmpty()) {
                return Optional.empty();
            }
            LocalDateTime soonest = null;
            for (RecurringSlot slot : recurringSlots) {
                // days until that weekday (0..6)
                int daysUntil = (slot.day.getValue() - now.getDayOfWeek().getValue() + 7) % 7;
                LocalDate candidateDate = now.toLocalDate().plusDays(daysUntil);
                LocalDateTime candidate = LocalDateTime.of(candidateDate, slot.start);

                // If candidate is before now (or it's today but slot already passed), advance
                boolean sameDayButPassed = daysUntil == 0 && (slot.isInstant() ? candidate.toLocalTime().isBefore(now.toLocalTime())
                        : slot.end.isBefore(now.toLocalTime()) || !slot.contains(now.toLocalTime()) && slot.start.isBefore(now.toLocalTime()));
                if (candidate.isBefore(now) || sameDayButPassed) {
                    int weeksToAdd = (type == SessionType.BIWEEKLY) ? 2 : 1;
                    candidate = candidate.plusWeeks(weeksToAdd);
                }

                if (soonest == null || candidate.isBefore(soonest)) {
                    soonest = candidate;
                }
            }
            return Optional.ofNullable(soonest);

        case MONTHLY:
            if (time == null) {
                return Optional.empty();
            }
            YearMonth currentMonth = YearMonth.from(now.toLocalDate());
            int safeDay = Math.min(dayOfMonth, currentMonth.lengthOfMonth());
            LocalDate candidateDate = currentMonth.atDay(safeDay);
            LocalDateTime candidate = LocalDateTime.of(candidateDate, time);
            if (!candidate.isAfter(now)) {
                YearMonth nextMonth = currentMonth.plusMonths(1);
                int safeNextDay = Math.min(dayOfMonth, nextMonth.lengthOfMonth());
                candidateDate = nextMonth.atDay(safeNextDay);
                candidate = LocalDateTime.of(candidateDate, time);
            }
            return Optional.of(candidate);

        default:
            return Optional.empty();
        }
    }

    private static boolean conflictsMonthlyWithRecurring(Session monthly, Session recurring) {
        if (recurring.recurringSlots.isEmpty()) {
            return false;
        }
        YearMonth start = YearMonth.from(LocalDate.now(monthly.clock));
        for (int i = 0; i < MONTH_LOOKAHEAD; i++) {
            YearMonth current = start.plusMonths(i);
            if (monthly.dayOfMonth > current.lengthOfMonth()) {
                continue;
            }
            LocalDate date = current.atDay(monthly.dayOfMonth);
            for (RecurringSlot slot : recurring.recurringSlots) {
                if (date.getDayOfWeek() == slot.day && slot.contains(monthly.time)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final class RecurringSlot implements Comparable<RecurringSlot> {
        private final DayOfWeek day;
        private final LocalTime start;
        private final LocalTime end;

        private RecurringSlot(DayOfWeek day, LocalTime start, LocalTime end) {
            this.day = requireNonNull(day);
            this.start = requireNonNull(start);
            this.end = requireNonNull(end);
        }

        private boolean isInstant() {
            return start.equals(end);
        }

        private boolean occursOn(LocalDateTime candidate) {
            if (candidate.getDayOfWeek() != day) {
                return false;
            }
            LocalTime candidateTime = candidate.toLocalTime();
            if (isInstant()) {
                return candidateTime.equals(start);
            }
            return !candidateTime.isBefore(start) && candidateTime.isBefore(end);
        }

        private boolean contains(LocalTime candidate) {
            if (isInstant()) {
                return candidate.equals(start);
            }
            return !candidate.isBefore(start) && candidate.isBefore(end);
        }

        private boolean overlaps(RecurringSlot other) {
            if (day != other.day) {
                return false;
            }
            if (isInstant() && other.isInstant()) {
                return start.equals(other.start);
            }
            if (isInstant()) {
                return !start.isBefore(other.start) && start.isBefore(other.end);
            }
            if (other.isInstant()) {
                return !other.start.isBefore(start) && other.start.isBefore(end);
            }
            return start.isBefore(other.end) && other.start.isBefore(end);
        }

        private String toCanonicalString() {
            return day.name().substring(0, 3) + "-" + formatTimeCompact(start) + "-" + formatTimeCompact(end);
        }

        @Override
        public int compareTo(RecurringSlot other) {
            int dayCompare = Integer.compare(day.getValue(), other.day.getValue());
            if (dayCompare != 0) {
                return dayCompare;
            }
            int startCompare = start.compareTo(other.start);
            if (startCompare != 0) {
                return startCompare;
            }
            return end.compareTo(other.end);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RecurringSlot)) {
                return false;
            }
            RecurringSlot other = (RecurringSlot) obj;
            return day == other.day && start.equals(other.start) && end.equals(other.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(day, start, end);
        }
    }

    /**
     * Represents the supported session recurrence types.
     */
    public enum SessionType {
        ONE_OFF,
        WEEKLY,
        BIWEEKLY,
        MONTHLY;

        static SessionType fromString(String raw) {
            try {
                return valueOf(raw.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_FORMAT, ex);
            }
        }
    }
}
