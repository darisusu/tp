package seedu.address.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.web.WebView;
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

    private static final List<Extension> MARKDOWN_EXTENSIONS = List.of(TablesExtension.create());

    private static final Parser MARKDOWN_PARSER =
            Parser.builder().extensions(MARKDOWN_EXTENSIONS).build();

    private static final HtmlRenderer HTML_RENDERER =
            HtmlRenderer.builder().extensions(MARKDOWN_EXTENSIONS).build();

    private static final String HTML_TEMPLATE_PREFIX = """
<html><head><meta charset="UTF-8" />
<style>
body {
    background-color: #383838;
    color: #e6e6e6;
    font-family: 'Segoe UI', 'Segoe UI Semibold', sans-serif;
    margin: 0;
    padding: 16px;
}

h1, h2, h3, h4, h5 {
    color: white;
    font-weight: 300;
}

a {
    color: #00b4d8;
    text-decoration: none;
}
a:hover {
    color: #4dabf7;
}

code, pre {
    font-family: 'Consolas', 'Courier New', monospace;
    background: #2e2e2e;
    color: #f8f8f8;
    padding: 2px 4px;
    border-radius: 4px;
}
pre {
    padding: 10px;
    border-radius: 6px;
    overflow: auto;
}

ul {
    padding-left: 20px;
}

li {
    margin-bottom: 6px;
}

/* Table styling */
table {
    border-collapse: collapse;
    width: 100%;
    margin-top: 10px;
    margin-bottom: 10px;
    background-color: #383838;
    border-radius: 6px;
    overflow: hidden;
}

th, td {
    border: 1px solid #4a4a4a;
    padding: 8px 10px;
    text-align: left;
    font-size: 11pt;
    color: #f5f5f5;
}

th {
    background-color: #2a2a2a;
    color: #00b4d8;
    font-weight: 600;
}

tr:nth-child(even) {
    background-color: #3c3e3f;
}

tr:nth-child(odd) {
    background-color: #515658;
}
</style></head><body>
                                """;

    private static final String HTML_TEMPLATE_SUFFIX = "</body></html>";



    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private WebView commandReferenceView;

    @FXML
    private Hyperlink userGuideLink;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        commandReferenceView.getEngine().setJavaScriptEnabled(false);
        commandReferenceView.getEngine().loadContent(loadCommandReferenceHtml());
        commandReferenceView.setContextMenuEnabled(false);
        userGuideLink.setText(USERGUIDE_URL);
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

    private String loadCommandReferenceHtml() {
        String markdown = loadCommandReferenceMarkdown();
        Node document = MARKDOWN_PARSER.parse(markdown);
        String htmlBody = HTML_RENDERER.render(document);
        return HTML_TEMPLATE_PREFIX + htmlBody + HTML_TEMPLATE_SUFFIX;
    }

}
