import io.github.treesitter.jtreesitter.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;


public class Example {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java FileInputASTParser <grammar> <filepath>");
            System.out.println("Supported grammars: javascript, cpp, python");
            return;
        }

        String grammar = args[0];
        String filePath = args[1];

        TreeSitterInitializer initializer = new TreeSitterInitializer();

        try{
            String libraryName = "";
            String treeSitterName = "";
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
                default:
                    System.out.println("Unsupported grammar: " + grammar);
                    return;
            }
            Language grammarLanguage = initializer.initializeLanguage(libraryName, treeSitterName);

            // Create a parser
            System.out.println("Creating parser...");
            Parser parser = new Parser();
            parser.setLanguage(grammarLanguage);
            System.out.println("Parser created successfully!");

            // Read the file contents
            String fileContent = readFile(filePath);
            System.out.println("Parsing file: " + filePath);
            System.out.println("Parsing code:\n" + fileContent);

            // Print Syntax Tree
            try (Tree tree = parser.parse(fileContent, InputEncoding.UTF_8).orElseThrow()) {
                Node rootNode = tree.getRootNode();


//                // Create a SyntaxTreePrinter
//                SyntaxTreePrinter printer = new SyntaxTreePrinter(rootNode);
//                // Generate and print the AST
//                String ast = printer.print();
//                System.out.println("Abstract Syntax Tree:");
//                System.out.println(ast);
                //printSyntaxTree(rootNode, 0);
                // Use ASTUtils to build and print the AST
                ASTUtils.ASTNode astRoot = ASTUtils.buildASTWithCursor(rootNode);
                String astString = ASTUtils.printAST(astRoot, 0);
                System.out.println("Abstract Syntax Tree:");
                System.out.println(astString);
            }
            parser.close();
        } catch (Exception e) {
            System.err.println("An error occurred:");
            e.printStackTrace();
        } finally {
            initializer.cleanup();
        }
    }
    private static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

}