package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonDeadlineComparator;

public class DashboardPanel extends UiPart<Region> {
    private static final String FXML = "DashboardPanel.fxml";

    @FXML private VBox leftColumn;
    @FXML private VBox rightColumn;
    @FXML private Pane rightListPlaceholder;

    private PersonListPanel unpaidListPanel;

    public DashboardPanel() {
        super(FXML);
    }

    /** Call this once from MainWindow after construction. */
    public void bindRightList(ObservableList<Person> masterList) {
        // Unpaid only  ->  p.getPaymentStatus().value == false
        FilteredList<Person> unpaidOnly = new FilteredList<>(masterList, p ->
                p.getPaymentStatus() != null && !p.getPaymentStatus().value);

        // Sort by deadline ascending
        SortedList<Person> unpaidByDeadline =
                new SortedList<>(unpaidOnly, new PersonDeadlineComparator(true));

        // Render into the right column
        unpaidListPanel = new PersonListPanel(unpaidByDeadline);
        Node listRoot = unpaidListPanel.getRoot();

        rightListPlaceholder.getChildren().setAll(listRoot);
        VBox.setVgrow(listRoot, Priority.ALWAYS);
    }
}
