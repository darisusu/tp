package seedu.address.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;


/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://se-education.org/addressbook-level3/UserGuide.html";

    public static final String HELP_MESSAGE = "Need a quick refresher?"
            + "\nBrowse the full command reference below...";
    public static final String COMMAND_REFERENCE_RESOURCE = "/help/CommandReference.md";
    public static final String COMMAND_REFERENCE_FALLBACK = "Command reference unavailable. "
            + "Please make sure CommandReference.md is packaged with the app.";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private Label helpTitleLabel;

    @FXML
    private ScrollPane commandReferenceScroll;

    @FXML
    private VBox commandReferenceContainer;

    @FXML
    private Hyperlink userGuideLink;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpTitleLabel.setText("Need a hand?");
        helpMessage.setText(HELP_MESSAGE);
        userGuideLink.setText(USERGUIDE_URL);
        commandReferenceScroll.setFitToWidth(true);
        populateCommandReference(loadCommandReferenceMarkdown());
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }


    /**
     * Opens the user guide in the system browser if supported. If opening fails, the URL is copied instead.
     */
    @FXML
    private void openUserGuide() {
        if (!Desktop.isDesktopSupported()) {
            logger.warning("Desktop browsing is not supported. Copying URL instead.");
            copyUrl();
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            logger.warning("Browse action is not supported. Copying URL instead.");
            copyUrl();
            return;
        }

        try {
            desktop.browse(java.net.URI.create(USERGUIDE_URL));
        } catch (IOException e) {
            logger.warning("Failed to open user guide in browser: " + e.getMessage());
            copyUrl();
        }
    }

    private String loadCommandReferenceMarkdown() {
        try (InputStream inputStream = HelpWindow.class.getResourceAsStream(COMMAND_REFERENCE_RESOURCE)) {
            if (inputStream == null) {
                logger.warning("Command reference resource not found: " + COMMAND_REFERENCE_RESOURCE);
                return COMMAND_REFERENCE_FALLBACK;
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.warning("Failed to load command reference: " + e.getMessage());
            return COMMAND_REFERENCE_FALLBACK;
        }
    }

    private void populateCommandReference(String markdownContent) {
        if (COMMAND_REFERENCE_FALLBACK.equals(markdownContent)) {
            Label fallbackLabel = new Label(COMMAND_REFERENCE_FALLBACK);
            fallbackLabel.getStyleClass().add("markdown-fallback");
            fallbackLabel.setWrapText(true);
            commandReferenceContainer.getChildren().setAll(fallbackLabel);
            return;
        }

        MarkdownRenderer renderer = new MarkdownRenderer();
        List<Node> renderedNodes = renderer.render(markdownContent);
        commandReferenceContainer.getChildren().setAll(renderedNodes);
    }
}
