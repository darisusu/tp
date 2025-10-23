package seedu.address.model.person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;


public final class PersonDeadlineComparator implements Comparator<Person> {

    private final boolean ascending;

    public PersonDeadlineComparator(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int compare(Person a, Person b) {
        Optional<LocalDate> da = extract(a);
        Optional<LocalDate> db = extract(b);

        int cmp;
        if (da.isPresent() && db.isPresent()) {
            cmp = da.get().compareTo(db.get());
        } else if (da.isPresent()) {
            cmp = -1;
        } else if (db.isPresent()) {
            cmp = 1;
        } else {
            cmp = 0;
        }
        return ascending ? cmp : -cmp;
    }

    private static Optional<LocalDate> extract(Person p) {
        return p.getDeadline().asOptional();
    }
}
