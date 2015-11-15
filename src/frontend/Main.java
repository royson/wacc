package frontend;

import java.io.*;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.*;

public class Main {
    public static void main(String[] args) {
        // System.out.println(args[0]);
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
        lexer.removeErrorListeners();

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
        int syntaxErrorCount = synVisitor.getSyntaxErrorCount()
                        + parser.getNumberOfSyntaxErrors();
        
        List<? extends ANTLRErrorListener> errorListeners = parser
                        .getErrorListeners();
        List<String> errorMessages = ((WACCErrorListener) errorListeners
                        .get(0)).getErrorMessages();
        errorMessages.addAll(synVisitor.getErrorMessages());
        
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
