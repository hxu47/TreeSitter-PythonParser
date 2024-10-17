import io.github.treesitter.jtreesitter.*;

public class Example {
    public static void main(String[] args) {
        TreeSitterInitializer initializer = new TreeSitterInitializer();
        try {
            String grammar = "python";
            String libraryName = "";
            String treeSitterName ="";
            switch (grammar) {
                case "javascript":
                    libraryName = "libtree-sitter-javascript.dylib";
                    treeSitterName = "tree_sitter_javascript";
                    break;
                case "cpp":
                    libraryName = "libtree-sitter-cpp.dylib";
                    treeSitterName = "tree_sitter_cpp";
                    break;
                case "python":
                    libraryName = "libtree-sitter-python.dylib";
                    treeSitterName = "tree_sitter_python";
                    break;
            }
            Language grammarLanguage = initializer.initializeLanguage(libraryName, treeSitterName);


            // Create a parser
            System.out.println("Creating parser...");
            Parser parser = new Parser();
            parser.setLanguage(grammarLanguage);
            System.out.println("Parser created successfully!");

            // Test a sample Python code
            String sampleCode = "print(\"Hello World!\")";
            //String sampleCode = "console.log(\"Hello, World!\");";
//            String sampleCode = "#include <iostream>\n" +
//                    "\n" +
//                    "int main() {\n" +
//                    "    std::cout << \"Hello, World!\" << std::endl;\n" +
//                    "    return 0;\n" +
//                    "}";
            System.out.println("Parsing code:\n" + sampleCode);

            try (Tree tree = parser.parse(sampleCode, InputEncoding.UTF_8).orElseThrow()) {
                Node rootNode = tree.getRootNode();
                ASTUtils.ASTNode ast = ASTUtils.buildASTWithCursor(rootNode);
                System.out.println("\nAST Structure:");
                System.out.println(ASTUtils.generateASTOutput(ast, 0));
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