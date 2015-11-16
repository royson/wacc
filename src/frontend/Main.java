package frontend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.WACCLexer;
import antlr.WACCParser;

public class Main {
    public static void main(String[] args) {
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

        SemanticVisitor semVisitor = new SemanticVisitor();
        semVisitor.visit(tree);
    }
}
