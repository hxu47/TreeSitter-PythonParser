import io.github.treesitter.jtreesitter.*;

import java.util.ArrayList;
import java.util.List;

public class ASTUtils {

    public static class ASTNode {
        public String type;
        public String fieldName;
        public List<ASTNode> children;
        public Point startPoint;
        public Point endPoint;

        public ASTNode(String type, String fieldName, Point startPoint, Point endPoint) {
            this.type = type;
            this.fieldName = fieldName;
            this.children = new ArrayList<>();
            this.startPoint = startPoint;
            this.endPoint = endPoint;
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
        Node currentNode = cursor.getCurrentNode();
        String nodeType = currentNode.getType();
        String fieldName = cursor.getCurrentFieldName();
        Point startPoint = currentNode.getStartPoint();
        Point endPoint = currentNode.getEndPoint();
        ASTNode node = new ASTNode(nodeType, fieldName, startPoint, endPoint);

        if (cursor.gotoFirstChild()) {
            do {
                node.addChild(buildNodeWithCursor(cursor));
            } while (cursor.gotoNextSibling());
            cursor.gotoParent();
        }

        return node;
    }

    public static String generateASTOutput(ASTNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(depth);

        sb.append(indent).append(node.type)
                .append(" [").append(node.startPoint.row()).append(", ").append(node.startPoint.column()).append("] - ")
                .append("[").append(node.endPoint.row()).append(", ").append(node.endPoint.column()).append("]\n");

        for (ASTNode child : node.children) {
            if (child.fieldName != null && !child.fieldName.isEmpty()) {
                sb.append(indent).append("  ").append(child.fieldName).append(":\n");
                if (child.type.equals("argument_list")) {
                    sb.append(generateASTOutput(child, depth + 1));
                } else {
                    sb.append(generateASTOutput(child, depth + 2));
                }
            } else {
                sb.append(generateASTOutput(child, depth + 1));
            }
        }

        return sb.toString();
    }
}