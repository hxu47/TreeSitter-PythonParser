import io.github.treesitter.jtreesitter.*;

import java.util.ArrayList;
import java.util.List;

public class ASTUtils {

    public static class ASTNode {
        public String type;
        public String fieldName;
        public List<ASTNode> children;

        public ASTNode(String type, String fieldName) {
            this.type = type;
            this.fieldName = fieldName;
            this.children = new ArrayList<>();
        }

        public void addChild(ASTNode child) {
            children.add(child);
        }
    }

    public static ASTNode buildASTWithCursor(Node rootNode) {
        try (TreeCursor cursor = rootNode.walk()) {
            return buildNodeWithCursor(cursor);
        }
    }

    private static ASTNode buildNodeWithCursor(TreeCursor cursor) {
        String nodeType = cursor.getCurrentNode().getType();
        String fieldName = cursor.getCurrentFieldName();
        ASTNode node = new ASTNode(nodeType, fieldName);

        if (cursor.gotoFirstChild()) {
            do {
                node.addChild(buildNodeWithCursor(cursor));
            } while (cursor.gotoNextSibling());
            cursor.gotoParent();
        }

        return node;
    }

    public static void printAST(ASTNode node, int depth) {
        String indent = "  ".repeat(depth);
        String fieldInfo = node.fieldName != null ? " (" + node.fieldName + ")" : "";
        System.out.printf("%s%s%s\n", indent, node.type, fieldInfo);
        for (ASTNode child : node.children) {
            printAST(child, depth + 1);
        }
    }
}