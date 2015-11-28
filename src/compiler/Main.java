package compiler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.WACCLexer;
import antlr.WACCParser;

public class Main {
    public static void main(String[] args)
                    throws FileNotFoundException,
                    UnsupportedEncodingException {
        String inputFile = args[0];

        // Load file/input stream into ANTLRInputStream input
        ANTLRInputStream input = null;
        try {
            InputStream is = new FileInputStream(inputFile);
            input = new ANTLRInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a lexer that reads from ANTLRInputStream
        WACCLexer lexer = new WACCLexer(input);

        // Create a buffer of tokens read from the lexer
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        // Create a parser that reads form the tokens buffer
        WACCParser parser = new WACCParser(tokenStream);

        // Removes the default error Listener
        parser.removeErrorListeners();
        parser.addErrorListener(new WACCErrorListener());

        ParseTree tree = parser.program();
        SyntaxVisitor synVisitor = new SyntaxVisitor();
        synVisitor.visit(tree);

        // -----------Syntax error-----------
        // syntaxErrorCount - Consists of errors found by the parser
        // (parser.getNumberOfSyntaxErrors())
        // +
        // errors found in exceptional cases (visitor.getSyntaxErrorCount())

        // Error messages from both the default listener and synVisitor
        List<? extends ANTLRErrorListener> errorListeners = parser
                        .getErrorListeners();
        List<String> errorMessages = ((WACCErrorListener) errorListeners
                        .get(0)).getErrorMessages();
        errorMessages.addAll(synVisitor.getErrorMessages());

        int syntaxErrorCount = errorMessages.size();

        System.err.println("-- Compiling...");
        if (syntaxErrorCount > 0) {
            System.err.println("Errors detected during compilation! Exit code 100 returned.");
            for (String errorMessage : errorMessages) {
                System.err.println(errorMessage);
            }
            System.err.println(syntaxErrorCount
                            + " parser error(s) detected, no further compilation attempted.");
            System.out.println("#syntax error#");
            System.exit(100);
        }

        // No syntax errors found, do semantic checks
        SemanticVisitor semVisitor = new SemanticVisitor();
        semVisitor.visit(tree);

        // No semantic errors found, being generating code
        CodeGenVisitor codeVisitor = new CodeGenVisitor();
        codeVisitor.visit(tree);

        // Print the assembly code
        List<String> data = codeVisitor.getData();
        List<String> text = codeVisitor.getText();

        String filename = inputFile.substring(inputFile
                        .lastIndexOf("/") + 1);
        filename = filename.substring(0, filename.lastIndexOf("."))
                        + ".s";
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        if (!data.isEmpty()) {
            // Print data array
        } else {
            writer.write(".text\n");
            writer.write("\n");
            writer.write(".global main\n");
            for (String s : text) {
                char firstChar = s.charAt(0);
                if ((firstChar >= 'A' && firstChar <= 'Z')
                                || firstChar == '.') {
                    writer.write("    ");
                }
                writer.write(s + "\n");
            }
        }

        writer.close();
        System.err.println("-- Finished");
    }
}
