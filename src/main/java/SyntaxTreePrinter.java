import io.github.treesitter.jtreesitter.*;


class SyntaxTreePrinter {
    private final Node rootNode;

    public SyntaxTreePrinter(Node rootNode) {
        this.rootNode = rootNode;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        printNode(rootNode, sb, 0);
        return sb.toString();
    }

    private void printNode(Node node, StringBuilder sb, int depth) {
        System.out.println("type: " + node.getType() + node.getSymbol());
        String indent = "  ".repeat(depth);
        Point start = node.getStartPoint();
        Point end = node.getEndPoint();
        sb.append(String.format("%s%s [%s,%s] - [%s,%s]\n",
                indent,
                node.getType(),
                start.row(),
                start.column(),
                end.row(),
                end.column()
        ));

        for (int i = 0; i < node.getChildCount(); i++) {
            node.getChild(i).ifPresent(child -> printNode(child, sb, depth + 1));
        }
    }
}