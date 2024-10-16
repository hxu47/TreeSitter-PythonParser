# Tree-sitter-Java Python Parser

A Java program, running in Maven and utilizing Java bindings for the Tree-sitter parsing library, processes a sample Python code to generate an Abstract Syntax Tree (AST). 

## Prerequisites

- Java Development Kit (JDK) 22 
- Maven
- Git

## Setup

1. Maven dependency for tree-sitter-java: https://central.sonatype.com/artifact/io.github.tree-sitter/jtreesitter/overview.
   Add this snippets to pom.xml:
   ```
   <dependency>
    <groupId>io.github.tree-sitter</groupId>
    <artifactId>jtreesitter</artifactId>
    <version>0.23.2</version>
   </dependency>
   ```

2. Build the core tree-sitter library:
   ```
   git clone https://github.com/tree-sitter/tree-sitter.git
   cd tree-sitter
   make
   ```
   This will produce `libtree-sitter.dylib`. Copy this file to the root of your project.

3. Build the Python-specific tree-sitter library:
   ```
   git clone https://github.com/tree-sitter/tree-sitter-python.git
   cd tree-sitter-python
   cc -c -I./src src/parser.c
   cc -shared -I./src src/parser.c src/scanner.c -o libtree-sitter-python.dylib
   ```
   This will produce `libtree-sitter-python.dylib`. Copy it to `src/main/resources/native/macos/` in your project.


4. Update your `pom.xml` to include the native library directory:
   ```xml
   <build>
     <resources>
       <resource>
         <directory>src/main/resources</directory>
         <includes>
           <include>**/*.dylib</include>
         </includes>
       </resource>
     </resources>
   </build>
   ```

## Running the Program

1. If you encounter a warning about native access, modify your run configuration:
   - In IntelliJ IDEA: Go to Run → Edit Configurations → Modify options → Add VM options
   - Add this line: `--enable-native-access=ALL-UNNAMED`


## Project Structure

- `src/main/java/`: Java source files
- `src/main/resources/native/macos/`: Directory for macOS-specific native libraries
- `libtree-sitter.dylib`: Core tree-sitter library (in project root)
- `pom.xml`: Maven project configuration file

## Troubleshooting

- Ensure all native libraries (`.dylib` files) are in the correct locations.
- If you're using a different operating system, you may need to compile the native libraries for your specific OS and adjust the paths accordingly.

