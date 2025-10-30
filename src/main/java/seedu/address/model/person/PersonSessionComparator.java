package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Compares two Persons based on the date/time of their next upcoming session.
 * Persons without upcoming sessions are placed at the end.
 */
public class PersonSessionComparator implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        Session s1 = p1.getSession();
        Session s2 = p2.getSession();
        LocalDateTime n1 = s1 == null ? null : s1.getNextOccurrence().orElse(null);
        LocalDateTime n2 = s2 == null ? null : s2.getNextOccurrence().orElse(null);

        if (n1 == null && n2 == null) {
            return 0;
        } else if (n1 == null) {
            return 1; // p1 has no upcoming session → later in list
        } else if (n2 == null) {
            return -1; // p2 has no upcoming session → earlier in list
        }

        // earlier (sooner) sessions come first
        return n1.compareTo(n2);
    }
}
