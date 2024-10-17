import io.github.treesitter.jtreesitter.*;

public class Example {
    public static void main(String[] args) {
        TreeSitterInitializer initializer = new TreeSitterInitializer();
        try {
            Language pythonLanguage = initializer.initializeLanguage("libtree-sitter-python.dylib", "tree_sitter_python");

            // Create a parser
            System.out.println("Creating parser...");
            Parser parser = new Parser();
            parser.setLanguage(pythonLanguage);
            System.out.println("Parser created successfully!");

            // Test a sample Python code
            String pythonCode = "print(\"Hello World!\")";
            System.out.println("Parsing code: " + pythonCode);

            // Generate AST
            try (Tree tree = parser.parse(pythonCode, InputEncoding.UTF_8).orElseThrow()) {
                Node rootNode = tree.getRootNode();
                ASTUtils.ASTNode ast = ASTUtils.buildASTWithCursor(rootNode);
                System.out.println("\nAST Structure:");
                ASTUtils.printAST(ast, 0);
            }
            parser.close();

        } catch (Exception e) {
            System.err.println("An error occurred:");
            e.printStackTrace();
        } finally {
            initializer.cleanup();
        }
    }


}