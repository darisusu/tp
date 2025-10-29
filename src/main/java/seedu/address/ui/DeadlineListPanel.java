package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons (simplified to show session info).
 */
public class DeadlineListPanel extends UiPart<Region> {
    private static final String FXML = "DeadlineListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(DeadlineListPanel.class);

    @FXML
    private ListView<Person> deadlineListView;

    /**
     * Creates a {@code DeadlineListPanel} with the given {@code ObservableList}.
     */
    public DeadlineListPanel(ObservableList<Person> personList) {
        super(FXML);
        deadlineListView.setItems(personList);
        deadlineListView.setCellFactory(listView -> new SessionListViewCell());
    }

    class SessionListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new DeadlineCard(person, getIndex() + 1).getRoot());
            }
        }
    }
}
