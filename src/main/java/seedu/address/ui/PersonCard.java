package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Address;
import seedu.address.model.person.Bodyfat;
import seedu.address.model.person.Deadline;
import seedu.address.model.person.Email;
import seedu.address.model.person.Goal;
import seedu.address.model.person.Paid;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Session;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label deadline;
    @FXML
    private Label goal;
    @FXML
    private Label height;
    @FXML
    private Label weight;
    @FXML
    private Label age;
    @FXML
    private Label gender;
    @FXML
    private Label bodyfat;
    @FXML
    private Label paid;
    @FXML
    private Label session;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText("Name: " + person.getName().fullName);
        phone.setText("Phone Number: " + valueOrPlaceholder(person.getPhone(), Phone::toString));
        address.setText("Address: " + valueOrPlaceholder(person.getAddress(), Address::toString));
        height.setText("Height: " + valueWithUnit(person.getHeight(), h -> h.value + " cm"));
        weight.setText("Weight: " + valueWithUnit(person.getWeight(), w -> w.value + " kg"));
        age.setText("Age: " + valueWithUnit(person.getAge(), a -> a.value + " years old"));
        gender.setText("Gender: " + valueOrPlaceholder(person.getGender(), g -> g.value));
        email.setText("Email: " + valueOrPlaceholder(person.getEmail(), Email::toString));
        deadline.setText("Payment Deadline: " + deadlineDisplay(person.getDeadline()));
        goal.setText("Personal Goal: " + goalDisplay(person.getGoal()));
        bodyfat.setText("Bodyfat Percentage: " + valueWithUnit(person.getBodyfat(), Bodyfat::toString, "%"));
        paid.setText("Payment Status: " + paymentDisplay(person.getPaymentStatus()));
        session.setText("Session: " + valueOrPlaceholder(person.getSession(), Session::toStorageString));
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    private <T> String valueOrPlaceholder(T value, java.util.function.Function<T, String> mapper) {
        return value == null ? "Not specified" : mapper.apply(value);
    }

    private String goalDisplay(Goal goalValue) {
        if (goalValue == null || goalValue.value.isEmpty()) {
            return "Not specified";
        }
        return goalValue.value;
    }

    private <T> String valueWithUnit(T value, java.util.function.Function<T, String> formatter) {
        return valueWithUnit(value, formatter, "");
    }

    private <T> String valueWithUnit(T value, java.util.function.Function<T, String> formatter, String suffix) {
        if (value == null) {
            return "Not specified";
        }
        String formatted = formatter.apply(value);
        return suffix.isEmpty() ? formatted : formatted + suffix;
    }

    private String deadlineDisplay(Deadline deadline) {
        if (deadline == null) {
            return "Not specified";
        }
        String storage = deadline.toStorageString();
        return storage.isEmpty() ? "Not specified" : storage;
    }

    private String paymentDisplay(Paid paidStatus) {
        if (paidStatus == null) {
            return "Not specified";
        }
        return paidStatus.value ? "Paid" : "Not Paid";
    }
}
