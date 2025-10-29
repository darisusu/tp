package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;

/**
 * A simplified UI component that displays only the name and upcoming session of a {@code Person}.
 */
public class SessionCard extends UiPart<Region> {

    private static final String FXML = "SessionListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label session;

    public SessionCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        id.setText(displayedIndex + ". ");
        name.setText("Name: " + person.getName().fullName);
        session.setText("Session: " + valueOrPlaceholder(person.getSession(), Session::toStorageString));
    }

    private <T> String valueOrPlaceholder(T value, java.util.function.Function<T, String> mapper) {
        return value == null ? "Not specified" : mapper.apply(value);
    }
}
