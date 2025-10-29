package seedu.address.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Lightweight renderer that converts a small Markdown subset into styled JavaFX nodes.
 */
public class MarkdownRenderer {

    private static final Pattern HEADING_PATTERN = Pattern.compile("^(#{1,6})\\s+(.*)$");
    private static final Pattern BULLET_PATTERN = Pattern.compile("^(\\s*)-\\s+(.*)$");
    private static final Pattern TABLE_SEPARATOR_PATTERN = Pattern.compile("^\\|\\s*[-:]+.*$");

    /**
     * Renders Markdown content as a list of JavaFX nodes.
     */
    public List<Node> render(String markdownContent) {
        List<Node> nodes = new ArrayList<>();
        if (markdownContent == null || markdownContent.isBlank()) {
            return nodes;
        }

        List<String> lines = Arrays.asList(markdownContent.split("\\R", -1));
        int index = 0;
        while (index < lines.size()) {
            String line = lines.get(index);
            if (line.trim().isEmpty()) {
                index++;
                continue;
            }

            Matcher headingMatcher = HEADING_PATTERN.matcher(line);
            if (headingMatcher.matches()) {
                int level = Math.min(3, headingMatcher.group(1).length());
                String content = headingMatcher.group(2).trim();
                Node heading = createHeading(level, content);
                nodes.add(heading);
                index++;
                continue;
            }

            if (looksLikeTableStart(lines, index)) {
                List<String> tableLines = new ArrayList<>();
                while (index < lines.size() && isTableLine(lines.get(index))) {
                    tableLines.add(lines.get(index));
                    index++;
                }
                nodes.add(createTable(tableLines));
                continue;
            }

            Matcher bulletMatcher = BULLET_PATTERN.matcher(line);
            if (bulletMatcher.matches()) {
                List<BulletItem> items = new ArrayList<>();
                while (index < lines.size()) {
                    Matcher matcher = BULLET_PATTERN.matcher(lines.get(index));
                    if (!matcher.matches()) {
                        break;
                    }
                    items.add(new BulletItem(matcher.group(1).length(), matcher.group(2).trim()));
                    index++;
                }
                nodes.add(createList(items));
                continue;
            }

            StringBuilder paragraphBuilder = new StringBuilder();
            while (index < lines.size()) {
                String current = lines.get(index);
                if (current.trim().isEmpty()
                        || HEADING_PATTERN.matcher(current).matches()
                        || BULLET_PATTERN.matcher(current).matches()
                        || looksLikeTableStart(lines, index)) {
                    break;
                }
                if (paragraphBuilder.length() > 0) {
                    paragraphBuilder.append('\n');
                }
                paragraphBuilder.append(current.trim());
                index++;
            }

            if (paragraphBuilder.length() > 0) {
                nodes.add(createParagraph(paragraphBuilder.toString()));
            }
        }
        return nodes;
    }

    private boolean isTableLine(String line) {
        String trimmed = line.trim();
        return trimmed.startsWith("|") && trimmed.endsWith("|");
    }

    private boolean looksLikeTableStart(List<String> lines, int index) {
        if (index + 1 >= lines.size()) {
            return false;
        }
        String header = lines.get(index).trim();
        String separator = lines.get(index + 1).trim();
        return isTableLine(header) && TABLE_SEPARATOR_PATTERN.matcher(separator).matches();
    }

    private Node createHeading(int level, String content) {
        TextFlow flow = createInlineTextFlow(content, List.of("markdown-text"));
        flow.getStyleClass().add("markdown-heading");
        flow.getStyleClass().add("markdown-heading-" + level);
        flow.setLineSpacing(2);
        VBox.setMargin(flow, new Insets(level == 1 ? 10 : 6, 0, 4, 0));
        return flow;
    }

    private Node createParagraph(String content) {
        TextFlow paragraph = createInlineTextFlow(content, List.of("markdown-text"));
        paragraph.getStyleClass().add("markdown-paragraph");
        paragraph.setLineSpacing(4);
        paragraph.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(paragraph, new Insets(0, 0, 8, 0));
        return paragraph;
    }

    private Node createList(List<BulletItem> items) {
        VBox listBox = new VBox();
        listBox.getStyleClass().add("markdown-list");
        for (BulletItem item : items) {
            HBox row = new HBox();
            row.getStyleClass().add("markdown-list-item");
            row.setAlignment(Pos.TOP_LEFT);
            row.setPadding(new Insets(0, 0, 0, computeIndent(item.indentLevel())));

            Label bullet = new Label("\u2022");
            bullet.getStyleClass().add("markdown-bullet");
            bullet.setMinWidth(14);

            TextFlow content = createInlineTextFlow(item.content(), List.of("markdown-text"));
            content.getStyleClass().add("markdown-list-content");
            content.setLineSpacing(3);
            content.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(content, Priority.ALWAYS);

            row.getChildren().addAll(bullet, content);
            listBox.getChildren().add(row);
        }
        VBox.setMargin(listBox, new Insets(0, 0, 8, 0));
        return listBox;
    }

