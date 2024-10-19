import io.github.treesitter.jtreesitter.*;

import java.util.function.Consumer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TreeSitterPrinter implements TreePrinter {
    private final Tree tree;

    public TreeSitterPrinter(Tree tree) {
        this.tree = tree;
    }

    @Override
    public String print() {
        StringBuilder stringBuilder = new StringBuilder(getPreamble());
        write(stringBuilder::append);
        return stringBuilder.toString();
    }

    @Override
    public File export() throws IOException {
        File file = Files.createTempFile("treesitter-export-", getFileExtension()).toFile();
        try (var writer = Files.newBufferedWriter(file.toPath())) {
            writer.write(getPreamble());
            Consumer<String> appender = s -> {
                try {
                    writer.write(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
            write(appender);
            return file;
        }
    }

    protected String getPreamble() {
        return "TreeSitter AST:\n";
    }

    protected String getFileExtension() {
        return ".txt";
    }

    protected void write(Consumer<String> appender) {
        try (TreeCursor cursor = tree.getRootNode().walk()) {
            for (;;) {
                Node cursorNode = cursor.getCurrentNode();
                if (cursorNode.isNamed()) {
                    int depth = cursor.getCurrentDepth();
                    String indent = "  ".repeat(depth);
                    String fieldName = cursor.getCurrentFieldName();
                    String nodeRepresentation = String.format("%s%s: %s [%s] - [%s]\n",
                            indent,
                            fieldName != null ? fieldName : "",
                            cursorNode.getType(),
                            cursorNode.getStartPoint(),
                            cursorNode.getEndPoint());
                    appender.accept(nodeRepresentation);
                }
                if (cursor.gotoFirstChild() || cursor.gotoNextSibling()) continue;
                do {
                    if (!cursor.gotoParent()) return;
                } while (!cursor.gotoNextSibling());
            }
        }
    }
}

interface TreePrinter {
    String print();
    File export() throws IOException;
}
