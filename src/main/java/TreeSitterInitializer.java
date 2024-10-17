import io.github.treesitter.jtreesitter.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class TreeSitterInitializer {

    private Path tempDir;
    private Language language;

    public Language initializeLanguage(String libraryName, String languageName) throws IOException {
        // Create a temporary directory for the native libraries
        tempDir = Files.createTempDirectory("tree-sitter-lib");
        System.out.println("Temporary directory created: " + tempDir);

        // Copy the library to the temporary directory
        Path libPath = copyLibrary(tempDir, libraryName);

        // Load the symbols from the library
        System.out.println("Loading symbols from the " + languageName + " library...");
        SymbolLookup symbols = SymbolLookup.libraryLookup(libPath, Arena.global());

        // Load the language
        System.out.println("Loading the " + languageName + " language...");
        language = Language.load(symbols, languageName);

        return language;
    }

    private Path copyLibrary(Path tempDir, String libraryName) throws IOException {
        Path libraryPath = tempDir.resolve(libraryName);
        try (InputStream is = getClass().getResourceAsStream("/native/macos/" + libraryName)) {
            if (is == null) {
                throw new RuntimeException("Could not find " + libraryName + " in resources");
            }
            Files.copy(is, libraryPath, StandardCopyOption.REPLACE_EXISTING);
        }
        System.out.println(libraryName + " copied to: " + libraryPath);
        return libraryPath;
    }

    public void cleanup() {
        // Add cleanup logic here if needed
        // For example, deleting the temporary directory
        if (tempDir != null) {
            try {
                Files.walk(tempDir)
                        .sorted((p1, p2) -> -p1.compareTo(p2))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("Failed to delete " + path + ": " + e.getMessage());
                            }
                        });
            } catch (IOException e) {
                System.err.println("Failed to clean up temporary directory: " + e.getMessage());
            }
        }
    }
}