    private double computeIndent(int level) {
        return Math.max(0, level) * 18.0;
    }

    private Node createTable(List<String> tableLines) {
        GridPane table = new GridPane();
        table.getStyleClass().add("markdown-table");

        List<List<String>> rows = new ArrayList<>();
        for (String line : tableLines) {
            if (TABLE_SEPARATOR_PATTERN.matcher(line.trim()).matches()) {
                continue;
            }
            rows.add(splitTableRow(line));
        }

        int columnCount = rows.isEmpty() ? 0 : rows.get(0).size();
        for (int col = 0; col < columnCount; col++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            table.getColumnConstraints().add(column);
        }

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            List<String> rowValues = rows.get(rowIndex);
            for (int colIndex = 0; colIndex < rowValues.size(); colIndex++) {
                Label cell = new Label(rowValues.get(colIndex));
                cell.setWrapText(true);
                cell.setMaxWidth(Double.MAX_VALUE);
                if (rowIndex == 0) {
                    cell.getStyleClass().add("markdown-table-header");
                } else {
                    cell.getStyleClass().add("markdown-table-cell");
                }
                table.add(cell, colIndex, rowIndex);
            }
        }

        VBox.setMargin(table, new Insets(4, 0, 12, 0));
        return table;
    }

    private List<String> splitTableRow(String line) {
        String trimmed = line.trim();
        if (trimmed.length() <= 2) {
            return List.of();
        }
        String[] parts = trimmed.substring(1, trimmed.length() - 1).split("\\|");
        List<String> cells = new ArrayList<>(parts.length);
        for (String part : parts) {
            cells.add(part.trim());
        }
        return cells;
    }

    private TextFlow createInlineTextFlow(String content, List<String> baseTextClasses) {
        TextFlow flow = new TextFlow();
        flow.setMaxWidth(Double.MAX_VALUE);
        int index = 0;
        while (index < content.length()) {
            char current = content.charAt(index);
            if (current == '\n') {
                Text newline = new Text("\n");
                newline.getStyleClass().addAll(baseTextClasses);
                flow.getChildren().add(newline);
                index++;
                continue;
            }

            if (content.startsWith("**", index)) {
                int end = content.indexOf("**", index + 2);
                if (end > index + 2) {
                    String boldText = content.substring(index + 2, end);
                    Text bold = new Text(boldText);
                    bold.getStyleClass().addAll(baseTextClasses);
                    bold.getStyleClass().add("markdown-strong");
                    flow.getChildren().add(bold);
                    index = end + 2;
                    continue;
                }
            }

            if (current == '`') {
                int end = content.indexOf('`', index + 1);
                if (end > index + 1) {
                    String codeText = content.substring(index + 1, end);
                    Label codeLabel = new Label(codeText);
                    codeLabel.setWrapText(true);
                    codeLabel.getStyleClass().addAll(baseTextClasses);
                    codeLabel.getStyleClass().add("markdown-code");
                    flow.getChildren().add(codeLabel);
                    index = end + 1;
                    continue;
                }
            }

            int nextSpecial = findNextSpecialIndex(content, index);
            String plainSegment = content.substring(index, nextSpecial);
            Text plain = new Text(plainSegment);
            plain.getStyleClass().addAll(baseTextClasses);
            flow.getChildren().add(plain);
            index = nextSpecial;
        }
        return flow;
    }

    private int findNextSpecialIndex(String content, int start) {
        int nextBold = content.indexOf("**", start);
        int nextCode = content.indexOf('`', start);
        int nextNewline = content.indexOf('\n', start);

        int nextIndex = content.length();
        if (nextBold != -1 && nextBold < nextIndex) {
            nextIndex = nextBold;
        }
        if (nextCode != -1 && nextCode < nextIndex) {
            nextIndex = nextCode;
        }
        if (nextNewline != -1 && nextNewline < nextIndex) {
            nextIndex = nextNewline;
        }
        return nextIndex;
    }

    private static class BulletItem {
        private final int indent;
        private final String content;

        private BulletItem(int indent, String content) {
            this.indent = indent;
            this.content = content;
        }

        private int indentLevel() {
            return indent / 4;
        }

        private String content() {
            return content;
        }
    }
}
