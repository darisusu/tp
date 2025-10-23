package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;

/**
 * Sidebar panel containing navigation buttons.
 */
public class SidebarPanel extends UiPart<Region> {
    private static final String FXML = "SidebarPanel.fxml";

    private SidebarListener listener;

    public SidebarPanel() {
        super(FXML);
    }

    /**
     * Used by MainWindow to react to sidebar events.
     */
    public void setSidebarListener(SidebarListener listener) {
        this.listener = listener;
    }

    @FXML
    private void handleClientsClicked() {
        if (listener != null) {
            listener.onShowClients();
        }
    }

    @FXML
    private void handleDashboardClicked() {
        if (listener != null) {
            listener.onShowDashboard();
        }
    }

    @FXML
    private void handleFile() {
        if (listener != null) {
            listener.onExit();
        }
    }

    @FXML
    private void handleHelp() {
        if (listener != null) {
            listener.onHelp();

        }
    }

    /**
     * Interface for MainWindow to implement, so Sidebar can notify it.
     */
    public interface SidebarListener {
        void onShowDashboard();
        void onShowClients();
        void onExit();
        void onHelp();
    }
}
