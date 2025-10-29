package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Deadline;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * A simplified UI component that displays only the name and deadline of a {@code Person}.
 */
public class DeadlineCard extends UiPart<Region> {

    private static final String FXML = "DeadlineListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label deadline;

    /**
     * Creates a {@code DeadlineCard} with the given {@code Person} and {@code Deadline} to display.
     */
    public DeadlineCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        name.setText("Name: " + person.getName().fullName);
        phone.setText(valueOrPlaceholder(person.getPhone(), Phone::toString));
        deadline.setText("Payment Deadline: " + deadlineDisplay(person.getDeadline()));
    }

    private <T> String valueOrPlaceholder(T value, java.util.function.Function<T, String> mapper) {
        return value == null ? "Not specified" : mapper.apply(value);
    }

    private String deadlineDisplay(Deadline deadline) {
        if (deadline == null) {
            return "Not specified";
        }
        String storage = deadline.toStorageString();
        return storage.isEmpty() ? "Not specified" : storage;
    }
}
