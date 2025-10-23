package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DashboardPanel extends UiPart<Region> {

    private static final String FXML = "DashboardPanel.fxml";

    @FXML
    private VBox leftColumn;

    @FXML
    private VBox rightColumn;

    public DashboardPanel() {
        super(FXML);
    }

    public VBox getLeftColumn() {
        return leftColumn;
    }

    public VBox getRightColumn() {
        return rightColumn;
    }
}
