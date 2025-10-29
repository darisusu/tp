package seedu.address.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.CodeBlock;
import org.commonmark.node.Emphasis;
import org.commonmark.node.Heading;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Link;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.TableBlock;
import org.commonmark.node.TableBody;
import org.commonmark.node.TableCell;
import org.commonmark.node.TableHead;
import org.commonmark.node.TableRow;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
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

    private static final String HEADING_STYLE_CLASS = "command-reference-heading";
    private static final String PARAGRAPH_STYLE_CLASS = "command-reference-paragraph";
    private static final String INLINE_STRONG_STYLE_CLASS = "command-reference-inline-strong";
    private static final String INLINE_EMPHASIS_STYLE_CLASS = "command-reference-inline-emphasis";
    private static final String INLINE_CODE_STYLE_CLASS = "command-reference-inline-code";
    private static final String INLINE_LINK_STYLE_CLASS = "command-reference-inline-link";
    private static final String CODE_BLOCK_STYLE_CLASS = "command-reference-code-block";
    private static final String BULLET_LIST_STYLE_CLASS = "command-reference-bullet-list";
    private static final String BULLET_STYLE_CLASS = "command-reference-bullet";
    private static final String LIST_CONTENT_STYLE_CLASS = "command-reference-list-content";
    private static final String TABLE_STYLE_CLASS = "command-reference-table";
    private static final String TABLE_CELL_STYLE_CLASS = "command-reference-table-cell";
    private static final String TABLE_HEADER_CELL_STYLE_CLASS = "command-reference-table-header-cell";



    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private ScrollPane commandReferenceScrollPane;

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
        helpMessage.setText(HELP_MESSAGE);
        commandReferenceScrollPane.setFitToWidth(true);
        commandReferenceContainer.setFillWidth(true);
        renderCommandReference();
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

    private void renderCommandReference() {
        String markdown = loadCommandReferenceMarkdown();
        Node document = MARKDOWN_PARSER.parse(markdown);
        List<javafx.scene.Node> renderedBlocks = renderMarkdownDocument(document);

        if (renderedBlocks.isEmpty()) {
            TextFlow fallback = createTextFlowFromText(COMMAND_REFERENCE_FALLBACK);
            commandReferenceContainer.getChildren().setAll(fallback);
            return;
        }

        commandReferenceContainer.getChildren().setAll(renderedBlocks);
    }

    private static List<javafx.scene.Node> renderMarkdownDocument(Node document) {
        List<javafx.scene.Node> blocks = new ArrayList<>();
        for (Node child = document.getFirstChild(); child != null; child = child.getNext()) {
            appendBlock(blocks, child);
        }
        return blocks;
    }

    private static void appendBlock(List<javafx.scene.Node> target, Node block) {
        if (block instanceof Heading) {
            target.add(createHeading((Heading) block));
            return;
        }

        if (block instanceof Paragraph) {
            target.add(createParagraph((Paragraph) block));
            return;
        }

        if (block instanceof CodeBlock) {
            target.add(createCodeBlock((CodeBlock) block));
            return;
        }

        if (block instanceof BulletList) {
            target.add(createBulletList((BulletList) block));
            return;
        }

        if (block instanceof OrderedList) {
            target.add(createOrderedList((OrderedList) block));
            return;
        }

        if (block instanceof TableBlock) {
            target.add(createTable((TableBlock) block));
            return;
        }

        if (block instanceof org.commonmark.node.ThematicBreak) {
            target.add(new Separator());
            return;
        }

        // Fallback: render nested children
        for (Node child = block.getFirstChild(); child != null; child = child.getNext()) {
            appendBlock(target, child);
        }
    }

    private static TextFlow createHeading(Heading heading) {
        TextFlow flow = createTextFlow(heading, HEADING_STYLE_CLASS,
                HEADING_STYLE_CLASS + "-h" + Math.min(heading.getLevel(), 4));
        flow.setMaxWidth(Double.MAX_VALUE);
        return flow;
    }

    private static TextFlow createParagraph(Paragraph paragraph) {
        TextFlow flow = createTextFlow(paragraph, PARAGRAPH_STYLE_CLASS);
        flow.setMaxWidth(Double.MAX_VALUE);
        return flow;
    }

    private static TextFlow createTextFlowFromText(String content) {
        TextFlow flow = new TextFlow();
        flow.getStyleClass().add(PARAGRAPH_STYLE_CLASS);
        flow.setLineSpacing(4);
        flow.setPrefWidth(0);
        flow.setMaxWidth(Double.MAX_VALUE);
        Text text = new Text(content);
        flow.getChildren().add(text);
        return flow;
    }

    private static Label createCodeBlock(CodeBlock codeBlock) {
        Label code = new Label(codeBlock.getLiteral().stripTrailing());
        code.getStyleClass().add(CODE_BLOCK_STYLE_CLASS);
        code.setWrapText(true);
        code.setMaxWidth(Double.MAX_VALUE);
        return code;
    }

    private static VBox createBulletList(BulletList list) {
        VBox listBox = new VBox();
        listBox.getStyleClass().add(BULLET_LIST_STYLE_CLASS);
        listBox.setSpacing(8);

        for (Node itemNode = list.getFirstChild(); itemNode != null; itemNode = itemNode.getNext()) {
            if (!(itemNode instanceof ListItem)) {
                continue;
            }
            listBox.getChildren().add(createBulletListItem((ListItem) itemNode, "\u2022"));
        }

        return listBox;
    }

    private static VBox createOrderedList(OrderedList list) {
        VBox listBox = new VBox();
        listBox.getStyleClass().add(BULLET_LIST_STYLE_CLASS);
        listBox.setSpacing(8);

        int index = 0;
        for (Node itemNode = list.getFirstChild(); itemNode != null; itemNode = itemNode.getNext()) {
            if (!(itemNode instanceof ListItem)) {
                continue;
            }
            int itemNumber = list.getStartNumber() + index;
            listBox.getChildren().add(createBulletListItem((ListItem) itemNode, itemNumber + "."));
            index++;
        }

        return listBox;
    }

    private static HBox createBulletListItem(ListItem listItem, String bulletText) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.TOP_LEFT);

        Label bullet = new Label(bulletText);
        bullet.getStyleClass().add(BULLET_STYLE_CLASS);
        bullet.setWrapText(true);
        bullet.setMinWidth(Region.USE_PREF_SIZE);

        VBox content = new VBox();
        content.getStyleClass().add(LIST_CONTENT_STYLE_CLASS);
        content.setSpacing(6);
        content.setFillWidth(true);

        for (Node child = listItem.getFirstChild(); child != null; child = child.getNext()) {
            appendBlock(content.getChildren(), child);
        }

        row.getChildren().addAll(bullet, content);
        HBox.setHgrow(content, Priority.ALWAYS);

        return row;
    }

    private static GridPane createTable(TableBlock tableBlock) {
        GridPane grid = new GridPane();
        grid.getStyleClass().add(TABLE_STYLE_CLASS);
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        int columnCount = determineTableColumnCount(tableBlock);
        if (columnCount > 0) {
            double columnPercent = 100.0 / columnCount;
            for (int i = 0; i < columnCount; i++) {
                ColumnConstraints constraints = new ColumnConstraints();
                constraints.setPercentWidth(columnPercent);
                constraints.setFillWidth(true);
                constraints.setHgrow(Priority.ALWAYS);
                grid.getColumnConstraints().add(constraints);
            }
        }

        int rowIndex = 0;
        for (Node section = tableBlock.getFirstChild(); section != null; section = section.getNext()) {
            if (section instanceof TableHead) {
                rowIndex = appendTableSection(grid, (TableHead) section, rowIndex, true);
            } else if (section instanceof TableBody) {
                rowIndex = appendTableSection(grid, (TableBody) section, rowIndex, false);
            }
        }

        return grid;
    }

    private static int appendTableSection(GridPane grid, Node section, int startingRow, boolean header) {
        int currentRow = startingRow;
        for (Node rowNode = section.getFirstChild(); rowNode != null; rowNode = rowNode.getNext()) {
            if (!(rowNode instanceof TableRow)) {
                continue;
            }

            int columnIndex = 0;
            for (Node cellNode = rowNode.getFirstChild(); cellNode != null; cellNode = cellNode.getNext()) {
                if (!(cellNode instanceof TableCell)) {
                    continue;
                }

                VBox cell = createTableCell((TableCell) cellNode, header);
                grid.add(cell, columnIndex, currentRow);
                columnIndex++;
            }

            currentRow++;
        }
        return currentRow;
    }

    private static int determineTableColumnCount(TableBlock tableBlock) {
        for (Node section = tableBlock.getFirstChild(); section != null; section = section.getNext()) {
            for (Node rowNode = section.getFirstChild(); rowNode != null; rowNode = rowNode.getNext()) {
                if (rowNode instanceof TableRow) {
                    int columns = 0;
                    for (Node cellNode = rowNode.getFirstChild(); cellNode != null; cellNode = cellNode.getNext()) {
                        if (cellNode instanceof TableCell) {
                            columns++;
                        }
                    }
                    if (columns > 0) {
                        return columns;
                    }
                }
            }
        }
        return 0;
    }

    private static VBox createTableCell(TableCell cell, boolean header) {
        VBox container = new VBox();
        container.getStyleClass().add(header ? TABLE_HEADER_CELL_STYLE_CLASS : TABLE_CELL_STYLE_CLASS);
        container.setPadding(new Insets(8, 10, 8, 10));
        container.setAlignment(Pos.CENTER_LEFT);
        container.setFillWidth(true);

        TextFlow content = createTextFlow(cell, PARAGRAPH_STYLE_CLASS);
        content.setMaxWidth(Double.MAX_VALUE);
        container.getChildren().add(content);

        return container;
    }

    private static TextFlow createTextFlow(Node node, String... styleClasses) {
        TextFlow flow = new TextFlow();
        flow.setLineSpacing(4);
        flow.setPrefWidth(0);
        flow.getStyleClass().addAll(styleClasses);
        appendInlineChildren(node, flow);
        return flow;
    }

    private static void appendInlineChildren(Node parent, TextFlow flow) {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNext()) {
            if (child instanceof Text) {
                flow.getChildren().add(createText(((Text) child).getLiteral(), null));
            } else if (child instanceof StrongEmphasis) {
                flow.getChildren().add(createText(collectLiteral(child), INLINE_STRONG_STYLE_CLASS));
            } else if (child instanceof Emphasis) {
                flow.getChildren().add(createText(collectLiteral(child), INLINE_EMPHASIS_STYLE_CLASS));
            } else if (child instanceof Code) {
                Label code = new Label(((Code) child).getLiteral());
                code.getStyleClass().add(INLINE_CODE_STYLE_CLASS);
                flow.getChildren().add(code);
            } else if (child instanceof SoftLineBreak) {
                flow.getChildren().add(createText(" ", null));
            } else if (child instanceof HardLineBreak) {
                flow.getChildren().add(createText(System.lineSeparator(), null));
            } else if (child instanceof Link) {
                flow.getChildren().add(createHyperlink((Link) child));
            } else {
                appendInlineChildren(child, flow);
            }
        }
    }

    private static Text createText(String content, String styleClass) {
        Text textNode = new Text(content);
        if (styleClass != null) {
            textNode.getStyleClass().add(styleClass);
        }
        return textNode;
    }

    private static Hyperlink createHyperlink(Link link) {
        String label = collectLiteral(link);
        Hyperlink hyperlink = new Hyperlink(label);
        hyperlink.getStyleClass().add(INLINE_LINK_STYLE_CLASS);
        String destination = link.getDestination();
        if (destination != null && !destination.isBlank()) {
            hyperlink.setOnAction(event -> openExternalLink(destination));
        }
        return hyperlink;
    }

    private static void openExternalLink(String destination) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            return;
        }

        try {
            desktop.browse(java.net.URI.create(destination));
        } catch (IOException e) {
            logger.warning("Failed to open link: " + e.getMessage());
        }
    }

    private static String collectLiteral(Node node) {
        StringBuilder builder = new StringBuilder();
        collectLiteral(node, builder);
        return builder.toString();
    }

    private static void collectLiteral(Node node, StringBuilder builder) {
        if (node instanceof Text) {
            builder.append(((Text) node).getLiteral());
            return;
        }

        if (node instanceof Code) {
            builder.append(((Code) node).getLiteral());
            return;
        }

        if (node instanceof SoftLineBreak) {
            builder.append(' ');
            return;
        }

        if (node instanceof HardLineBreak) {
            builder.append(System.lineSeparator());
            return;
        }

        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            collectLiteral(child, builder);
        }
    }

}
