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

/**
 * UI component for the Dashboard screen.
 *
 * <p>This panel renders a two-column layout defined in {@code DashboardPanel.fxml}.
 * The left column is reserved for future content (e.g., upcoming sessions),
 * while the right column displays a live view of <em>unpaid</em> clients sorted by
 * payment deadline (earliest first).</p>
 *
 * <p>The right list is built by wrapping the app’s master/filtered person list in a
 * {@link javafx.collections.transformation.FilteredList} (unpaid-only) and then a
 * {@link javafx.collections.transformation.SortedList} (by deadline). Because these
 * wrappers observe the same underlying {@link javafx.collections.ObservableList},
 * any add/edit/delete or status change performed in the Clients tab is reflected here
 * automatically without additional wiring.</p>
 *
 * <h2>FXML injection</h2>
 * <ul>
 *   <li>{@code leftColumn} – placeholder {@link javafx.scene.layout.VBox} for the left side.</li>
 *   <li>{@code rightColumn} – container {@link javafx.scene.layout.VBox} for the right side.</li>
 *   <li>{@code rightListPlaceholder} – resizable placeholder (e.g., {@code StackPane})
 *       into which the {@link PersonListPanel} root (a {@link javafx.scene.control.ListView})
 *       is inserted.</li>
 * </ul>
 *
 * <p>Usage: construct the panel, then call {@link #bindRightList(ObservableList)} exactly once
 * from {@code MainWindow} (after Logic is available) to connect it to the app’s list.</p>
 *
 * @see seedu.address.ui.PersonListPanel
 * @see seedu.address.model.person.Person
 * @see seedu.address.model.person.PersonDeadlineComparator
 */
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
